package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.BancoDadosEX;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.exception.SATAEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.Dia;
import sata.auto.to.DiaCotacao;
import sata.auto.to.Periodo;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.Cache;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class PrecoAcao extends Preco implements IConstants {
	
	private static Cache<DiaCotacao,CotacaoAtivoTO> cacheCotacoes = new Cache<DiaCotacao,CotacaoAtivoTO>(500);
	private static Cache<DiaCotacao,BigDecimal> cacheMM = new Cache<DiaCotacao,BigDecimal>(500);
	
	private Acao acao;
	
	public PrecoAcao() {}
	
	public PrecoAcao (Acao acao, Dia dia) {
		this.acao = acao;
		this.dia = dia;
	}
	
	@Override
	public void calculaPreco() throws SATAEX {
		CotacaoAtivoTO cotacaoAtivo = getCotacaoAcao(dia);
		valor = cotacaoAtivo.getValorFechamento();
		volatilidade = cotacaoAtivo.getValorVolatilidadeAnual();
	}
	
	@Override
	public BigDecimal calculaMediaMovel(Integer periodo) throws BancoDadosEX {
		try {
			return calculaMediaMovel(dia, periodo, acao);
		} catch (SQLException e) {
			throw new BancoDadosEX(e);
		}
	}
	
	private CotacaoAtivoTO getCotacaoAcao(Dia dia) throws SATAEX {
		CotacaoAtivoTO cotacao;
		int tentativas = 0;
		do {
			cotacao = getCotacaoAtivo(dia);
			if (cotacao == null)
				dia = dia.getProximoDia();
		} while (cotacao == null && tentativas++<20);
		if (cotacao == null)
			throw new CotacaoInexistenteEX();
		return cotacao;
	}
	
	private CotacaoAtivoTO getCotacaoAtivo(Dia dia) throws SATAEX {
		DiaCotacao diaCotacao = new DiaCotacao(dia, acao);
		if (!cacheCotacoes.containsKey(diaCotacao)) {
			try {
				ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
				CotacaoAtivoTO cotacao = caDAO.getCotacaoDoAtivo(acao.getNome(), dia.formatoBanco());
				cacheCotacoes.put(diaCotacao, cotacao);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BancoDadosEX(e);
			}
		}
		return cacheCotacoes.get(diaCotacao);
	}
	
	private static BigDecimal calculaMediaMovel(Dia dia, int periodo, Acao acao) throws SQLException {
		if (periodo == 0) return BigDecimal.ZERO;
		DiaCotacao diaCotacao = new DiaCotacao(dia, acao);
		if (!cacheMM.containsKey(diaCotacao)) {
				List<CotacaoAtivoTO> cotacoes = getListaCotacoesAteAData(dia, periodo, acao);
				BigDecimal mm = BigDecimal.ZERO;
				if (cotacoes.size() == periodo) {
					BigDecimal soma = BigDecimal.ZERO;
					for (CotacaoAtivoTO cotacao: cotacoes) {
						soma = soma.add(cotacao.getValorFechamento());
					}
					mm = soma.divide(new BigDecimal(periodo), RoundingMode.HALF_EVEN);
				}
				cacheMM.put(diaCotacao, mm);
		}
		return cacheMM.get(diaCotacao);
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAteAData (Dia diaFinal, int qtdDias, Acao acao) throws SQLException {
		List<CotacaoAtivoTO> cotacoes = getListaCotacoesAcaoPeriodo(acao, diaFinal.getDiaAnterior(qtdDias*2), diaFinal);
		while(cotacoes.size() > qtdDias) {
			cotacoes.remove(0);
		}
		return cotacoes;
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcaoPeriodo(Acao acao, Dia diaInicial, Dia diaFinal) throws SQLException {
		return getListaCotacoesAcaoPeriodo(acao, new Periodo(diaInicial, diaFinal));
	}
	
	private static List<CotacaoAtivoTO> getListaCotacoesAcaoPeriodo(Acao acao, Periodo periodo) throws SQLException {
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		return caDAO.getCotacoesDoAtivo(acao.getNome(), periodo.getDiaInicial().formatoBanco(), periodo.getDiaFinal().formatoBanco());
	}
	
	@Override
	public String toString() {
		return SATAUtil.getMessage(MSG_PATTERN_PRECO_ACAO, 
				dia.toString(), 
				SATAUtil.formataNumero(valor), 
				SATAUtil.formataNumero(volatilidade.multiply(CEM)));
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(10,27).
		   appendSuper(8).
	       append(acao).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
