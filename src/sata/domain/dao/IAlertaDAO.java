package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;

public interface IAlertaDAO {

	public AlertaTO recuperar(Integer id) throws SQLException;
	public List<AlertaTO> listar() throws SQLException;
	public void salvar(AlertaTO alerta) throws SQLException;
	public void excluir(AlertaTO alerta) throws SQLException;
	public List<AlertaTO> listaAlertasAtivos() throws SQLException;
	public List<AlertaTO> listaAlertasInvestidor(InvestidorTO investidor) throws SQLException;
}
