package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import sata.auto.enums.Posicao;
import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.domain.dao.IAlertaDAO;
import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;

public class MySQLAlertaDAO implements IAlertaDAO {
	
	private Connection con;
	private final Integer[] noParam = {}; 
	
	@Override
	public AlertaTO recuperar(Integer id) throws SQLException {
		Integer[] paramAlerta = {id};
		List<AlertaTO> list = listaAlertas("id = ?", null, null, paramAlerta, noParam, noParam);
		if (!list.isEmpty()) return list.get(0);
		else return null;
	}
	
	@Override
	public List<AlertaTO> listar() throws SQLException {
		return listaAlertas(null, null, null);
	}
	
	@Override
	public List<AlertaTO> listaAlertasAtivos() throws SQLException {
		return listaAlertas("ativo = 1", "ativo = 1", null);
	}

	@Override
	public List<AlertaTO> listaAlertasInvestidor(InvestidorTO investidor) throws SQLException {
		return null;
	}
	
	private List<AlertaTO> listaAlertas(String whereAlerta, String whereSerie, String whereOperacao) throws SQLException {
		return listaAlertas(whereAlerta, whereSerie, whereOperacao, noParam, noParam, noParam);
	}

	private List<AlertaTO> listaAlertas(String whereAlerta, String whereSerie, String whereOperacao, 
			Integer[] paramsAlerta, Integer[] paramsSerie, Integer[] paramsOperacao) throws SQLException {
		List<AlertaTO> listaAlertasAtivos = new ArrayList<AlertaTO>();
		String query = "SELECT * FROM AlertaOperacao WHERE dtExclusao IS NULL";
		if (!StringUtils.isEmpty(whereAlerta))
			query += " AND " + whereAlerta;
		PreparedStatement ps = con.prepareStatement(query);
		for (int i=1; i <= paramsAlerta.length; i++) {
			ps.setInt(i, paramsAlerta[i]);
		}
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			AlertaTO alerta = new AlertaTO(); 
			alerta.setId(rs.getInt("id"));
			alerta.setNome(rs.getString("nome"));
			alerta.setPorcentagemGanho(rs.getInt("porcentagemGanho"));
			alerta.setPorcentagemPerda(rs.getInt("porcentagemPerda"));
			alerta.setTipoCalculoVI(TipoCalculoValorInvestido.get(rs.getString("tipoCalculoVI")));
			alerta.setPercCalculoVI(rs.getInt("percCalculoVI"));
			alerta.setAtivo(rs.getBoolean("ativo"));
			alerta.setSeries(new ArrayList<SerieOperacoesTO>());
			
			query = "SELECT * FROM SerieOperacoes WHERE id_alerta = ? AND dtExclusao IS NULL";
			if (!StringUtils.isEmpty(whereSerie))
				query += " AND " + whereSerie;
			PreparedStatement psSerie = con.prepareStatement(query);
			psSerie.setInt(1, alerta.getId());
			for (int i=1; i <= paramsSerie.length; i++) {
				psSerie.setInt(i, paramsSerie[i]);
			}
			ResultSet rsSerie = psSerie.executeQuery();
			while(rsSerie.next()){
				SerieOperacoesTO serie = new SerieOperacoesTO();
				serie.setId(rsSerie.getInt("id"));
				serie.setAlerta(alerta);
				serie.setAcao(AcaoConteiner.get(rsSerie.getString("acao")));
				serie.setPrecoAcao(rsSerie.getBigDecimal("precoAcao"));
				serie.setQtdLotesAcao(rsSerie.getInt("qtdLotesAcao"));
				serie.setDataExecucao(rsSerie.getDate("dataExecucao"));
				serie.setAtiva(rsSerie.getBoolean("ativo"));
				
				query = "SELECT * FROM Investidor WHERE id = ?";
				PreparedStatement psInvest = con.prepareStatement(query);
				psInvest.setInt(1, rsSerie.getInt("id_investidor"));
				ResultSet rsInvest = psInvest.executeQuery();
				if (rsInvest.next()) {
					InvestidorTO investidor = new InvestidorTO();
					investidor.setId(rsInvest.getInt("id"));
					investidor.setNome(rsInvest.getString("nome"));
					investidor.setEmail(rsInvest.getString("email"));
					serie.setInvestidor(investidor);
				}

				serie.setOperacoes(new ArrayList<OperacaoRealizadaTO>());
				
				query = "SELECT * FROM OperacaoRealizada WHERE id_serie = ? AND dtExclusao IS NULL";
				if (!StringUtils.isEmpty(whereOperacao))
					query += " AND " + whereOperacao;
				PreparedStatement psOp = con.prepareStatement(query);
				psOp.setInt(1, serie.getId());
				for (int i=1; i <= paramsOperacao.length; i++) {
					psOp.setInt(i, paramsOperacao[i]);
				}
				ResultSet rsOp = psOp.executeQuery();
				while(rsOp.next()){
					OperacaoRealizadaTO op = new OperacaoRealizadaTO();
					op.setId(rsOp.getInt("id"));
					op.setPosicao(Posicao.get(rsOp.getString("posicao").charAt(0)));
					op.setQtdLotes(rsOp.getInt("qtdLotes"));
					op.setAtivo(rsOp.getString("ativo"));
					op.setValor(rsOp.getBigDecimal("valor"));
					op.setSerie(serie);
					serie.getOperacoes().add(op);
				}
				alerta.getSeries().add(serie);
			}
			
			listaAlertasAtivos.add(alerta);
		}
		MySQLDAOFactory.returnConnection(con);
		return listaAlertasAtivos;
	}
	
	@Override
	public void salvar(AlertaTO alerta) throws SQLException {
		if (alerta.getId() == null) { // INSERT
			String sql = "INSERT INTO AlertaOperacao (nome, porcentagemGanho, porcentagemPerda, tipoCalculoVI, " +
						 "percCalculoVI, ativo) VALUES (?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, alerta.getNome());
			ps.setInt(i++, alerta.getPorcentagemGanho());
			ps.setInt(i++, alerta.getPorcentagemPerda());
			ps.setString(i++, alerta.getTipoCalculoVI().getName());
			ps.setInt(i++, alerta.getPercCalculoVI());
			ps.setBoolean(i++, alerta.isAtivo());
			ps.executeUpdate();
			
			sql = "SELECT max(id) as id FROM AlertaOperacao";
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) alerta.setId(rs.getInt("id"));
		}
		else { // UPDATE
			String sql = "UPDATE AlertaOperacao SET nome = ?, porcentagemGanho = ?, porcentagemPerda = ?, " +
					"tipoCalculoVI = ? , percCalculoVI = ?, ativo = ? WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			int i = 1;
			ps.setString(i++, alerta.getNome());
			ps.setInt(i++, alerta.getPorcentagemGanho());
			ps.setInt(i++, alerta.getPorcentagemPerda());
			ps.setString(i++, alerta.getTipoCalculoVI().getName());
			ps.setInt(i++, alerta.getPercCalculoVI());
			ps.setBoolean(i++, alerta.isAtivo());
			ps.setInt(i++, alerta.getId());
			ps.executeUpdate();
		}
		MySQLDAOFactory.returnConnection(con);
	}
	
	@Override
	public void excluir(AlertaTO alerta) throws SQLException {
		String sql = "UPDATE AlertaOperacao SET dtExclusao = NOW() WHERE id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, alerta.getId());
		ps.executeUpdate();
		MySQLDAOFactory.returnConnection(con);
	}
	
	// Implementação do singleton
	private MySQLAlertaDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLAlertaDAO instance;
	public static MySQLAlertaDAO get(Connection connection) {
		if (instance != null) {
			instance.con = connection;
			return instance;
		}
		return create(connection); 
	}
	private static synchronized MySQLAlertaDAO create(Connection connection) {
		if (instance == null) instance = new MySQLAlertaDAO(connection);
		return instance;
	}
}
