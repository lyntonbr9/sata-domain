package sata.domain.dao;

import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class SATAFactoryFacade implements IConstants {
	
	public static ICotacaoOpcaoDAO getCotacaoOpcaoDAO(){
		return getDAOFactory().getCotacaoOpcaoDAO();
	}	
	public static IAtivoDAO getAtivoDAO(){
		return getDAOFactory().getAtivoDAO();
	}
	public static ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return getDAOFactory().getCotacaoAtivoDAO();
	}
	public static IAlertaDAO getAlertaDAO(){
		return getDAOFactory().getAlertaDAO();
	}
	public static ISerieOperacoesDAO getSerieOperacoesDAO(){
		return getDAOFactory().getSerieOperacoesDAO();
	}
	public static IOperacaoRealizadaDAO getOperacaoRealizadaDAO(){
		return getDAOFactory().getOperacaoRealizadaDAO();
	}
	public static IInvestidorDAO getInvestidorDAO(){
		return getDAOFactory().getInvestidorDAO();
	}
	public static IAcompanhamentoDAO getAcompanhamentoDAO(){
		return getDAOFactory().getAcompanhamentoDAO();
	}
	public static IAcompOpcaoDAO getAcompOpcaoDAO(){
		return getDAOFactory().getAcompOpcaoDAO();
	}
	public static IOpcaoDAO getOpcaoDAO(){
		return getDAOFactory().getOpcaoDAO();
	}
	public static IAcaoDAO getAcaoDAO(){
		return getDAOFactory().getAcaoDAO();
	}
	
	private static DAOFactory getDAOFactory() {
		String driver = SATAPropertyLoader.getProperty(PROP_SATA_BD);
		if (driver.equals(BD_MYSQL))
			return DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		
		else if (driver.equals(BD_POSTGRE))
			return DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		
		else if (driver.equals(BD_HIBERNATE))
			return DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
		
		else return null;
	}
}
