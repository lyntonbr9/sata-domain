package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.CotacaoOpcaoTO;

public interface ICotacaoOpcaoDAO {

	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao)throws SQLException;
	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao, Integer ano) throws SQLException;
	public CotacaoOpcaoTO getCotacaoDaOpcao(String codigoOpcao, String data) throws SQLException;
	public boolean possuiCotacaoNoAno(String codigoOpcao, Integer ano) throws SQLException;
	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao, String dataInicial, String dataFinal) throws SQLException;
	public void insertCotacaoDaOpcao(CotacaoOpcaoTO caTO) throws SQLException;
	public boolean existeCotacao(String codigoOpcao, String periodo) throws SQLException;
	public String getDataUltimoCadastro(String codigoOpcao) throws SQLException;
	public int updateCotacaoDaOpcao(CotacaoOpcaoTO caTO) throws SQLException;
}
