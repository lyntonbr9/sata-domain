package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AcompanhamentoTO;

public interface IAcompanhamentoDAO {

	public List<AcompanhamentoTO> listar() throws SQLException;
	public void salvar(AcompanhamentoTO opcao) throws SQLException;
	public void excluir(AcompanhamentoTO alerta) throws SQLException;
	public AcompanhamentoTO recuperar(Integer id) throws SQLException;
}
