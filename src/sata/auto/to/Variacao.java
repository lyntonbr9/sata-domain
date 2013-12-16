package sata.auto.to;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Variacao {
	
	Integer valorInicial;
	Integer valorFinal;
	Integer incremento = 1;
	
	public Variacao(Integer valorInicial, Integer valorFinal) {
		this.valorInicial = valorInicial;
		this.valorFinal = valorFinal;
	}
	
	public Variacao(Integer valorInicial, Integer valorFinal, Integer incremento) {
		this.valorInicial = valorInicial;
		this.valorFinal = valorFinal;
		this.incremento = incremento;
	}
	
	@Override
	public String toString() {
		return valorInicial + "-" + valorFinal + "("+incremento+" em "+incremento+")";
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11,29).
	       append(valorInicial).
	       append(valorFinal).
	       append(incremento).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Integer getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(Integer valorInicial) {
		this.valorInicial = valorInicial;
	}
	public Integer getValorFinal() {
		return valorFinal;
	}
	public void setValorFinal(Integer valorFinal) {
		this.valorFinal = valorFinal;
	}
	public Integer getIncremento() {
		return incremento;
	}
	public void setIncremento(Integer incremento) {
		this.incremento = incremento;
	}
}
