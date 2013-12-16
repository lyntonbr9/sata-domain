package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AcompOpcaoTO;

public interface IAcompOpcaoDAO {

	public List<AcompOpcaoTO> listar() throws SQLException;
	public void salvar(AcompOpcaoTO opcao) throws SQLException;
	public void excluir(AcompOpcaoTO alerta) throws SQLException;
	public AcompOpcaoTO recuperar(Integer id) throws SQLException;
}
