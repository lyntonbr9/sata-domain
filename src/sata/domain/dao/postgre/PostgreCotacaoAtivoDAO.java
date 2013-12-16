package sata.domain.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;
import sata.domain.util.SATAUtil;

public class PostgreCotacaoAtivoDAO implements ICotacaoAtivoDAO, IConstants {

	private Connection con;
	
	public PostgreCotacaoAtivoDAO(Connection postgreConnection){
		this.con = postgreConnection;
	}

	@Deprecated
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = '" + codigoAtivo + "' "
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}
	
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, Integer ano) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = ? AND ano = ? " 
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setString(2, ano.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setTipoPeriodo(rs.getString("tipoperiodo"));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				caTO.setVolume(rs.getString("volume"));
				caTO.setVolatilidadeAnual(Double.valueOf(rs.getString("volatilidadeAnual")));
				caTO.setVolatilidadeMensal(Double.valueOf(rs.getString("volatilidadeMensal")));
				caTO.setSplit(getSplit(caTO));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}
	
	public boolean possuiCotacaoNoAno(String codigoAtivo, Integer ano) throws SQLException {
		String sqlStmt = "SELECT COUNT(*) AS \"qtd\" FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = ? AND ano = ? ";
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setString(2, ano.toString());
			ResultSet rs = ps.executeQuery();
			PostgreDAOFactory.returnConnection(con);
		if (rs.next())
			return rs.getInt("qtd") > 0;
		else return false;
	}
	
	public int getSplit(CotacaoAtivoTO caTO){
		
		int resultado = 1; //caso nao consiga retornar o split
		
		//Formata o periodo para ser utilizado no Banco
		String periodoFormatadoParaBD = SATAUtil.getDataFormatadaParaBD(caTO.getPeriodo());
		
		//EXP(SUM(LOG(split))) eh igual a uma funcao MULTIPLY(split)
		//que multiplicaria todos os valores da coluna
//		String sqlStmt = "SELECT EXP(SUM(LOG(split))) AS ProdutoDosSplits FROM \"SplitAtivo\" "
//			+ " WHERE \"codigoAtivo\" = ? AND periodo >= ? GROUP BY \"codigoAtivo\" ";
		String sqlStmt = "SELECT split FROM \"SplitAtivo\" "
			+ " WHERE \"codigoAtivo\" = ? AND periodo >= ? ";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, caTO.getCodigo());
			ps.setTimestamp(2, SATAUtil.getTimeStampPeriodoCotacao(periodoFormatadoParaBD));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				resultado *= rs.getInt("split");
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		System.out.println("periodoFormatadoParaBD= " + periodoFormatadoParaBD + " resultado split: " + resultado);
		return resultado; 
		
	}
	
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String dataInicial, 
													String dataFinal) {
		
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		
		// TODO Auto-generated method stub
		String tabela = "CotacaoAtivo"; //tabela das acoes
		if (codigoAtivo.length() != 5)
			tabela = "CotacaoOpcao"; //tabela das opções
		
		String sqlStmt = "SELECT * FROM \"" + tabela + "\"" 
			+ " WHERE \"codigoAtivo\" = ? AND periodo BETWEEN ? AND ? ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setTimestamp(2, SATAUtil.getTimeStampPeriodoCotacao(dataInicial));
			ps.setTimestamp(3, SATAUtil.getTimeStampPeriodoCotacao(dataFinal));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setTipoPeriodo(rs.getString("tipoperiodo"));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				caTO.setVolume(rs.getString("volume"));
				caTO.setVolatilidadeAnual(Double.valueOf(rs.getString("volatilidadeAnual")));
				caTO.setVolatilidadeMensal(Double.valueOf(rs.getString("volatilidadeMensal")));
				caTO.setSplit(getSplit(caTO));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}

	//TODO: Colocar a volatilidade anual e mensal
	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO) {
		
		String tabela = "CotacaoAtivo"; //tabela das acoes
		if (caTO.getCodigo().length() != 5)
			tabela = "CotacaoOpcao"; //tabela das opções
		
		String sqlStmt = "INSERT INTO \"" + tabela + "\"" 
			+ "(\"codigoAtivo\", periodo, tipoperiodo, abertura, maxima, minima, fechamento, ano, volume) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1,caTO.getCodigo());
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(caTO.getPeriodo()));
			ps.setString(3,caTO.getTipoPeriodo());
			ps.setString(4,caTO.getAbertura());
			ps.setString(5,caTO.getMaxima());
			ps.setString(6,caTO.getMinima());
			ps.setString(7,caTO.getFechamento());
			ps.setString(8,caTO.getAno());
			ps.setString(9, caTO.getVolume());
			ps.executeUpdate();
			
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean existeCotacao(String codigoAtivo, String periodo){
		
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? AND periodo = ? ";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(periodo));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //se existe
			{
				PostgreDAOFactory.returnConnection(con);
				return true;
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public String getDataUltimoCadastro(String codigoAtivo) {
		
		String dataUltimoCadastro = "";
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? ORDER BY periodo DESC";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //pega o primeiro registro
			{
				dataUltimoCadastro = SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"), false);
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataUltimoCadastro;
	}

	@Override
	public int updateCotacaoDoAtivo(CotacaoAtivoTO caTO) {
		
		String sqlStmt = "UPDATE \"CotacaoAtivo\" SET " +
			" tipoperiodo=?, abertura=?, maxima=?, minima=?, fechamento=?, ano=?, volume=?, volatilidadeAnual=?, volatilidadeMensal=? " +
			" WHERE \"codigoAtivo\"=? AND periodo=?";
		
		int numeroLinhasAtualizadas = 0;
		String periodoFormatadoParaBD = SATAUtil.getDataFormatadaParaBD(caTO.getPeriodo());
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, caTO.getTipoPeriodo());
			ps.setString(2, caTO.getAbertura());
			ps.setString(3, caTO.getMaxima());
			ps.setString(4, caTO.getMinima());
			ps.setString(5, caTO.getFechamento());
			ps.setString(6, caTO.getAno());
			ps.setString(7, caTO.getVolume());
			ps.setString(8, String.valueOf(caTO.getVolatilidadeAnual()));
			ps.setString(9, String.valueOf(caTO.getVolatilidadeMensal()));
			ps.setString(10, caTO.getCodigo());
			ps.setTimestamp(11, SATAUtil.getTimeStampPeriodoCotacao(periodoFormatadoParaBD));
			numeroLinhasAtualizadas = ps.executeUpdate();
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return numeroLinhasAtualizadas;
	}	
	
	public static void main(String[] args) {
		
		//PETR4 teve split dividido por 2 em 25/04/2008
//		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		//atualizacao da cotacao na PETR4 por causa do split
//		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", "20060101", "20080425");
//		for(CotacaoAtivoTO caTO : listaCotacoesAcao)
//		{
//			int aberturaAtualizado = Integer.valueOf(caTO.getAbertura()) / 2;
//			int fechamentoAtualizado = Integer.valueOf(caTO.getFechamento()) / 2;
//			int maximaAtualizado = Integer.valueOf(caTO.getMaxima()) / 2;
//			int minimaAtualizado = Integer.valueOf(caTO.getMinima()) / 2;
//			System.out.println(caTO.getCodigo() + ": " + " F: " + caTO.getFechamento() + " Data: " + caTO.getPeriodo() + " novaCotacao: " + fechamentoAtualizado);
//			caTO.setAbertura(String.valueOf(aberturaAtualizado));
//			caTO.setFechamento(String.valueOf(fechamentoAtualizado));
//			caTO.setMaxima(String.valueOf(maximaAtualizado));
//			caTO.setMinima(String.valueOf(minimaAtualizado));
//			caDAO.updateCotacaoDoAtivo(caTO);
//		}
		
		
//		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", SATAUtil.getDataFormatadaParaBD("21/06/2011"), SATAUtil.getDataFormatadaParaBD("18/07/2011"));
		
//		String[] datasInicioSerie = {"03/01/2011","18/01/2011","18/02/2011","22/03/2011","19/04/2011","17/05/2011","21/06/2011","19/07/2011"};
//		String[] datasFimSerie = {"17/01/2011","17/02/2011","21/03/2011","18/04/2011","16/05/2011","20/06/2011","18/07/2011","15/08/2011"};
		
//		String[] datasInicioSerie = {"02/01/2009","20/01/2009","17/02/2009","17/03/2009","22/04/2009","19/05/2009","16/06/2009","21/07/2009","18/08/2009","22/09/2009","20/10/2009","17/11/2009"};
//		String[] datasFimSerie = {"19/01/2009","16/02/2009","16/03/2009","20/04/2009","18/05/2009","15/06/2009","20/07/2009","17/08/2009","21/09/2009","19/10/2009","16/11/2009","21/12/2009"};

//		String[] datasInicioSerie = {"02/01/2007","16/01/2007","13/02/2007","20/03/2007","17/04/2007","19/05/2007","19/06/2007","17/07/2007","20/08/2007","18/09/2007","16/10/2007","21/11/2007"};
//		String[] datasFimSerie = {"15/01/2007","12/02/2007","19/03/2007","16/04/2007","21/05/2007","18/06/2007","16/07/2007","17/08/2007","17/09/2007","15/10/2007","19/11/2007","17/12/2007"};
//
////		String dataInicioSerie = "";
////		String dataFimSerie = "";
//		
//		String resultadoOperacao = "";
//		
//		for (int i = 0; i < datasFimSerie.length; i++)
//		{
//			List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", SATAUtil.getDataFormatadaParaBD(datasInicioSerie[i]), SATAUtil.getDataFormatadaParaBD(datasFimSerie[i]));
//
//			for(CotacaoAtivoTO cotacaoAcaoTO : listaCotacoesAcao){
//				System.out.println(cotacaoAcaoTO.getCodigo() + ": " + Double.parseDouble(cotacaoAcaoTO.getFechamento())/100 + " dia: " + cotacaoAcaoTO.getPeriodo());
//				resultadoOperacao += analisaMelhorOpcaoCompra(cotacaoAcaoTO, i, datasInicioSerie[i], datasFimSerie[i]);
//			}
//		}
//		System.out.println(resultadoOperacao);
	}
	public static String analisaMelhorOpcaoCompra(CotacaoAtivoTO cotacaoAcaoTO, int indiceSerieOpcao, String dataInicial, String dataFinal) throws SQLException{

		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		Properties SATAProps = SATAPropertyLoader.loadProperties(IConstants.ARQ_SATA_CONF);
		int valorMaximoOpcaoPUT = Integer.parseInt((String)SATAProps.get(PROP_VALORMAX_OPCAO_PUT));
		int valorMaximoOpcaoCALL = Integer.parseInt((String)SATAProps.get(PROP_VALORMAX_OPCAO_CALL));
		int qtdDiasFaltamParaVencimentoPUT = Integer.parseInt((String)SATAProps.get(PROP_QTD_DIAS_FALTAM_VENCIMENTO_PUT));
		int qtdDiasFaltamParaVencimentoCALL = Integer.parseInt((String)SATAProps.get(PROP_QTD_DIAS_FALTAM_VENCIMENTO_CALL));

		int valorAcaoArredondado = Integer.parseInt(cotacaoAcaoTO.getFechamento())/100;
		System.out.println("valor acao arredondado: " + valorAcaoArredondado);
		
		String resultadoOpcaoPUT ="";
		String resultadoOpcaoCALL ="";
		System.out.println("ANALISANDO OPCOES DE VENDA PUT");
		//procura nas puts OTMs da acao
		for(int i = valorAcaoArredondado; i >= valorAcaoArredondado - 4; i--)
		{
			String nomeOpcao = "PETR" + SERIES_PUT[indiceSerieOpcao] + i;
			System.out.println("nomeOpcao: " + nomeOpcao);
			//pega todas as cotacoes da opcao de todos os dias
			List<CotacaoAtivoTO> listaTodasCotacoesOpcaoPUT = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(dataInicial), SATAUtil.getDataFormatadaParaBD(dataFinal));
			if(listaTodasCotacoesOpcaoPUT.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			//pega a cotacao da opcao no dia
			List<CotacaoAtivoTO> listaCotacaoOpcaoCorrente = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()), SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()));
			if(listaCotacaoOpcaoCorrente.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			CotacaoAtivoTO cotacaoOpcaoCorrente = listaCotacaoOpcaoCorrente.get(0);
			int j=0;
			for(CotacaoAtivoTO caTO : listaTodasCotacoesOpcaoPUT){
				j++;
				if(cotacaoOpcaoCorrente.getPeriodo().equalsIgnoreCase(caTO.getPeriodo())){ //Porque existem dias que a opcao nao teve cotacao(negociacao)
//					System.out.println(caTO.getCodigo());
					System.out.println(caTO.getPeriodo());
//					System.out.println(caTO.getAno());
//					System.out.println("A: " + caTO.getAbertura());
//					System.out.println("Max: " + caTO.getMaxima());
//					System.out.println("Min: " + caTO.getMinima());
					System.out.println("F: " + caTO.getFechamento());
					int valorOpcao = Integer.parseInt(caTO.getFechamento());
//					System.out.println("valorOpcao: " + valorOpcao);
					System.out.println("diaDaOpcao: " + j);
					if(valorOpcao <= valorMaximoOpcaoPUT && j < listaTodasCotacoesOpcaoPUT.size() - qtdDiasFaltamParaVencimentoPUT)
					{
						resultadoOpcaoPUT += "[PUT] Melhor opcao put: " + caTO.getCodigo() + " data compra: " + caTO.getPeriodo() + "\n";
						resultadoOpcaoPUT += "[PUT] PRECO DE COMPRA: " + listaTodasCotacoesOpcaoPUT.get(j-1).getFechamento() + "\n";
						resultadoOpcaoPUT += "[PUT] PRECO DE VENDA 75%: " + listaTodasCotacoesOpcaoPUT.get(j).getFechamento() + "\n";
						resultadoOpcaoPUT += "[PUT] PRECO DE VENDA 25%: " + listaTodasCotacoesOpcaoPUT.get(j+1).getFechamento() + "\n";
						//ver o resto das cotacoes
						for(int k=j+2; k<listaTodasCotacoesOpcaoPUT.size(); k++)
							resultadoOpcaoPUT += "[PUT] diaDaOpcao: " + k + " valor: " + listaTodasCotacoesOpcaoPUT.get(k).getFechamento() + "\n";
						
						System.out.println(resultadoOpcaoPUT);
						break;
					}					
				}
			}
			System.out.println("###################");
		}
		
		System.out.println("ANALISANDO OPCOES DE COMPRA CALL");
		//procura nas calls OTMs da acao
		for(int i = valorAcaoArredondado; i <= valorAcaoArredondado + 4; i++)
		{
			String nomeOpcao = "PETR" + SERIES_CALL[indiceSerieOpcao] + i;
			System.out.println("nomeOpcao: " + nomeOpcao);
			//pega todas as cotacoes da opcao de todos os dias
			List<CotacaoAtivoTO> listaTodasCotacoesOpcaoCall = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(dataInicial), SATAUtil.getDataFormatadaParaBD(dataFinal));
			if(listaTodasCotacoesOpcaoCall.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			//pega a cotacao da opcao no dia
			List<CotacaoAtivoTO> listaCotacaoOpcaoCorrente = caDAO.getCotacoesDoAtivo(nomeOpcao, SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()), SATAUtil.getDataFormatadaParaBD(cotacaoAcaoTO.getPeriodo()));
			if(listaCotacaoOpcaoCorrente.size() == 0)
			{
				System.out.println("###################");
				continue;
			}
			CotacaoAtivoTO cotacaoOpcaoCorrente = listaCotacaoOpcaoCorrente.get(0);
			int j=0;
			for(CotacaoAtivoTO caTO : listaTodasCotacoesOpcaoCall){
				j++;
				if(cotacaoOpcaoCorrente.getPeriodo().equalsIgnoreCase(caTO.getPeriodo())){ //Porque existem dias que a opcao nao teve cotacao(negociacao)
					System.out.println(caTO.getPeriodo());
					System.out.println("F: " + caTO.getFechamento());
					int valorOpcao = Integer.parseInt(caTO.getFechamento());
					System.out.println("diaDaOpcao: " + j);
					System.out.println("qtd cotacoes opcao: " + listaTodasCotacoesOpcaoCall.size());
					if(valorOpcao <= valorMaximoOpcaoCALL && j < listaTodasCotacoesOpcaoCall.size() - qtdDiasFaltamParaVencimentoCALL)
					{
						resultadoOpcaoCALL += "[CALL] Melhor opcao call: " + caTO.getCodigo() + " data compra: " + caTO.getPeriodo() + "\n";
						resultadoOpcaoCALL += "[CALL] PRECO DE COMPRA: " + listaTodasCotacoesOpcaoCall.get(j-1).getFechamento() + "\n";
						resultadoOpcaoCALL += "[CALL] PRECO DE VENDA 75%: " + listaTodasCotacoesOpcaoCall.get(j).getFechamento() + "\n";
						resultadoOpcaoCALL += "[CALL] PRECO DE VENDA 25%: " + listaTodasCotacoesOpcaoCall.get(j+1).getFechamento() + "\n";
						//ver o resto das cotacoes
						for(int k=j+2; k<listaTodasCotacoesOpcaoCall.size(); k++)
							resultadoOpcaoCALL += "[CALL] diaDaOpcao: " + k + " valor: " + listaTodasCotacoesOpcaoCall.get(k).getFechamento() + "\n";
						
						System.out.println(resultadoOpcaoCALL);
						break;
					}					
				}
			}
			System.out.println("###################");
		}
		
		return resultadoOpcaoPUT + resultadoOpcaoCALL;
	}

	@Override
	public CotacaoAtivoTO getCotacaoDoAtivo(String codigoAtivo, String data)
			throws SQLException {
		return null;
	}

}

