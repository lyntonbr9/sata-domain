package sata.domain.simulacao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;
import sata.metastock.util.BlackScholes;
import sata.metastock.util.CalculoUtil;

//TODO TEM QUE RESOLVER ESSE
public class SimulacaoReinvestimentoCALLePUT implements ISimulacao, IConstants{

	static Logger logger = Logger.getLogger(SimulacaoReinvestimentoCALLePUT.class.getName());
	
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		int QTD_DIAS_OPCAO = 20;
		int tamanhoArray = listaDasCotacoes.size()/QTD_DIAS_OPCAO + 1; //Tamanho para guardar as operacoes
		double variacaoAcao[] = new double[tamanhoArray];
		double variacaoATM[] = new double[tamanhoArray];
		double variacaoPUT[] = new double[tamanhoArray];
		double ganhoVEVendaCobMuitoITM[] = new double[tamanhoArray];
		double totalGanhoVEVendaCobMuitoITM = 0.0;
		
		double valorCALL_ATM[] = new double[tamanhoArray];
		double gastoCALL_ATM[] = new double[tamanhoArray];
		double ganhoCALL_ATM[] = new double[tamanhoArray];
		double totalGastoCALL_ATM = 0.0;
		double totalGanhoCALL_ATM = 0.0;
		
		double valorPUT_ATM[] = new double[tamanhoArray];		
		double gastoPUT_ATM[] = new double[tamanhoArray];
		double ganhoPUT_ATM[] = new double[tamanhoArray];
		double totalGastoPUT_ATM = 0.0;
		double totalGanhoPUT_ATM = 0.0;

		double totalCaixa = 0.0;
		int indiceOperacao = 0;
		
		double fechamentoAnterior = 0.0;
		double fechamentoCorrente = 0.0;
		double fechamentoAcaoNaCALL_ATM = 0.0;
		double fechamentoAcaoNaPUT_ATM = 0.0;
		
		double volatilidade = CalculoUtil.getVolatilidade();
		double qtdDiasFaltaEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);
		
		int QTD_CALLS = 1;
//		double PCTE_2_CALL_ITM = 0.01; //valor de 2 ITM sendo que foi comprado 2 lotes de ações
//		double PCTE_1_CALL_ATM = 0.035; //metade 0.0175 (3,5% a CALL) quando se trabalha com 2 lotes
//		double PCTE_1_PUT_ATM = 0.03; //metade 0.015 (3,0% a PUT) quando se trabalha com 2 lotes
//		double PCTE_1_PUT_ATM = 0.026; //metade 0.015 (3,0% a PUT) quando se trabalha com 2 lotes
		int QTD_LOTES = 2;
		int j = 0; //indice para as ATMs (CALL e PUT)
		for(int i = 0; i <listaDasCotacoes.size(); i++)
		{
			CotacaoAtivoTO caTOAnterior = listaDasCotacoes.get(i); //pega a cotacao anterior
			fechamentoAnterior = Double.parseDouble(caTOAnterior.getFechamento());
			logger.info(caTOAnterior.getCodigo() + ": " + i + " " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
			i = i + QTD_DIAS_OPCAO -1; //vai para o vencimento da opcao (a opcao dura 20 dias uteis) diminui 1 pq a lista comeca com indice 0
			if (i >= listaDasCotacoes.size()) 
				break; //se depois no futuro ultrapassar o tamanho da lista termina a simulacao
			
			CotacaoAtivoTO caTOCorrente = listaDasCotacoes.get(i); //pega a cotacao corrente no vencimento da opcao
			fechamentoCorrente = Double.parseDouble(caTOCorrente.getFechamento());
			logger.info(caTOCorrente.getCodigo() + ": " + i + " " + caTOCorrente.getPeriodo() + " - F: " + fechamentoCorrente);

			//calcula a variacao da acao
			variacaoAcao[indiceOperacao] = fechamentoCorrente - fechamentoAnterior; //pega a variacao da acao
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			
			//calcula ganhoVEVendaCobertaMuitoITM
			//se a acao nao cair ate o preco de exercicio da ITM voce ganha 1% de VE
//			double ganhoVECobertaITM = PCTE_2_CALL_ITM * fechamentoAnterior * QTD_LOTES;
			
			double precoExercOpcaoCob2ITM = BlackScholes.getPrecoExercicio(false, fechamentoAnterior, 2); // Pega a 2 ITM para baixo
			logger.info("precoExercOpcaoCob2ITM: " + precoExercOpcaoCob2ITM);
			double valorOpcaoCoberta2ITM = BlackScholes.blackScholes(true, fechamentoAnterior, precoExercOpcaoCob2ITM, qtdDiasFaltaEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoCoberta2ITM: " + valorOpcaoCoberta2ITM);
			double ganhoVECobertaITM = BlackScholes.getVE(true, fechamentoAnterior, precoExercOpcaoCob2ITM, valorOpcaoCoberta2ITM) * QTD_LOTES;
			logger.info("ganhoVECobertaITM: " + ganhoVECobertaITM);
			ganhoVEVendaCobMuitoITM[indiceOperacao] = ganhoVECobertaITM;
			logger.info("ganhoVEVendaCobMuitoITM[" + indiceOperacao + "]=" + ganhoVEVendaCobMuitoITM[indiceOperacao]);
			totalGanhoVEVendaCobMuitoITM+=ganhoVECobertaITM;
			totalCaixa+=ganhoVECobertaITM; //atualiza o caixa com acrescimo quando o lancamento coberto perde VE
			
			//calcula o ganho na call ATM caso a acao tenha subido
			CotacaoAtivoTO caTONaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			fechamentoAcaoNaCALL_ATM = Double.parseDouble(caTONaATM.getFechamento());
			logger.info(caTONaATM.getCodigo() + " na ATM: " + j + " " + caTONaATM.getPeriodo() + " - F: " + fechamentoAcaoNaCALL_ATM);
			variacaoATM[indiceOperacao] = fechamentoCorrente - fechamentoAcaoNaCALL_ATM;
			if(variacaoATM[indiceOperacao] > 0)
			{
				ganhoCALL_ATM[indiceOperacao] =  variacaoATM[indiceOperacao];
				totalGanhoCALL_ATM+=ganhoCALL_ATM[indiceOperacao];
				totalCaixa+=ganhoCALL_ATM[indiceOperacao];
			}
			logger.info("ganhoCALL_ATM[" + indiceOperacao + "]=" + ganhoCALL_ATM[indiceOperacao]);
			
			//calcula o gasto na call ATM
			double precoExercOpcaoCALL_ATM = BlackScholes.getPrecoExercicio(true, fechamentoAcaoNaCALL_ATM, 0); // Pega a CALL ATM
			logger.info("precoExercCALL_ATM: " + precoExercOpcaoCALL_ATM);
			double valorOpcaoCALL_ATM = BlackScholes.blackScholes(true, fechamentoAcaoNaCALL_ATM, precoExercOpcaoCALL_ATM, qtdDiasFaltaEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoCALL_ATM: " + valorOpcaoCALL_ATM);
			double gastoCallNaATM = BlackScholes.getVE(true, fechamentoAcaoNaCALL_ATM, precoExercOpcaoCALL_ATM, valorOpcaoCALL_ATM); //perde o VE no vencimento
			logger.info("gastoCallNaATM: " + gastoCallNaATM);
			
//			double gastoCallNaATM = PCTE_1_CALL_ATM * fechamentoAcaoNaCALL_ATM;
			valorCALL_ATM[indiceOperacao] = gastoCallNaATM;
			double gastoCompraATM = QTD_CALLS * gastoCallNaATM;
			gastoCALL_ATM[indiceOperacao] = gastoCompraATM;
			totalGastoCALL_ATM+=gastoCompraATM;
			totalCaixa-=gastoCompraATM; //retira o dinheiro da call ATM do caixa
			

			//calcula o ganho na PUT ATM caso a acao tenha caido
			CotacaoAtivoTO caTOPUTNaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			fechamentoAcaoNaPUT_ATM = Double.parseDouble(caTOPUTNaATM.getFechamento());
			logger.info(caTOPUTNaATM.getCodigo() + " na PUT ATM: " + j + " " + caTOPUTNaATM.getPeriodo() + " - F: " + fechamentoAcaoNaPUT_ATM);
			variacaoPUT[indiceOperacao] = (fechamentoCorrente - fechamentoAcaoNaCALL_ATM) * (-1); //fica positivo caso cair
			if(variacaoPUT[indiceOperacao] > 0)
			{
				ganhoPUT_ATM[indiceOperacao] = QTD_CALLS * variacaoPUT[indiceOperacao];
				totalGanhoPUT_ATM+=ganhoPUT_ATM[indiceOperacao];
				totalCaixa+=ganhoPUT_ATM[indiceOperacao];
			}
			logger.info("ganhoPUT_ATM[" + indiceOperacao + "]=" + ganhoPUT_ATM[indiceOperacao]);
			
			//calcula o gasto na PUT ATM
			double precoExercOpcaoPUT_ATM = BlackScholes.getPrecoExercicio(true, fechamentoAcaoNaPUT_ATM, 0); // Pega a PUT ATM
			logger.info("precoExercOpcaoPUT_ATM: " + precoExercOpcaoPUT_ATM);
			double valorOpcaoPUT_ATM = BlackScholes.blackScholes(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, qtdDiasFaltaEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoPUT_ATM: " + valorOpcaoPUT_ATM);
			double gastoPUTNaATM = BlackScholes.getVE(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, valorOpcaoPUT_ATM); //perde o VE no vencimento
			logger.info("gastoPUTNaATM: " + gastoPUTNaATM);

//			double gastoPUTNaATM = PCTE_1_PUT_ATM * fechamentoAcaoNaPUT_ATM;
			valorPUT_ATM[indiceOperacao] = gastoPUTNaATM;
			double gastoCompraPUT_ATM = QTD_CALLS * gastoPUTNaATM;
			gastoPUT_ATM[indiceOperacao] = gastoCompraPUT_ATM;
			totalGastoPUT_ATM+=gastoCompraPUT_ATM;
			totalCaixa-=gastoCompraPUT_ATM; //retira o dinheiro da PUT ATM do caixa
				
			logger.info("valorCALL_ATM[" + indiceOperacao + "]=" + valorCALL_ATM[indiceOperacao]);
			logger.info("gastoCALL_ATM[" + indiceOperacao + "]=" + gastoCALL_ATM[indiceOperacao]);
			logger.info("valorPUT_ATM[" + indiceOperacao + "]=" + valorPUT_ATM[indiceOperacao]);
			logger.info("gastoPUT_ATM[" + indiceOperacao + "]=" + gastoPUT_ATM[indiceOperacao]);
			
			logger.info("totalCaixa[" + indiceOperacao + "]=" + totalCaixa);
			
			logger.info(" ");
			
			j = j + QTD_DIAS_OPCAO - 1; //vai para a proxima ATM
			indiceOperacao++; //parte para a proxima operacao
			i--; //deve-se subtrair 1 porque vai ser incrementado depois no LACO DO FOR

		}
		
		//imprime os valores encontrados
		//calcula o valor total ganho NA ACAO
		double primeiraCotacaoAcao = Double.parseDouble(listaDasCotacoes.get(0).getFechamento());
		logger.info("GANHOS COM A ACAO APENAS:");
		logger.info("primeiraCotacaoAcao=" + primeiraCotacaoAcao);
		logger.info("ultimaCotacaoAcao=" + fechamentoCorrente);
		double totalGanhoAcao = (fechamentoCorrente - primeiraCotacaoAcao) * QTD_LOTES;
		logger.info("totalGanhoAcao=" + totalGanhoAcao);
		double pctGanhoAcao=totalGanhoAcao*100/(primeiraCotacaoAcao * QTD_LOTES);
		logger.info("pctGanhoAcao=" + pctGanhoAcao);
		
		//calcula o valor total ganho NAS OPCOES
		logger.info("GANHOS COM A SIMULACAO DE REINVESTIMENTO:");
		logger.info("totalCaixa=" + totalCaixa);
		logger.info("totalGanhoVEVendaCobMuitoITM=" + totalGanhoVEVendaCobMuitoITM);
		logger.info("totalGanhoCALL_ATM=" + totalGanhoCALL_ATM);
		logger.info("totalGastoCALL_ATM=" + totalGastoCALL_ATM);
		logger.info("totalGanhoPUT_ATM=" + totalGanhoPUT_ATM);
		logger.info("totalGastoPUT_ATM=" + totalGastoPUT_ATM);
		
		double totalFinalGanhoSimulacao = primeiraCotacaoAcao + totalCaixa;
		logger.info("totalFinalGanhoSimulacao=primeiraCotacaoAcao + totalCaixa=" + totalFinalGanhoSimulacao);
		
		//calcula a porcentagem em relacao ao comeco do investimetno
		double pctGanhoFinalGanhoSimulacao = (totalFinalGanhoSimulacao-primeiraCotacaoAcao)*100/(primeiraCotacaoAcao*QTD_LOTES);
		logger.info("pctGanhoFinalGanhoSimulacao=" + pctGanhoFinalGanhoSimulacao);
		
		logger.info(" ");
		
		// TODO Auto-generated method stub
		return null;
	}

	private void imprimeVetor(String nomeVetor, double obj[]){
		
		double total=0;
		for(int i = 0; i < obj.length; i++)
		{
			logger.info(nomeVetor + "[" + i + "]=" + obj[i]);
			total = total + obj[i];
		}
		logger.info("TOTAL " + nomeVetor + "=" + total);
	}
	

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws SQLException {
//		BasicConfigurator.configure();
		
		PropertyConfigurator.configure("log4j.properties");
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		/* Simulacao 2007 e 2008 VALE 
		List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo("VALE5", "2007");
		List<CotacaoAtivoTO> listaDasCotacoes2008 = caDAO.getCotacoesDoAtivo("VALE5", "2008");
		List<CotacaoAtivoTO> novaLista = new ArrayList<CotacaoAtivoTO>(200);
		
		for(int i=0; i < 187; i++) //removendo as cotacoes anteriores a simulacao
			listaDasCotacoes.remove(0);
		
		novaLista.addAll(listaDasCotacoes);
		
//		for(int i=0; i < listaDasCotacoes2008.size(); i++){
		for(int i=0; i < 83; i++){
			CotacaoAtivoTO caTO = listaDasCotacoes2008.get(i);
			novaLista.add(caTO);
		}
		SimulacaoReinvestimento sr = new SimulacaoReinvestimento();
		sr.getResultado(novaLista, null);
		*/
	
		for(int i=2009; i <= 2011; i++){
			logger.info("ANO " + String.valueOf(i));
//			List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("BVMF3", String.valueOf(i));
			List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("PETR4", i);
			SimulacaoReinvestimentoCALLePUT sr = new SimulacaoReinvestimentoCALLePUT();
			sr.getResultado(listaDasCotacoes2009, null);			
		}

//		calculaGanho(30000, 2000, 72, 0.04);
		
	}
	
	public static void calculaGanho(double valorInicial, double quantiaPorUnidadeTempo, int tempo, double taxa){
		double caixa = valorInicial;
		double ganho = 0.0;
		for (int i=1; i <= tempo; i++){
			ganho=caixa * taxa;
			caixa=caixa + ganho + quantiaPorUnidadeTempo;
			System.out.println("caixa:" + caixa);
		}
			
		System.out.println("caixa:" + caixa);
	}

}
