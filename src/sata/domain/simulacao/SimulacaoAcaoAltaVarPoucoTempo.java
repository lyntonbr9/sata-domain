package sata.domain.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sata.domain.simulacao.to.IndicadoSimulacaoAltaVarPoucoTempoTO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class SimulacaoAcaoAltaVarPoucoTempo implements ISimulacao, IConstants{
	
	
	private List<IndicadoSimulacaoAltaVarPoucoTempoTO> chaves;

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		
//		System.out.println(listaDasCotacoes.size());
		
		double[] valor = getDiasDeVariacao(listaDasCotacoes);
		chaves = marcaDiasChave(valor);
		// adiciona as indicacoes de saida de alta
		chaves.addAll(marcaChavesAlta(listaDasCotacoes));
		
		ArrayList<Integer> dias = calculaNumeroDiasAteRetorno(chaves,listaDasCotacoes,valor);
		
	/*	for(int i=0;i<listaDasCotacoes.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getFechamento());
		}
	*/	
		for(int i=0;i<chaves.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			CotacaoAtivoTO cotacao = listaDasCotacoes.get(chaves.get(i).getIndice());
			System.out.println("Dia de variacao: " + cotacao.getPeriodo() 
							+ " Num dias ate o retorno: " + dias.get(i));
		}
				
		return null;
	}

	public List<Integer> getIndicesIndicadosSimulacao(){
		List<Integer> indices = new ArrayList<Integer>();
		for(IndicadoSimulacaoAltaVarPoucoTempoTO indicado : chaves) {
			indices.add(indicado.getIndice());
		}
		return indices;
	}
	
	public IndicadoSimulacaoAltaVarPoucoTempoTO getIndicadoPorIndice(int indice) {
		for(IndicadoSimulacaoAltaVarPoucoTempoTO indicado : chaves) {
			if (indicado.getIndice() == indice) {
				return indicado;
			}
		}
		return null;
	}
	
	public List<IndicadoSimulacaoAltaVarPoucoTempoTO> getIndicadosSimulacao(){
		return chaves;
	}
		
	public double[] getDiasDeVariacao(List<CotacaoAtivoTO> listaDasCotacoes){
		
		
		double[] prc = new double[listaDasCotacoes.size()];
		
		for(int i=2;i<listaDasCotacoes.size();i++){
			BigDecimal cot1 = new BigDecimal(Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(i-2)).getFechamento()));
			BigDecimal cot2 = new BigDecimal(Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getFechamento()));
			BigDecimal prcVariacao;
			prcVariacao = cot2.subtract(cot1).divide(cot1, 6, BigDecimal.ROUND_HALF_EVEN);
			prc[i] = prcVariacao.doubleValue();
//			System.out.println(prc[i]);
		}
		
		return prc;
	}
	
	public List<IndicadoSimulacaoAltaVarPoucoTempoTO> marcaDiasChave(double[] var){
				
		int alta = 0;
		int baixa = 0;
		List<IndicadoSimulacaoAltaVarPoucoTempoTO> chaves = new ArrayList<IndicadoSimulacaoAltaVarPoucoTempoTO>();
		Properties SATAProps = SATAPropertyLoader.loadProperties(ARQ_SATA_CONF);
		double pctgemVariacao = Double.parseDouble(SATAProps.getProperty(PROP_PCTGEM_VARIACAOALTAPOUCOTEMPO));
		
		for(int i=2;i<var.length;i++){
			if(var[i] >= pctgemVariacao){
				alta++;
				
				System.out.println("indice do dia variacao de Alta: " + i );
				//TODO: Perguntar pro flavio pq deste if
				if(baixa>=1){
					IndicadoSimulacaoAltaVarPoucoTempoTO chave = new IndicadoSimulacaoAltaVarPoucoTempoTO();
					chave.setIndice(i-2);
					chave.setAlta(true);
					chaves.add(chave);
				}
				baixa=0;
			}else if (var[i] <= ((-1)*pctgemVariacao)){
				baixa++;
				System.out.println("indice do dia variacao de Baixa: " + i );
				//TODO: Perguntar pro flavio pq deste if
				if(alta>=2){
//					IndicadoSimulacaoAltaVarPoucoTempoTO chave = new IndicadoSimulacaoAltaVarPoucoTempoTO();
//					chave.setIndice(i-2);
//					chave.setBaixa(true);
//					chaves.add(chave);
				}
				alta=0;
			}
		}
		
		return chaves;
	}
	
	public List<IndicadoSimulacaoAltaVarPoucoTempoTO> marcaChavesAlta(List<CotacaoAtivoTO> listaDasCotacoes){
		
		List<IndicadoSimulacaoAltaVarPoucoTempoTO> chavesAlta = new ArrayList<IndicadoSimulacaoAltaVarPoucoTempoTO>();
		
		// PARA TODAS AS CHAVES INDICADAS
		for(int i=0; i < chaves.size();i++){
			int indiceIndicado = chaves.get(i).getIndice();
			// PRIMEIRA TENTATIVA DE ENTRADA NA OPERACAO PQ PODE NÃO CHEGAR A ENTRAR NA OPERAÇÃO
			int entradaAlta = indiceIndicado + 3;
			if (chaves.get(i).isAlta()) {
				if (entradaAlta < listaDasCotacoes.size()) {
					for (int j = entradaAlta; j < entradaAlta + 5; j++) {
						// SE FOR O DIA DE ENTRADA E 
						// SE O VALOR DA ABERTURA FOR ABAIXO DA MINIMA DO DIA ANTERIOR ENTAO NAO ENTRA NA OPERACAO
						// OU SE O FECHAMENTO FOR MENOR DO QUE A ABERTURA DO DIA (CANDLE VERMELHO) E A MINIMA FOR MENOR
						// DO QUE A METADE DO CORPO DO DIA ANTERIOR ENTAO SAI NO FECHAMENTO
						// OU SE O FECHAMENTO FOR MAIOR DO QUE A ABERTURA DO DIA (CANDLE VERDE) E NAO SUBIR 
						// MAIS DO QUE 1 POR CENTO ENTAO SAI NO FECHAMENTO
						if (j == entradaAlta && 
								(listaDasCotacoes.get(j).getValorAbertura().compareTo(listaDasCotacoes.get(j-1).getValorMinima()) == -1 
								|| (listaDasCotacoes.get(j).getValorFechamento().compareTo(listaDasCotacoes.get(j).getValorAbertura()) == -1
								&& listaDasCotacoes.get(j).getValorMinima().compareTo(metadeCorpo(listaDasCotacoes.get(j-1))) == -1)
								|| (listaDasCotacoes.get(j).getValorFechamento().compareTo(listaDasCotacoes.get(j).getValorAbertura()) > -1
								&& variacaoDia(listaDasCotacoes.get(j)).compareTo(BigDecimal.valueOf(0.01)) == -1)
								)) {
							// INDICA QUE NAO VAI ENTRAR NA OPERACAO OU SAI NO MESMO DIA
							IndicadoSimulacaoAltaVarPoucoTempoTO indicadoNaoEntrarAlta = new IndicadoSimulacaoAltaVarPoucoTempoTO();
							indicadoNaoEntrarAlta.setIndice(j);
							indicadoNaoEntrarAlta.setNaoEntrarAlta(true);
							chavesAlta.add(indicadoNaoEntrarAlta);
							break;
						} else if(j == entradaAlta) {
							// ADICIONA O DIA DE TENTATIVA DE ENTRADA
							IndicadoSimulacaoAltaVarPoucoTempoTO pivo = new IndicadoSimulacaoAltaVarPoucoTempoTO();
							pivo.setIndice(entradaAlta);
							pivo.setEntradaAlta(true);
							chavesAlta.add(pivo);
						}
						// SE NAO FOR O DIA DE ENTRADA E O FECHAMENTO FOR MENOR DO QUE A ABERTURA NO DIA ENTAO SAI DA OPERACAO NO FECHAMENTO
						if (j > entradaAlta && listaDasCotacoes.get(j).getValorFechamento().compareTo(listaDasCotacoes.get(j).getValorAbertura()) == -1) {
							// SAI DA OPERACAO
							IndicadoSimulacaoAltaVarPoucoTempoTO indicadoSaidaAlta = new IndicadoSimulacaoAltaVarPoucoTempoTO();
							indicadoSaidaAlta.setIndice(j);
							indicadoSaidaAlta.setSaidaAlta(true);
							chavesAlta.add(indicadoSaidaAlta);
							break;
						}
					}
				}
			}
		}
		
		return chavesAlta;
	}
	
	public BigDecimal metadeCorpo(CotacaoAtivoTO cotacao) {
		return cotacao.getValorMinima().add(cotacao.getValorMaxima().subtract(cotacao.getValorMinima()).divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN));
	}
	
	public BigDecimal variacaoDia(CotacaoAtivoTO cotacao) {
		return cotacao.getValorFechamento().subtract(cotacao.getValorAbertura()).divide(cotacao.getValorAbertura(), RoundingMode.HALF_EVEN);
	}
	
	public ArrayList<Integer> calculaNumeroDiasAteRetorno(List<IndicadoSimulacaoAltaVarPoucoTempoTO> chaves, List<CotacaoAtivoTO> listaDasCotacoes,double[] valor){
		
		
		ArrayList<Integer> nDias = new ArrayList<Integer>();
		
		for(int i=0;i<chaves.size();i++){
		
			int contador = 0;
			
			int index = chaves.get(i).getIndice();
			double valorBase =Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index)).getFechamento());
			double valorVariacao = valor[index+2];
			
			
			boolean subida =true;
			if(valorVariacao < 0){
				subida = false;
				
			}
			
			boolean fim =false;
			int j = 2;
			while(!fim){
				
				if(subida && (index + j) < listaDasCotacoes.size() && Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index + j)).getFechamento())>=valorBase){
					contador++;
					j++;
				}else if(subida){					
						fim = true;
					
				}
				if(!subida && (index + j) < listaDasCotacoes.size() && Integer.parseInt(((CotacaoAtivoTO)listaDasCotacoes.get(index + j)).getFechamento())<=valorBase){
					contador++;
					j++;
				}else if(!subida){					
						fim = true;
					
				}
				
				
			}
			nDias.add(new Integer(contador));
		}
		
		return nDias;
	}
	
//	public static void main(String[] args) {
//		SimulacaoAcaoAltaVarPoucoTempo s = new SimulacaoAcaoAltaVarPoucoTempo();
//		DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
//		ICotacaoAtivoDAO caDAO = factory.getCotacaoAtivoDAO();
//		List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo("USIM5", "2011");
//		
//		
//		s.getResultado(listaDasCotacoes, null);
//	}

}
