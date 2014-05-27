package sata.domain.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;

//TODO TEM QUE RESOLVER ESSE
public class SimulacaoDetectaPontoAcao implements ISimulacao, IConstants{

	static Logger logger = Logger.getLogger(SimulacaoDetectaPontoAcao.class.getName());
	static int qtdAcertos = 0;
	
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {

		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws SQLException {
		
		// cria lista de cotacoes
//		List<CotacaoAtivoTO> cotacoes = new ArrayList<CotacaoAtivoTO>();
//		int indice = -1;
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("2200");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("2000");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1500");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1000");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1200");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1500");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1300");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1100");
//		cotacoes.add(new CotacaoAtivoTO());
//		cotacoes.get(++indice).setFechamento("1400");
//		for (CotacaoAtivoTO c : cotacoes) {
//			c.setSplit(1);
//		}
		
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		List<CotacaoAtivoTO> cotacoes = caDAO.getCotacoesDoAtivo("PETR4", 2007);
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2008));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2009));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2010));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2011));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2012));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2013));
		cotacoes.addAll(caDAO.getCotacoesDoAtivo("PETR4", 2014));
		
		SimulacaoDetectaPontoAcao s = new SimulacaoDetectaPontoAcao();
		int qtdMaxCandles = 10;
		int qtdMaxCandlesComeco = 20;

		for (int i = 0; i < cotacoes.size(); i++) {
			for (int j = i + 1; j < cotacoes.size(); j++){
				BigDecimal cotaI = cotacoes.get(i).getValorFechamento().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				BigDecimal cotaJ = cotacoes.get(j).getValorFechamento().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				// se a queda for maior do que 10%
				if (cotaI.subtract(cotaJ).divide(cotaI, RoundingMode.HALF_EVEN).compareTo(BigDecimal.valueOf(0.1)) == 1) {
					if ((j - i) < qtdMaxCandlesComeco) {
						int pontoAcao = s.getPontoAcao(i, j, 0.4, qtdMaxCandles, cotacoes, true);
						if (pontoAcao != -1) {
							System.out.println("pontoAcao: " + pontoAcao + " data: " + cotacoes.get(pontoAcao).getPeriodo());
						}
						qtdAcertos = 0;
					} else {
						break;
					}
				}
			}
		}
	}
	
	public int getPontoAcao(int ini, int fim, double retracao, int qtdCand, List<CotacaoAtivoTO> ca, boolean sobeOuDesce) {
		// se chegou a quantidade de pontos necessarios
		if (qtdAcertos == 2) { 
			return ini + 1; 
		}
		// calcula a retracao
		BigDecimal diferenca = ca.get(fim).getValorFechamento().subtract(ca.get(ini).getValorFechamento()).multiply(BigDecimal.valueOf(retracao));
		BigDecimal valorRetracao = ca.get(fim).getValorFechamento().subtract(diferenca);
		BigDecimal pivo = valorRetracao;
		BigDecimal max_limit = BigDecimal.ZERO;
		if (sobeOuDesce) {
			max_limit = ca.get(ini).getValorFechamento();
		}else{
			max_limit = ca.get(fim).getValorFechamento();
		}
		boolean acertou = false;
		int maiorOuMenor = (sobeOuDesce ? 1: -1);
		fim = (sobeOuDesce ? fim : ini);
		for (int i = fim + 1; i < fim + qtdCand + 1; i++) {
			if (ca.get(i).getValorFechamento().compareTo(pivo) == maiorOuMenor) {
				pivo = ca.get(i).getValorFechamento();
				acertou = true;
			}
			if (ca.get(i).getValorFechamento().compareTo(max_limit) == maiorOuMenor) {
				return -1;
			}
			if ((ca.get(i).getValorFechamento().compareTo(pivo) == maiorOuMenor*-1) && acertou) {
				qtdAcertos++;
				sobeOuDesce = (maiorOuMenor == 1 ? false : true);
				fim = (sobeOuDesce ? ini : fim);
				return getPontoAcao(i-1, fim, retracao, qtdCand, ca, sobeOuDesce);
			}
		}
		return -1;
	}

}
