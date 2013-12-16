package sata.domain.dao.hibernate;

import sata.domain.dao.ISerieOperacoesDAO;
import sata.domain.to.SerieOperacoesTO;

public class HibernateSerieOperacoesDAO extends GenericDAOHibernate<SerieOperacoesTO> implements ISerieOperacoesDAO {

	public HibernateSerieOperacoesDAO() {
		super(SerieOperacoesTO.class);
	}

	// Implementa��o do singleton
	private static HibernateSerieOperacoesDAO instance;
	public static HibernateSerieOperacoesDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateSerieOperacoesDAO create() {
		if (instance == null) instance = new HibernateSerieOperacoesDAO();
		return instance;
	}
}
