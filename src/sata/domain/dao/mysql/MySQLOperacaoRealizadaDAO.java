package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.to.OperacaoRealizadaTO;

public class MySQLOperacaoRealizadaDAO implements IOperacaoRealizadaDAO {
	
	private Connection con;
	
	@Override
	public void salvar(OperacaoRealizadaTO operacao) throws SQLException {
		if (operacao.getId() == null) { // INSERT
			String sql = "INSERT INTO OperacaoRealizada (id_serie, posicao, qtdLotes, ativo, valor) " +
						 "VALUES (?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setInt(i++, operacao.getSerie().getId());
			ps.setString(i++, String.valueOf(operacao.getPosicao().getKey()));
			ps.setInt(i++, operacao.getQtdLotes());
			ps.setString(i++, operacao.getAtivo());
			ps.setBigDecimal(i++, operacao.getValor());
			ps.executeUpdate();
			
			sql = "SELECT max(id) as id FROM OperacaoRealizada";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) operacao.setId(rs.getInt("id"));
		}
		else { // UPDATE
			String sql = "UPDATE OperacaoRealizada SET posicao = ?, qtdLotes = ?, valor = ? WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, String.valueOf(operacao.getPosicao().getKey()));
			ps.setInt(i++, operacao.getQtdLotes());
			ps.setBigDecimal(i++, operacao.getValor());
			ps.setInt(i++, operacao.getId());
			ps.executeUpdate();
		}
		MySQLDAOFactory.returnConnection(con);
	}
	
	@Override
	public void excluir(OperacaoRealizadaTO operacao) throws SQLException {
		String sql = "UPDATE OperacaoRealizada SET dtExclusao = NOW() WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, operacao.getId());
		ps.executeUpdate();
		MySQLDAOFactory.returnConnection(con);
	}
	
	// Implementação do singleton
	private MySQLOperacaoRealizadaDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLOperacaoRealizadaDAO instance;
	public static MySQLOperacaoRealizadaDAO get(Connection connection) {
		if (instance != null) {
			instance.con = connection;
			return instance;
		}
		return create(connection); 
	}
	private static synchronized MySQLOperacaoRealizadaDAO create(Connection connection) {
		if (instance == null) instance = new MySQLOperacaoRealizadaDAO(connection);
		return instance;
	}
}
