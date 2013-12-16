package sata.domain.dao;

import java.sql.SQLException;

import sata.domain.to.OperacaoRealizadaTO;

public interface IOperacaoRealizadaDAO {

	public void salvar(OperacaoRealizadaTO operacao) throws SQLException;
	public void excluir(OperacaoRealizadaTO operacao) throws SQLException;
}
