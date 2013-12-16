package sata.domain.dao;

//import sata.domain.dao.arquivo.ArquivoDAOFactory;
import sata.domain.dao.hibernate.HibernateDAOFactory;
import sata.domain.dao.mysql.MySQLDAOFactory;
import sata.domain.dao.postgre.PostgreDAOFactory;

public abstract class DAOFactory {
	
	public static final int ARQUIVO = 1;
	public static final int POSTGRESQL = 2;
	public static final int MYSQL = 3;
	public static final int HIBERNATE = 4;
	
	public abstract IAtivoDAO getAtivoDAO();
	public abstract ICotacaoAtivoDAO getCotacaoAtivoDAO();
	public abstract ICotacaoOpcaoDAO getCotacaoOpcaoDAO();
	public abstract IAlertaDAO getAlertaDAO();
	public abstract ISerieOperacoesDAO getSerieOperacoesDAO();
	public abstract IOperacaoRealizadaDAO getOperacaoRealizadaDAO();
	public abstract IInvestidorDAO getInvestidorDAO();
	public abstract IAcompanhamentoDAO getAcompanhamentoDAO();
	public abstract IAcompOpcaoDAO getAcompOpcaoDAO();
	public abstract IOpcaoDAO getOpcaoDAO();
	public abstract IAcaoDAO getAcaoDAO();
	
	public static DAOFactory getDAOFactory(int wichFactory){
		switch(wichFactory)
		{
			case ARQUIVO:
//				return new ArquivoDAOFactory();
			case POSTGRESQL:
				return PostgreDAOFactory.singleton();
			case MYSQL:
				return MySQLDAOFactory.singleton();
			case HIBERNATE:
				return HibernateDAOFactory.singleton();
			default:
				return null;
		}
	}

}
