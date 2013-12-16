package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IAlertaDAO;
import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;
import sata.domain.to.SerieOperacoesTO;

public class HibernateAlertaDAO extends GenericDAOHibernate<AlertaTO> implements IAlertaDAO {

	private HibernateAlertaDAO() {
		super(AlertaTO.class);
	}

	@Override
	public List<AlertaTO> listaAlertasAtivos() throws SQLException {
		List<AlertaTO> lista = super.listar("where ativo = 1");
		for (AlertaTO alerta: lista) {
			List<SerieOperacoesTO> remover = new ArrayList<SerieOperacoesTO>();
			for (SerieOperacoesTO serie: alerta.getSeries()) {
				if (!serie.isAtiva()) {
					remover.add(serie);
				}
			}
			alerta.getSeries().removeAll(remover);
		}
		return lista;
	}

	@Override
	public List<AlertaTO> listaAlertasInvestidor(InvestidorTO investidor) throws SQLException {
		return null;
	}
	
	@Override
	public AlertaTO recuperar(Integer id) throws SQLException {
		return null;
	}
	
	// Implementação do singleton
	private static HibernateAlertaDAO instance;
	public static HibernateAlertaDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAlertaDAO create() {
		if (instance == null) instance = new HibernateAlertaDAO();
		return instance;
	}
}
