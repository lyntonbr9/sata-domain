package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import sata.domain.dao.ICotacaoOpcaoDAO;
import sata.domain.to.CotacaoOpcaoTO;
import sata.domain.to.SplitAtivoTO;

public class HibernateCotacaoOpcaoDAO extends GenericDAOHibernate<CotacaoOpcaoTO> implements ICotacaoOpcaoDAO {
	
	@Override
	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao) throws SQLException {
//		return setSplit(super.listar("where codigo = ? order by periodo", codigoOpcao));
		return super.listar("where codigo like ? order by periodo", codigoOpcao+"%");
	}
	@Override
	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao, Integer ano) throws SQLException {
//		return setSplit(super.listar("where codigo = ? and ano = ? order by periodo", codigoOpcao, ano.toString()));
		return super.listar("where codigo like ? and ano = ? order by periodo", codigoOpcao+"%", ano.toString());
	}
	@Override
	public CotacaoOpcaoTO getCotacaoDaOpcao(String codigoOpcao, String data) throws SQLException {
		try {
//			return setSplit(super.listar("where codigo = ? and periodo like ?", codigoOpcao, data+"%")).get(0);
//			return super.listar("where codigo like ? and periodo like ?", codigoOpcao+"%", data+"%").get(0);
			return setSplit(super.listar("where codigo like ? and periodo like ?", codigoOpcao+"%", data+"%")).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		} 
	}
	@Override
	public boolean possuiCotacaoNoAno(String codigoOpcao, Integer ano) throws SQLException {
		return !getCotacoesDaOpcao(codigoOpcao, ano).isEmpty();
	}
	@Override
	public List<CotacaoOpcaoTO> getCotacoesDaOpcao(String codigoOpcao, String dataInicial, String dataFinal) throws SQLException {
//		return setSplit(super.listar("where codigo like ? and periodo BETWEEN ? and ? order by periodo", codigoOpcao+"%", dataInicial, dataFinal));
//		return super.listar("where codigo like ? and periodo BETWEEN ? and ? order by periodo", codigoOpcao+"%", dataInicial, dataFinal);
		return setSplit(super.listar("where codigo like ? and periodo BETWEEN ? and ? order by periodo", codigoOpcao+"%", dataInicial, dataFinal));
	}
	
	@Override
	public void insertCotacaoDaOpcao(CotacaoOpcaoTO caTO) throws SQLException {
		super.salvar(caTO);
	}
	@Override
	public boolean existeCotacao(String codigoOpcao, String periodo) throws SQLException {
		return getCotacaoDaOpcao(codigoOpcao, periodo) != null;
	}
	@Override
	public String getDataUltimoCadastro(String codigoOpcao) {
		return null; //TODO implementar
	}
	@Override
	public int updateCotacaoDaOpcao(CotacaoOpcaoTO caTO) throws SQLException {
		super.salvar(caTO);
		return 0;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<CotacaoOpcaoTO> setSplit(List<CotacaoOpcaoTO> list) throws SQLException {
		for (CotacaoOpcaoTO cotacao: list) {
			String query = "from SplitAtivoTO where codigoAtivo = '" + cotacao.getCodigoAcao()
				+ "' and periodo >= '" + cotacao.getPeriodo() + "'";
			List<SplitAtivoTO> splits = (List<SplitAtivoTO>) super.executeQuery(query).list();
			int split = 1;
			if (!splits.isEmpty()) {
				for (SplitAtivoTO splitAtivo: splits) {
					split *= splitAtivo.getSplit();
				}
			}
			cotacao.setSplit(split);
		}
		return list;
	}

	public HibernateCotacaoOpcaoDAO() {
		super(CotacaoOpcaoTO.class);
	}

	// Implementação do singleton
	private static HibernateCotacaoOpcaoDAO instance;
	public static HibernateCotacaoOpcaoDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateCotacaoOpcaoDAO create() {
		if (instance == null) instance = new HibernateCotacaoOpcaoDAO();
		return instance;
	}
}
