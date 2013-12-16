package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.CotacaoAtivoTO;

public interface ICotacaoAtivoDAO {

	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo)throws SQLException;
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, Integer ano) throws SQLException;
	public CotacaoAtivoTO getCotacaoDoAtivo(String codigoAtivo, String data) throws SQLException;
	public boolean possuiCotacaoNoAno(String codigoAtivo, Integer ano) throws SQLException;
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String dataInicial, String dataFinal)throws SQLException;
	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO) throws SQLException;
	public boolean existeCotacao(String codigoAtivo, String periodo) throws SQLException;
	public String getDataUltimoCadastro(String codigoAtivo) throws SQLException;
	public int updateCotacaoDoAtivo(CotacaoAtivoTO caTO) throws SQLException;
}
