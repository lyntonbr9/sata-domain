package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;
import sata.metastock.util.BlackScholes;

public class PrecoOpcao extends Preco implements IConstants {
	
	boolean call;
	Opcao opcao;
	int diasParaVencimento;
	BigDecimal precoAcao;
	BigDecimal precoExercicioOpcao;
	
	public PrecoOpcao() {}
	
	public PrecoOpcao(boolean call, Opcao opcao,
			int diasParaVencimento, Dia dia, BigDecimal precoExercicioOpcao) {
		this.call = call;
		this.opcao = opcao;
		this.diasParaVencimento = diasParaVencimento;
		this.dia = dia;
		this.precoExercicioOpcao = precoExercicioOpcao;
	}

	@Override
	public void calculaPreco() throws SATAEX {
		valor = blackScholes();
	}
	
	@Override
	public BigDecimal calculaMediaMovel(Integer periodo) throws SATAEX {
		return opcao.getAcao().getMediaMovel(dia, periodo);
	}
	
	public static BigDecimal blackScholes(boolean call, BigDecimal precoAcao, BigDecimal precoExercicioOpcao, 
			int diasParaVencimento, BigDecimal volatilidade, BigDecimal taxaJuros) {
		double tempoParaVencimentoOpcaoEmAnos = 0.000000000000000000000000001;
		if (diasParaVencimento > 0)
			tempoParaVencimentoOpcaoEmAnos = BlackScholes.getQtdDiasEmAnos(diasParaVencimento);
		double valor = BlackScholes.blackScholes(call, precoAcao.doubleValue(), precoExercicioOpcao.doubleValue(), tempoParaVencimentoOpcaoEmAnos, taxaJuros.doubleValue(), volatilidade.doubleValue());
		return new BigDecimal(valor); 
	}
	
	public static BigDecimal calculaVolatilidade(boolean call, BigDecimal precoAcao, BigDecimal precoExercicioOpcao, 
			int diasParaVencimento, BigDecimal precoOpcao, BigDecimal taxaJuros) {
		double menorDiferenca = Double.POSITIVE_INFINITY;
		BigDecimal volatilidadeFinal = BigDecimal.ZERO;
		for (double volatilidade = 0; volatilidade < 1; volatilidade += 0.01) {
			BigDecimal bs = blackScholes(call, precoAcao, precoExercicioOpcao, diasParaVencimento, new BigDecimal(volatilidade), taxaJuros);
			double diferenca = bs.subtract(precoOpcao).divide(precoOpcao, RoundingMode.HALF_EVEN).abs().doubleValue();
			if (diferenca < menorDiferenca) {
				menorDiferenca = diferenca;
				volatilidadeFinal = new BigDecimal(volatilidade);
			}
		} 
		return volatilidadeFinal;
	}

	private BigDecimal blackScholes() throws SATAEX {
		double precoAcao = calculaPrecoAcao().doubleValue();
		double precoExercicioOpcao = this.precoExercicioOpcao.doubleValue();
		double tempoParaVencimentoOpcaoEmAnos = BlackScholes.getQtdDiasEmAnos(diasParaVencimento);
		double taxaDeJuros = SATAUtil.getTaxaDeJuros(dia.getAno());
		double volatilidade = getVolatilidadeAcao().doubleValue();
		double valor = BlackScholes.blackScholes(call, precoAcao, precoExercicioOpcao, tempoParaVencimentoOpcaoEmAnos, taxaDeJuros, volatilidade);
		return new BigDecimal(valor);
	}
	
	private BigDecimal calculaPrecoAcao() throws SATAEX {
		precoAcao = opcao.getAcao().getPreco(dia);
		volatilidade = opcao.getAcao().getVolatilidade(dia);
		return precoAcao;
	}
	
	private BigDecimal getVolatilidadeAcao() throws SATAEX {
		return opcao.getAcao().getVolatilidade(dia);
	}
	
	@Override
	public String toString() {
		String opcao = "Call";
		if (!call) opcao = "Put";
		return SATAUtil.getMessage(MSG_PATTERN_PRECO_OPCAO, 
				opcao, 
				SATAUtil.formataNumero(precoExercicioOpcao), 
				SATAUtil.formataNumero(precoAcao),
				dia.toString(),
				SATAUtil.formataNumero(valor),
				String.valueOf(diasParaVencimento));
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(1,27).
		   appendSuper(7).
	       append(call).
	       append(opcao).
	       append(diasParaVencimento).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public boolean isCall() {
		return call;
	}
	public void setCall(boolean call) {
		this.call = call;
	}
	public Opcao getOpcao() {
		return opcao;
	}
	public void setOpcao(Opcao opcao) {
		this.opcao = opcao;
	}
	public int getDiasParaVencimento() {
		return diasParaVencimento;
	}
	public void setDiasParaVencimento(int diasParaVencimento) {
		this.diasParaVencimento = diasParaVencimento;
	}
	public BigDecimal getPrecoExercicioOpcao() {
		return precoExercicioOpcao;
	}
	public void setPrecoExercicioOpcao(BigDecimal precoExercicioOpcao) {
		this.precoExercicioOpcao = precoExercicioOpcao;
	}
	public BigDecimal getPrecoAcao() {
		return precoAcao;
	}
	public void setPrecoAcao(BigDecimal precoAcao) {
		this.precoAcao = precoAcao;
	}
}
