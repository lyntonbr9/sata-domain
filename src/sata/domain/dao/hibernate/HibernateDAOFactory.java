package sata.domain.dao.hibernate;

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

public class HibernateDAOFactory extends DAOFactory {

	@Override
	public IAtivoDAO getAtivoDAO() {
		return null;
	}

	@Override
	public ICotacaoAtivoDAO getCotacaoAtivoDAO() {
		return HibernateCotacaoAtivoDAO.singleton();
	}

	@Override
	public IAlertaDAO getAlertaDAO() {
		return HibernateAlertaDAO.singleton();
	}
	
	@Override
	public ISerieOperacoesDAO getSerieOperacoesDAO() {
		return HibernateSerieOperacoesDAO.singleton();
	}

	@Override
	public IOperacaoRealizadaDAO getOperacaoRealizadaDAO() {
		return HibernateOperacaoRealizadaDAO.singleton();
	}

	@Override
	public IInvestidorDAO getInvestidorDAO() {
		return HibernateInvestidorDAO.singleton();
	}
	
	@Override
	public IAcompanhamentoDAO getAcompanhamentoDAO() {
		return HibernateAcompanhamentoDAO.singleton();
	}
	
	@Override
	public IAcompOpcaoDAO getAcompOpcaoDAO() {
		return HibernateAcompOpcaoDAO.singleton();
	}
	
	@Override
	public IOpcaoDAO getOpcaoDAO() {
		return HibernateOpcaoDAO.singleton();
	}
	
	@Override
	public IAcaoDAO getAcaoDAO() {
		return HibernateAcaoDAO.singleton();
	}
	
	@Override
	public ICotacaoOpcaoDAO getCotacaoOpcaoDAO() {
		return HibernateCotacaoOpcaoDAO.singleton();
	}

	// Implementação do singleton
	private HibernateDAOFactory() {}
	private static HibernateDAOFactory instance;
	public static HibernateDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateDAOFactory create() {
		if (instance == null) instance = new HibernateDAOFactory();
		return instance;
	}
}
