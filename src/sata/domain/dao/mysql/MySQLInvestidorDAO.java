package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IInvestidorDAO;
import sata.domain.to.InvestidorTO;

public class MySQLInvestidorDAO implements IInvestidorDAO {
	
	private Connection con;
	
	@Override
	public List<InvestidorTO> listar() throws SQLException {
		List<InvestidorTO> lista = new ArrayList<InvestidorTO>();
		String query = "SELECT * FROM Investidor";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			InvestidorTO investidor = new InvestidorTO();
			investidor.setId(rs.getInt("id"));
			investidor.setNome(rs.getString("nome"));
			investidor.setEmail(rs.getString("email"));
			lista.add(investidor);
		}
		MySQLDAOFactory.returnConnection(con);
		return lista;
	}
	
	public void salvar(InvestidorTO investidor) throws SQLException {
		if (investidor.getId() == null) { //INSERT
			
		}
		else { //UPDATE
			String sql = "UPDATE Investidor SET nome = ?, email = ?, senha = ? WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, investidor.getNome());
			ps.setString(i++, investidor.getEmail());
			ps.setString(i++, investidor.getSenha());
			ps.setInt(i++, investidor.getId());
			ps.executeUpdate();
		}
		MySQLDAOFactory.returnConnection(con);
	}
	
	public InvestidorTO recuperar(String email) throws SQLException {
		String sql = "SELECT * FROM Investidor WHERE email = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		int i = 1;
		ps.setString(i++, email);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			InvestidorTO investidor = new InvestidorTO();
			investidor.setId(rs.getInt("id"));
			investidor.setNome(rs.getString("nome"));
			investidor.setEmail(rs.getString("email"));
			investidor.setSenha(rs.getString("senha"));
			MySQLDAOFactory.returnConnection(con);
			return investidor;
		}
		MySQLDAOFactory.returnConnection(con);
		return null;
	}
	
	// Implementação do singleton
	private MySQLInvestidorDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLInvestidorDAO instance;
	public static MySQLInvestidorDAO get(Connection connection) {	
		if (instance != null) {
			instance.con = connection;
			return instance;
		}
		return create(connection);  
	}
	private static synchronized MySQLInvestidorDAO create(Connection connection) {
		if (instance == null) instance = new MySQLInvestidorDAO(connection);
		return instance;
	}
}
