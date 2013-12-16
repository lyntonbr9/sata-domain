package sata.domain.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import sata.auto.operacao.ativo.Acao;
import sata.domain.to.OpcaoTO;

public interface IOpcaoDAO {

	public List<OpcaoTO> listar() throws SQLException;
	public void salvar(OpcaoTO opcao) throws SQLException;
	public OpcaoTO recuperar(String codigo) throws SQLException;
	public List<OpcaoTO> pesquisa(Acao acao, Date dataVencimento) throws SQLException;
	public List<Date> listarDatasVencimento() throws SQLException;
}
