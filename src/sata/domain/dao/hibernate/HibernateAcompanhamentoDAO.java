package sata.domain.dao.hibernate;

import java.sql.SQLException;

import sata.domain.dao.IAcompanhamentoDAO;
import sata.domain.to.AcompanhamentoTO;

public class HibernateAcompanhamentoDAO extends GenericDAOHibernate<AcompanhamentoTO> implements IAcompanhamentoDAO {

	public HibernateAcompanhamentoDAO() {
		super(AcompanhamentoTO.class);
	}
	
	@Override
	public AcompanhamentoTO recuperar(Integer id) throws SQLException {
		return super.recuperar(id);
	}
	
	// Implementação do singleton
	private static HibernateAcompanhamentoDAO instance;
	public static HibernateAcompanhamentoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAcompanhamentoDAO create() {
		if (instance == null) instance = new HibernateAcompanhamentoDAO();
		return instance;
	}
}
