package sata.domain.dao.hibernate;

import java.sql.SQLException;

import sata.auto.operacao.ativo.Acao;
import sata.domain.dao.IAcaoDAO;

public class HibernateAcaoDAO extends GenericDAOHibernate<Acao> implements IAcaoDAO {

	public HibernateAcaoDAO() {
		super(Acao.class);
	}
	
	@Override
	public Acao recuperar(String codigo) throws SQLException {
		return super.recuperar(codigo);
	}
	
	// Implementação do singleton
	private static HibernateAcaoDAO instance;
	public static HibernateAcaoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAcaoDAO create() {
		if (instance == null) instance = new HibernateAcaoDAO();
		return instance;
	}
}
