package sata.domain.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IAtivoDAO;
import sata.domain.to.AtivoTO;

public class PostgreAtivoDAO implements IAtivoDAO {

	private Connection con;
	
	public PostgreAtivoDAO(Connection postgreConnection){
		this.con = postgreConnection;
	}

	public AtivoTO getAtivo(String codigo) {
		return null;
	}

	public List<String> getCodigosAtivos() {
		List<String> listaCodigosAtivos = new ArrayList<String>();
		String sqlStmt = "SELECT * FROM \"Ativo\"";
		try {
			PreparedStatement ps  = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String ativo = rs.getString("codigoAtivo");
				listaCodigosAtivos.add(ativo);
			}
			
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCodigosAtivos;
	}

	@Override
	public List<String> getCodigosOpcoesLiquidas(String ano) {
		List<String> listaCodigosOpcoesLiquidas = new ArrayList<String>();
		String sqlStmt = "SELECT * FROM \"OpcoesLiquidas\" "
				+ " WHERE ano = ?";
		try {
			PreparedStatement ps  = con.prepareStatement(sqlStmt);
			ps.setString(1, ano);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String ativo = rs.getString("codigoAtivo");
				listaCodigosOpcoesLiquidas.add(ativo);
			}
			
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCodigosOpcoesLiquidas;
	}
}
