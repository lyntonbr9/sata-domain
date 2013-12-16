package sata.domain.dao.mysql;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAcaoDAO;
import sata.domain.dao.IAcompOpcaoDAO;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.ICotacaoOpcaoDAO;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.IAcompanhamentoDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.dao.ISerieOperacoesDAO;

public class MySQLDAOFactory extends DAOFactory{
	
	// Implementação do singleton
	private MySQLDAOFactory() {}
	private static MySQLDAOFactory instance;
	public static MySQLDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized MySQLDAOFactory create() {
		if (instance == null) instance = new MySQLDAOFactory();
		return instance;
	}
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
	@Override
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return MySQLCotacaoAtivoDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public ICotacaoOpcaoDAO getCotacaoOpcaoDAO(){
//		return MySQLCotacaoOpcaoDAO.get(conPoolManager.getConnectionFromPool());
		return null;
	}
	
	@Override
	public IAlertaDAO getAlertaDAO(){
		return MySQLAlertaDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public ISerieOperacoesDAO getSerieOperacoesDAO() {
		return MySQLSerieOperacoesDAO.get(conPoolManager.getConnectionFromPool());
	}

	@Override
	public IOperacaoRealizadaDAO getOperacaoRealizadaDAO() {
		return MySQLOperacaoRealizadaDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IInvestidorDAO getInvestidorDAO() {
		return MySQLInvestidorDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IAtivoDAO getAtivoDAO() {
		return null;
	}
	
	@Override
	public IAcompanhamentoDAO getAcompanhamentoDAO() {
		return null;
	}
	
	@Override
	public IOpcaoDAO getOpcaoDAO() {
		return null;
	}

	@Override
	public IAcaoDAO getAcaoDAO() {
		return null;
	}
	@Override
	public IAcompOpcaoDAO getAcompOpcaoDAO() {
		return null;
	}
}
