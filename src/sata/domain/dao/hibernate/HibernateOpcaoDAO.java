package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import sata.auto.operacao.ativo.Acao;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.to.OpcaoTO;

public class HibernateOpcaoDAO extends GenericDAOHibernate<OpcaoTO> implements IOpcaoDAO {

	public HibernateOpcaoDAO() {
		super(OpcaoTO.class);
	}
	
	@Override
	public List<OpcaoTO> pesquisa(Acao acao, Date dataVencimento) throws SQLException {
		return super.listar("where acao = ? and dataVencimento = ?", acao, dataVencimento);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Date> listarDatasVencimento() throws SQLException {
		return (List<Date>) super.executeQuery("select distinct dataVencimento from OpcaoTO where dataVencimento >= NOW() order by dataVencimento").list();
	}

	@Override
	public OpcaoTO recuperar(String codigo) throws SQLException {
		return super.recuperar(codigo);
	}
	
	// Implementação do singleton
	private static HibernateOpcaoDAO instance;
	public static HibernateOpcaoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateOpcaoDAO create() {
		if (instance == null) instance = new HibernateOpcaoDAO();
		return instance;
	}
}
