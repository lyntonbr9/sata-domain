package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.to.Dia;
import sata.domain.util.Cache;

public abstract class Preco {
	
	BigDecimal valor;
	BigDecimal volatilidade;
	Dia dia;
	Cache<Integer, BigDecimal> mediasMoveis = new Cache<Integer, BigDecimal>(200);

	public abstract void calculaPreco() throws SATAEX;
	
	public abstract BigDecimal calculaMediaMovel(Integer periodo) throws SATAEX;

	public BigDecimal getMediaMovel(Integer periodo) throws SATAEX {
		if (!mediasMoveis.containsKey(periodo))
			mediasMoveis.put(periodo, calculaMediaMovel(periodo));
		return mediasMoveis.get(periodo);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(10,27).
	       append(valor).
	       append(volatilidade).
	       append(dia).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public BigDecimal getVolatilidade() {
		return volatilidade;
	}
	public void setVolatilidade(BigDecimal volatilidade) {
		this.volatilidade = volatilidade;
	}
	public Dia getDia() {
		return dia;
	}
	public void setDia(Dia dia) {
		this.dia = dia;
	}
}
