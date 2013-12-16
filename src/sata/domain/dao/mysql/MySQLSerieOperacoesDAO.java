package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import sata.domain.dao.ISerieOperacoesDAO;
import sata.domain.to.SerieOperacoesTO;
import sata.domain.util.SATAUtil;

public class MySQLSerieOperacoesDAO implements ISerieOperacoesDAO {
	
	private Connection con;
	@Override
	public void salvar(SerieOperacoesTO serie) throws SQLException {
		if (serie.getId() == null) { // INSERT
			String sql = "INSERT INTO SerieOperacoes (id_alerta, id_investidor, dataExecucao, acao, " +
						 "precoAcao, qtdLotesAcao, ativo) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setInt(i++, serie.getAlerta().getId());
			ps.setInt(i++, serie.getInvestidor().getId());
			ps.setDate(i++, SATAUtil.converteToSQLDate(serie.getDataExecucao()));
			ps.setString(i++, serie.getAcao().getNome());
			ps.setBigDecimal(i++, serie.getPrecoAcao());
			ps.setInt(i++, serie.getAlerta().getId());
			ps.setBoolean(i++, serie.isAtiva());
			ps.executeUpdate();
			
			sql = "SELECT max(id) as id FROM SerieOperacoes";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) serie.setId(rs.getInt("id"));
		}
		else { // UPDATE
			String sql = "UPDATE SerieOperacoes SET id_investidor = ?, dataExecucao = ?, acao = ?, " +
						"precoAcao = ?, qtdLotesAcao = ?, ativo = ? WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setInt(i++, serie.getInvestidor().getId());
			ps.setDate(i++, SATAUtil.converteToSQLDate(serie.getDataExecucao()));
			ps.setString(i++, serie.getAcao().getNome());
			ps.setBigDecimal(i++, serie.getPrecoAcao());
			ps.setInt(i++, serie.getQtdLotesAcao());
			ps.setBoolean(i++, serie.isAtiva());
			ps.setInt(i++, serie.getId());
			ps.executeUpdate();
		}
		MySQLDAOFactory.returnConnection(con);
	}
	
	@Override
	public void excluir(SerieOperacoesTO serie) throws SQLException {
		String sql = "UPDATE SerieOperacoes SET dtExclusao = NOW() WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, serie.getId());
		ps.executeUpdate();
		MySQLDAOFactory.returnConnection(con);
	}
	
	// Implementação do singleton
	private MySQLSerieOperacoesDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLSerieOperacoesDAO instance;
	public static MySQLSerieOperacoesDAO get(Connection connection) {
		if (instance != null) {
			instance.con = connection;
			return instance;
		}
		return create(connection); 
	}
	private static synchronized MySQLSerieOperacoesDAO create(Connection connection) {
		if (instance == null) instance = new MySQLSerieOperacoesDAO(connection);
		return instance;
	}
}
