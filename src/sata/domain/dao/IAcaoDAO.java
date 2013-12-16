package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.auto.operacao.ativo.Acao;

public interface IAcaoDAO {

	public List<Acao> listar() throws SQLException;
	public void salvar(Acao acao) throws SQLException;
	public Acao recuperar(String codigo) throws SQLException;
}
