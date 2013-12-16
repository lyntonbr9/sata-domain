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
public class SimulacaoReinvestimentoCALLePUT_DoisMeses implements ISimulacao, IConstants{

	static Logger logger = Logger.getLogger(SimulacaoReinvestimentoCALLePUT_DoisMeses.class.getName());
	
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		int QTD_DIAS_OPCAO = 20;
		int tamanhoArray = listaDasCotacoes.size()/QTD_DIAS_OPCAO + 1; //Tamanho para guardar as operacoes
		double variacaoAcao[] = new double[tamanhoArray];
		double totalGanhoNaAcao = 0.0;
		
		double ganhoCALL_OTM[] = new double[tamanhoArray];
		double totalGanhoCALL_OTM = 0.0;
				
		double gastoPUT_ATM[] = new double[tamanhoArray];
		double totalGastoPUT_ATM = 0.0;

		double totalCaixa = 0.0;
		int indiceOperacao = 0;
		
		double fechamentoAnterior = 0.0;
		double fechamentoCorrente = 0.0;
		double fechamentoAcaoNaCALL_OTM = 0.0;
		double fechamentoAcaoNaPUT_ATM = 0.0;
		
		//Calcula a volatilidade do ano anterior
		double volatilidade = 0.0;
//		double volatilidade = (Double) parametros[0];
//		System.out.println("VOLATILIDADE UTILIZADA: " + volatilidade);
		
		double qtdDiasFaltaUmMesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);
//		double qtdDiasFaltaUmMesVencEmAnos = BlackScholes.getQtdDiasEmAnos(5);
		double qtdDiasFaltaDoisMesesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_2_MES_VENC);
//		double qtdDiasFaltaDoisMesesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);
	
		int QTD_LOTES = 1;
		int j = 0; //indice para as CALL e PUT
		for(int i = 0; i <listaDasCotacoes.size(); i++)
		{
			if (i + QTD_DIAS_OPCAO -1 >= listaDasCotacoes.size()) 
				break; //se depois no futuro ultrapassar o tamanho da lista termina a simulacao
			
			CotacaoAtivoTO caTOAnterior = listaDasCotacoes.get(i); //pega a cotacao anterior
			fechamentoAnterior = Double.parseDouble(caTOAnterior.getFechamento());
			logger.info(caTOAnterior.getCodigo() + ": " + i + " " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
			i = i + QTD_DIAS_OPCAO -1; //vai para o vencimento da opcao (a opcao dura 20 dias uteis) diminui 1 pq a lista comeca com indice 0
			
			CotacaoAtivoTO caTOCorrente = listaDasCotacoes.get(i); //pega a cotacao corrente no vencimento da opcao
			fechamentoCorrente = Double.parseDouble(caTOCorrente.getFechamento());
			logger.info(caTOCorrente.getCodigo() + ": " + i + " " + caTOCorrente.getPeriodo() + " - F: " + fechamentoCorrente);

			//calcula a variacao da acao
			logger.info("================= Variacao da Acao =================");
			variacaoAcao[indiceOperacao] = fechamentoCorrente - fechamentoAnterior; //pega a variacao da acao
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			if(variacaoAcao[indiceOperacao] > 0)
			{
				totalGanhoNaAcao+=variacaoAcao[indiceOperacao];
				totalCaixa+=variacaoAcao[indiceOperacao]; //atualiza o caixa caso a ação tenha subido
			}

			//calcula o ganho no lancamento da call 2 OTM
			logger.info("================= Opcao CALL 2 OTM =================");
			
			logger.info("=== CALL OTM Faltando 2 Meses ===");
			CotacaoAtivoTO caTONaOTM = listaDasCotacoes.get(j); //pega a cotacao da acao na OTM quando for comprar
			fechamentoAcaoNaCALL_OTM = Double.parseDouble(caTONaOTM.getFechamento());
			double precoExercOpcaoCALL_OTM = BlackScholes.getPrecoExercicio(true, fechamentoAcaoNaCALL_OTM, 2); // Pega a CALL 2 OTM
			logger.info("precoExercOpcaoCALL_OTM: " + precoExercOpcaoCALL_OTM);
			
			volatilidade = caTONaOTM.getVolatilidadeAnual();
			logger.info("VOLATILIDADE UTILIZADA (CALL 2 OTM): " + volatilidade);
			double valorOpcaoDoisMesesCALL_OTM = BlackScholes.blackScholes(true, fechamentoAcaoNaCALL_OTM, precoExercOpcaoCALL_OTM, qtdDiasFaltaDoisMesesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoDoisMesesCALL_OTM: " + valorOpcaoDoisMesesCALL_OTM);
			double ganhoCallDoisMesesNaOTM = BlackScholes.getVE(true, fechamentoAcaoNaCALL_OTM, precoExercOpcaoCALL_OTM, valorOpcaoDoisMesesCALL_OTM); //perde o VE no vencimento
			logger.info("ganhoCallDoisMesesNaOTM: " + ganhoCallDoisMesesNaOTM);
			
			logger.info("=== CALL OTM Faltando 1 Mes ===");
			volatilidade = caTOCorrente.getVolatilidadeAnual();
			logger.info("VOLATILIDADE UTILIZADA (CALL OTM Faltando 1 Mes): " + volatilidade);
			logger.info("fechamentoAcaoParaCalculoUmMesCALL_OTM: " + fechamentoCorrente);
			double valorOpcaoUmMesRestanteCALL_OTM = BlackScholes.blackScholes(true, fechamentoCorrente, precoExercOpcaoCALL_OTM, qtdDiasFaltaUmMesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoUmMesRestanteCALL_OTM: " + valorOpcaoUmMesRestanteCALL_OTM);
			double custoCallUmMesRestanteNaOTM = BlackScholes.getVE(true, fechamentoCorrente, precoExercOpcaoCALL_OTM, valorOpcaoUmMesRestanteCALL_OTM); //perde o VE no vencimento
			logger.info("custoCallUmMesRestanteNaOTM: " + custoCallUmMesRestanteNaOTM);
			
			logger.info("=== CALL OTM Resultado ===");
			double ganhoCallNaOTM = ganhoCallDoisMesesNaOTM - custoCallUmMesRestanteNaOTM; //perde o VE no vencimento
			logger.info("ganhoCallNaOTM: ganhoCallDoisMesesNaOTM - custoCallUmMesRestanteNaOTM=" + ganhoCallNaOTM);
			logger.info("Porcentagem ganhoCallNaOTM: " + (ganhoCallNaOTM*100)/fechamentoAcaoNaCALL_OTM + "%");
			ganhoCALL_OTM[indiceOperacao] = ganhoCallNaOTM;
			totalGanhoCALL_OTM+=ganhoCallNaOTM;
			totalCaixa+=ganhoCallNaOTM; //atualiza o caixa
			
			//calcula o gasto na compra da PUT ATM
			logger.info("================= Opcao PUT ATM =================");
			
			logger.info("=== PUT ATM Faltando 1 Mes ===");
			CotacaoAtivoTO caTOPUTNaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			volatilidade = caTOPUTNaATM.getVolatilidadeAnual();
			logger.info("VOLATILIDADE UTILIZADA (PUT ATM Faltando 1 Mes): " + volatilidade);
			fechamentoAcaoNaPUT_ATM = Double.parseDouble(caTOPUTNaATM.getFechamento());
			double precoExercOpcaoPUT_ATM = BlackScholes.getPrecoExercicio(false, fechamentoAcaoNaPUT_ATM, 0); // Pega a PUT ATM
			logger.info("precoExercOpcaoPUT_ATM: " + precoExercOpcaoPUT_ATM);
			logger.info("fechamentoAcaoParaCalculoUmMesPUT_ATM: " + fechamentoAcaoNaPUT_ATM);
			double valorOpcaoUmMesRestantePUT_ATM = BlackScholes.blackScholes(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, qtdDiasFaltaUmMesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoUmMesRestantePUT_ATM: " + valorOpcaoUmMesRestantePUT_ATM);
			double custoPUTUmMesRestanteNaATM = BlackScholes.getVE(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, valorOpcaoUmMesRestantePUT_ATM); //perde o VE no vencimento
			logger.info("custoPUTUmMesRestanteNaATM: " + custoPUTUmMesRestanteNaATM);
			

			logger.info("=== PUT ATM Resultado ===");
			double gastoPUTNaATM = custoPUTUmMesRestanteNaATM; //perde o VE no vencimento
			logger.info("gastoPUTNaATM: " + gastoPUTNaATM);
			logger.info("Porcentagem gastoPUTNaATM: " + (gastoPUTNaATM*100)/fechamentoAcaoNaPUT_ATM + "%");
			gastoPUT_ATM[indiceOperacao] = gastoPUTNaATM;
			totalGastoPUT_ATM+= gastoPUTNaATM;
			totalCaixa-=gastoPUTNaATM; //atualiza o caixa
			
			//calcula o resultado do mes
			logger.info("================= RESULTADO NO MES =================");
			double ganhoNaAcao = (variacaoAcao[indiceOperacao] > 0 ? variacaoAcao[indiceOperacao] : 0.0);
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			logger.info("ganhoNaAcao=" + ganhoNaAcao);
			logger.info("ganhoCALL_OTM[" + indiceOperacao + "]=" + ganhoCALL_OTM[indiceOperacao]);
			logger.info("gastoPUT_ATM[" + indiceOperacao + "]=" + gastoPUT_ATM[indiceOperacao]);
			
			double resultadoDoMes = ganhoNaAcao + ganhoCALL_OTM[indiceOperacao] - gastoPUT_ATM[indiceOperacao];
			logger.info("resultadoDoMes:  ganhoNaAcao + ganhoCALL_OTM - gastoPUT_ATM");
			logger.info("resultadoDoMes: " + ganhoNaAcao + " + " + ganhoCALL_OTM[indiceOperacao] 
					 + " - " + gastoPUT_ATM[indiceOperacao] + " = " + resultadoDoMes);
			logger.info("totalCaixa[" + indiceOperacao + "]=" + totalCaixa);
			
			logger.info(" ");
			logger.info(" ");
			
			j = j + QTD_DIAS_OPCAO - 1; //vai para a proxima ATM
			indiceOperacao++; //parte para a proxima operacao
			i--; //deve-se subtrair 1 porque vai ser incrementado depois no LACO DO FOR

		}
		
		//imprime os valores encontrados
		logger.info("================= RESULTADO FINAL NO ANO =================");
		//calcula o valor total ganho NA ACAO
		double primeiraCotacaoAcao = Double.parseDouble(listaDasCotacoes.get(0).getFechamento());
		logger.info("GANHOS COM A ACAO APENAS:");
		logger.info("primeiraCotacaoAcao=" + primeiraCotacaoAcao);
		logger.info("ultimaCotacaoAcao=" + fechamentoCorrente);
		double totalGanhoAcaoApenas = (fechamentoCorrente - primeiraCotacaoAcao) * QTD_LOTES;
		logger.info("totalGanhoAcaoApenas=" + totalGanhoAcaoApenas);
		double pctGanhoAcao=(totalGanhoAcaoApenas*100)/(primeiraCotacaoAcao * QTD_LOTES);
		logger.info("pctGanhoAcao=" + pctGanhoAcao + "%");
		logger.info(" ");
		
		//calcula o valor total ganho NAS OPCOES
		logger.info("GANHOS COM A SIMULACAO DE REINVESTIMENTO:");
		logger.info("totalCaixa=" + totalCaixa);
		logger.info("totalGanhoNaAcao=" + totalGanhoNaAcao);
		logger.info("totalGanhoCALL_OTM=" + totalGanhoCALL_OTM);
		logger.info("totalGastoPUT_ATM=" + totalGastoPUT_ATM);
		
		double totalFinalGanhoSimulacao = primeiraCotacaoAcao + totalCaixa;
		logger.info("totalFinalGanhoSimulacao=primeiraCotacaoAcao + totalCaixa=" + totalFinalGanhoSimulacao);
		
		//calcula a porcentagem em relacao ao comeco do investimento
		double pctGanhoFinalGanhoSimulacao = (totalCaixa*100)/(primeiraCotacaoAcao*QTD_LOTES);
		logger.info("pctGanhoFinalGanhoSimulacao=" + pctGanhoFinalGanhoSimulacao + "%");
		
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
		
		PropertyConfigurator.configure("log4j.properties");
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		String acao = "PETR4";
//		String acao = "OGXP3";
		for(int ano=2000; ano <= 2011; ano++)
		{
			logger.info("ANO " + String.valueOf(ano));
			double volatilidade = 0;
//			List<CotacaoAtivoTO> listaDasCotacoesAnoPassado = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano));
//			List<CotacaoAtivoTO> listaDasCotacoesAnoRetrasado = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano-1));
//			if (acao.equalsIgnoreCase("PETR4") && (ano == 2009 || ano == 2008)) //SE FOR PETR4 em 2009
//				volatilidade = 0.3;
//			else
//				volatilidade = CalculoUtil.getVolatilidadeAnualAcao(listaDasCotacoesAnoPassado, listaDasCotacoesAnoRetrasado);
			
//			volatilidade = 0.49;
//			List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano));
			List<CotacaoAtivoTO> listaDasCotacoes = CalculoUtil.calculaVolatilidade(acao, String.valueOf(ano));
			SimulacaoReinvestimentoCALLePUT_DoisMeses sr = new SimulacaoReinvestimentoCALLePUT_DoisMeses();
			sr.getResultado(listaDasCotacoes, new Object[] {volatilidade});			
		}

//		calculaGanho(30000, 2000, 72, 0.04);
		
	}
	
	public static void calculaGanho(double valorInicial, double quantiaPorUnidadeTempo, int tempo, double taxa){
		double caixa = valorInicial;
		double ganho = 0.0;
		for (int i=1; i <= tempo; i++){
			ganho=caixa * taxa;
			caixa=caixa + ganho + quantiaPorUnidadeTempo;
			logger.info("caixa:" + caixa);
		}
			
		logger.info("caixa:" + caixa);
	}

}
