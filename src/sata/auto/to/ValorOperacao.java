package sata.auto.to;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Derivado;
import sata.auto.operacao.ativo.preco.Preco;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class ValorOperacao implements Comparable<ValorOperacao>, IConstants {
	
	private Operacao operacao;
	private Mes mes;
	private Preco preco;
	
	public ValorOperacao() {};
	
	public ValorOperacao (Operacao operacao, Mes mes, Preco preco) {
		this.operacao = operacao;
		this.mes = mes;
		this.preco = preco;
	}
	
	public BigDecimal getValor() {
		BigDecimal valor = preco.getValor().multiply(new BigDecimal(operacao.getQtdLotes()));
		if (operacao instanceof Compra)
			valor = valor.negate();
		return valor;
	}
	
	public BigDecimal getPrecoAcao() throws SATAEX {
		if (operacao.getAtivo() instanceof Acao)
			return preco.getValor();
		else if (operacao.getAtivo() instanceof Derivado)
			return ((Derivado)operacao.getAtivo()).getAcao().getPreco(preco.getDia());
		return BigDecimal.ZERO;
	}
	
	@Override
	public int compareTo(ValorOperacao other) {
		return new CompareToBuilder()
	       .append(mes, other.mes)
	       .append(operacao.getMomento(), other.operacao.getMomento())
	       .toComparison();
	}
	
	@Override
	public String toString() {
		return mes + " - " + operacao + " = " + SATAUtil.formataNumero(getValor()) + " [" + preco + "] " ;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(operacao).
	       append(mes).
	       append(preco).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
	public Preco getPreco() {
		return preco;
	}
	public void setPreco(Preco preco) {
		this.preco = preco;
	}
}
