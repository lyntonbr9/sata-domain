package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.InvestidorTO;

public interface IInvestidorDAO {

	public List<InvestidorTO> listar() throws SQLException;
	public void salvar(InvestidorTO investidor) throws SQLException;
	public InvestidorTO recuperar(String email) throws SQLException;
}
