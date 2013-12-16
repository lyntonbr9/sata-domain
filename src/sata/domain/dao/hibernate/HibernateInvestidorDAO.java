package sata.domain.dao.hibernate;

import java.sql.SQLException;

import sata.domain.dao.IInvestidorDAO;
import sata.domain.to.InvestidorTO;

public class HibernateInvestidorDAO extends GenericDAOHibernate<InvestidorTO> implements IInvestidorDAO {

	public HibernateInvestidorDAO() {
		super(InvestidorTO.class);
	}
	
	@Override
	public InvestidorTO recuperar(String email) throws SQLException {
		return super.listar("where email = '" + email + "'").get(0);
	}

	// Implementação do singleton
	private static HibernateInvestidorDAO instance;
	public static HibernateInvestidorDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateInvestidorDAO create() {
		if (instance == null) instance = new HibernateInvestidorDAO();
		return instance;
	}
}
