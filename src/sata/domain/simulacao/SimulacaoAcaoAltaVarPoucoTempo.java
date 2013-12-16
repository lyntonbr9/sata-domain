package sata.domain.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class SimulacaoAcaoAltaVarPoucoTempo implements ISimulacao, IConstants{
	
	
	private ArrayList<Integer> chaves;

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
		
		ArrayList<Integer> dias = calculaNumeroDiasAteRetorno(chaves,listaDasCotacoes,valor);
		
	/*	for(int i=0;i<listaDasCotacoes.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getFechamento());
		}
	*/	
		for(int i=0;i<chaves.size();i++){
			
			//System.out.println(((CotacaoAtivoTO)listaDasCotacoes.get(i)).getCodigo());
			CotacaoAtivoTO cotacao = listaDasCotacoes.get(((Integer)chaves.get(i)).intValue());
			System.out.println("Dia de variacao: " + cotacao.getPeriodo() 
							+ " Num dias ate o retorno: " + dias.get(i));
		}
				
		return null;
	}

	public ArrayList<Integer> getIndicesIndicadosSimulacao(){
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
	
	public ArrayList<Integer> marcaDiasChave(double[] var){
				
		int alta = 0;
		int baixa = 0;
		ArrayList<Integer> chaves = new ArrayList<Integer>();
		Properties SATAProps = SATAPropertyLoader.loadProperties(ARQ_SATA_CONF);
		double pctgemVariacao = Double.parseDouble(SATAProps.getProperty(PROP_PCTGEM_VARIACAOALTAPOUCOTEMPO));
		
		for(int i=2;i<var.length;i++){
			if(var[i] >= pctgemVariacao){
				alta++;
				
				System.out.println("indice do dia variacao de Alta: " + i );
				//TODO: Perguntar pro flavio pq deste if
				if(baixa>=2){
					chaves.add(new Integer(i-2));
				}
				baixa =0 ;
			}else if (var[i] <= ((-1)*pctgemVariacao)){
				baixa++;
				System.out.println("indice do dia variacao de Baixa: " + i );
				//TODO: Perguntar pro flavio pq deste if
				if(alta>=2){
					chaves.add(new Integer(i-2));
				}
				alta=0;
			}
		}
		
		return chaves;
	}
	
	public ArrayList<Integer> calculaNumeroDiasAteRetorno(ArrayList<Integer> chaves, List<CotacaoAtivoTO> listaDasCotacoes,double[] valor){
		
		
		ArrayList<Integer> nDias = new ArrayList<Integer>();
		
		for(int i=0;i<chaves.size();i++){
		
			int contador = 0;
			
			int index = ((Integer)chaves.get(i)).intValue();
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
