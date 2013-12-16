package sata.domain.dao.postgre;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAcaoDAO;
import sata.domain.dao.IAcompOpcaoDAO;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.ICotacaoOpcaoDAO;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.IAcompanhamentoDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.dao.ISerieOperacoesDAO;

public class PostgreDAOFactory extends DAOFactory{
	
	// Implementação do singleton
	private PostgreDAOFactory() {}
	private static PostgreDAOFactory instance;
	public static PostgreDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized PostgreDAOFactory create() {
		if (instance == null) instance = new PostgreDAOFactory();
		return instance;
	}
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public IAtivoDAO getAtivoDAO(){
		return new PostgreAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return new PostgreCotacaoAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public ICotacaoOpcaoDAO getCotacaoOpcaoDAO(){
//		return new PostgreCotacaoOpcaoDAO(conPoolManager.getConnectionFromPool());
		return null;
	}
	
	public IAlertaDAO getAlertaDAO() {
		return null;
	}
	
	@Override
	public ISerieOperacoesDAO getSerieOperacoesDAO() {
		return null;
	}

	@Override
	public IOperacaoRealizadaDAO getOperacaoRealizadaDAO() {
		return null;
	}
	
	@Override
	public IAcompanhamentoDAO getAcompanhamentoDAO() {
		return null;
	}
	
	public IInvestidorDAO getInvestidorDAO() {
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
