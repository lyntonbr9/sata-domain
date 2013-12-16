package sata.main;

import java.sql.SQLException;
import java.util.List;

import sata.domain.alert.OperacaoDeAltaVarPoucoTempo;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.data.DataManagement;
import sata.domain.simulacao.ISimulacao;
import sata.domain.simulacao.SimulacaoAcaoOperacaoDeAlta;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.metastock.simulacao.SimulaGanhoOpcoes;

public class Principal {

	
	public static void simulaAcao(String codigoAcao, String ano) throws SQLException{
		System.out.println("testando Simulacao " + codigoAcao + " ano de " + ano);
		ICotacaoAtivoDAO cotacaoAtivoDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		List<CotacaoAtivoTO> listaCotacoesAtivo = cotacaoAtivoDAO.getCotacoesDoAtivo(codigoAcao, Integer.valueOf(ano));
		Object[] paramSimulacao = {10, 12, 0.5}; //{stopGain, stopLoss, probStopLoss}
		ISimulacao simulacao = new SimulacaoAcaoOperacaoDeAlta();
		//ISimulacao simulacao = new SimulacaoAcaoOperacaoDeBaixa();
		//ISimulacao simulacao = new SimulacaoAcaoOperacaoDeAltaBasico();
		
		ResultadoSimulacaoTO res = simulacao.getResultado(listaCotacoesAtivo, paramSimulacao);
		System.out.println("ACAO: " + codigoAcao);
		System.out.println("TOTAL OPERACOES: " + res.getQtdTotalOperacoes());
		System.out.println("OPERACOES RISCO: " + res.getQtdOperacoesRiscoStop());
		System.out.println("OPERACOES GANHO: " + res.getQtdOperacoesSucesso());
		System.out.println("OPERACOES PERDA: " + res.getQtdOperacoesFalha());
		System.out.println("VALOR TOTAL: " + res.getValorTotal());
		System.out.println("VALOR GANHO: " + res.getValorGanho());
		System.out.println("VALOR PERDA: " + res.getValorPerda());
		System.out.println("==================================================");
		System.out.println("");
	}
	
//	public static void insereAcaoDB(String codigoAcao, String ano){
//		System.out.println("inserindo acao " + codigoAcao + " ano de " + ano);
//		DataManagement dm = new DataManagement();
//		String pathArqListaDeCotacoesDaAcao = ano + "\\saida_" + codigoAcao + "_" + ano + ".txt";
//		dm.setArquivoListaCotacaoDeAtivos(pathArqListaDeCotacoesDaAcao);
//		dm.importarArqCotacaoToDB(codigoAcao, ano);
//	}
	
	public static void insereCotacaoAcaoHistoricoBovespaDB(String codigoAtivo, String ano){
		System.out.println("inserindo acao(opcao) " + codigoAtivo + " ano de " + ano);
		DataManagement dm = new DataManagement();
		dm.importarArqCotacaoHistoricaBovespaToDB(codigoAtivo, ano);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
//		String[] acoes = {"PETR4", "VALE5","OGXP3","BVMF3"};
		String[] acoes = {"PETR4"};
//		String[] acoes = {};
		//BVMF3 - Mais ou menos liguidez
		//"ITUB4","USIM5","GGBR4","CSNA3","BBAS3","BBDC4"
		for (int i=0; i < acoes.length; i++)
			for(int j = 2011; j < 2012; j++)
				insereCotacaoAcaoHistoricoBovespaDB(acoes[i], String.valueOf(j));
	
//		switch(Integer.parseInt(args[0])){
//			case 1:
//				//Vai alertar as acoes que tiveram alta variacao em pouco tempo
//				OperacaoDeAltaVarPoucoTempo.alerta();
//				break;
//			case 2:
//				//Vai alertar com a simulacao de ganho nas opcoes
//				SimulaGanhoOpcoes.SimulaGanhoEmOpcoes();
//				break;
//		}
		
//		String ano="2011";
//		String ano="2010";
//		String ano="2009";
		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
//		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String codigoAcao = i.next();
//			if (codigoAcao.equalsIgnoreCase("PETR4"))
//				simulaAcao(codigoAcao, ano);
//		}
		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
//		IAtivoDAO ativoDAO = daoFactory.getAtivoDAO();
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext())
//		{
//			String codigoAcao = i.next();
////			if (codigoAcao.equalsIgnoreCase("PETR4"))
////			{
//				ICotacaoAtivoDAO cotacaoAtivoDAO = daoFactory.getCotacaoAtivoDAO();
//				List<CotacaoAtivoTO> listaCotacoesAtivo = cotacaoAtivoDAO.getCotacoesDoAtivo(codigoAcao, ano);
//				List<CotacaoAtivoTO> listaParaAlerta = new ArrayList<CotacaoAtivoTO>();
////				//for(int j = 70; j < 74; j ++){
//				for(int j = listaCotacoesAtivo.size() - 4; j < listaCotacoesAtivo.size(); j ++){
//					CotacaoAtivoTO caTO = listaCotacoesAtivo.get(j);
//					listaParaAlerta.add(caTO);
////					if(j > 3){
////						OperacaoDeAlta oper = new OperacaoDeAlta();
////						System.out.println("Fazer operacao = " + oper.analisaFazerOperacao(listaParaAlerta, null) + " j= " + j);
////						listaParaAlerta.remove(0);
////					}
//				}
//
//				OperacaoDeAlta oper = new OperacaoDeAlta();
//				if(oper.analisaFazerOperacao(listaParaAlerta, null))
//					System.out.println("Ativo: " + codigoAcao + " \t Fazer operacao = " + oper.analisaFazerOperacao(listaParaAlerta, null) 
//							+ " data indicativo: " + listaParaAlerta.get(listaParaAlerta.size()-1).getPeriodo() + " SE OPERA NO PROXIMO DIA");
//				
////			}
//		}

//		
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoAtivoDAO ativoDAO = (ArquivoAtivoDAO) daoFactory.getAtivoDAO();
//		ativoDAO.setArquivoListaDeAtivos("listaDeAtivos_" + ano + ".txt");
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String codigoAcao = i.next();
//			simulaAcao(codigoAcao, ano);
//		}
		
		// TODO Auto-generated method stub
//		System.out.println("testando ArquivoDAO");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoAtivoDAO ativoDAO = (ArquivoAtivoDAO) daoFactory.getAtivoDAO();
//		ativoDAO.setArquivoListaDeAtivos("listaDeAtivos_2008.txt");
//		Iterator<String> i = ativoDAO.getCodigosAtivos().iterator();
//		while(i.hasNext()){
//			String ativo = i.next();
//			System.out.println("ATIVO: " + ativo);
//		}
		
//		System.out.println("testando CotacaoAtivoDAO");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoCotacaoAtivoDAO cotacaoAtivoDAO = (ArquivoCotacaoAtivoDAO) daoFactory.getCotacaoAtivoDAO();
//		cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_PETR4_2008.txt");
//		Iterator<CotacaoAtivoTO> i = cotacaoAtivoDAO.getCotacoesDosAtivos().iterator();
//		while(i.hasNext()){
//			CotacaoAtivoTO ativo = i.next();
//			System.out.println("ATIVO: " + ativo.getCodigo());
//			System.out.println("ABERTURA: " + ativo.getAbertura());
//			System.out.println("MAXIMA: " + ativo.getMaxima());
//			System.out.println("MINIMA: " + ativo.getMinima());
//			System.out.println("FECHAMENTO: " + ativo.getFechamento());
//		}
		
//		System.out.println("testando Simulacao");
//		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.ARQUIVO);
//		ArquivoCotacaoAtivoDAO cotacaoAtivoDAO = (ArquivoCotacaoAtivoDAO) daoFactory.getCotacaoAtivoDAO();
//		//cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_PETR4_2009.txt");
//		String codigoAcao = "BBDC4";
//		String ano="2009";
//		
//		cotacaoAtivoDAO.setArquivoListaCotacaoDeAtivos("saida_BBDC4_2009.txt");
//		List<CotacaoAtivoTO> listaCotacoesAtivo = cotacaoAtivoDAO.getCotacoesDosAtivos();
//		ISimulacao simulacao = new SimulacaoAcaoOperacaoAlta();
//		//simulacao.setQtdTotalOperacoesRiscoStop(25);
//		ResultadoSimulacaoTO res = simulacao.getResultado(listaCotacoesAtivo, 30, 10, 0.5);
//		System.out.println("ACAO: " + codigoAcao);
//		System.out.println("TOTAL OPERACOES: " + res.getQtdTotalOperacoes());
//		System.out.println("OPERACOES RISCO: " + res.getQtdOperacoesRiscoStop());
//		System.out.println("OPERACOES GANHO: " + res.getQtdOperacoesSucesso());
//		System.out.println("OPERACOES PERDA: " + res.getQtdOperacoesFalha());
//		System.out.println("VALOR TOTAL: " + res.getValorTotal());
//		System.out.println("VALOR GANHO: " + res.getValorGanho());
//		System.out.println("VALOR PERDA: " + res.getValorPerda());
		
//		while(i.hasNext()){
//			CotacaoAtivoTO ativo = i.next();
//			System.out.println("ATIVO: " + ativo.getCodigo());
//			System.out.println("ABERTURA: " + ativo.getAbertura());
//			System.out.println("MAXIMA: " + ativo.getMaxima());
//			System.out.println("MINIMA: " + ativo.getMinima());
//			System.out.println("FECHAMENTO: " + ativo.getFechamento());
//		}
	}
}
