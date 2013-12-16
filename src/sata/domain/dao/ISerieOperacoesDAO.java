package sata.domain.dao;

import java.sql.SQLException;

import sata.domain.to.SerieOperacoesTO;

public interface ISerieOperacoesDAO {

	public void salvar(SerieOperacoesTO serie) throws SQLException;
	public void excluir(SerieOperacoesTO serie) throws SQLException;
}
