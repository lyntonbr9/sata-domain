package sata.domain.dao.hibernate;

import java.sql.SQLException;

import sata.domain.dao.IAcompOpcaoDAO;
import sata.domain.to.AcompOpcaoTO;

public class HibernateAcompOpcaoDAO extends GenericDAOHibernate<AcompOpcaoTO> implements IAcompOpcaoDAO {

	public HibernateAcompOpcaoDAO() {
		super(AcompOpcaoTO.class);
	}
	
	@Override
	public AcompOpcaoTO recuperar(Integer id) throws SQLException {
		return super.recuperar(id);
	}
	
	// Implementação do singleton
	private static HibernateAcompOpcaoDAO instance;
	public static HibernateAcompOpcaoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAcompOpcaoDAO create() {
		if (instance == null) instance = new HibernateAcompOpcaoDAO();
		return instance;
	}
}
