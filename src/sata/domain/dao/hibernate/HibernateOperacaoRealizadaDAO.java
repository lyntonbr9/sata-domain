package sata.domain.dao.hibernate;

import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.to.OperacaoRealizadaTO;

public class HibernateOperacaoRealizadaDAO extends GenericDAOHibernate<OperacaoRealizadaTO> implements IOperacaoRealizadaDAO {

	public HibernateOperacaoRealizadaDAO() {
		super(OperacaoRealizadaTO.class);
	}

	// Implementação do singleton
	private static HibernateOperacaoRealizadaDAO instance;
	public static HibernateOperacaoRealizadaDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateOperacaoRealizadaDAO create() {
		if (instance == null) instance = new HibernateOperacaoRealizadaDAO();
		return instance;
	}
}
