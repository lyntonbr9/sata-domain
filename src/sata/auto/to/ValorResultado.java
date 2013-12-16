package sata.auto.to;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.domain.util.SATAUtil;

public class ValorResultado {
	
	String label;
	List<BigDecimal> valores;
	
	public ValorResultado() {}
	
	public ValorResultado(String label, List<BigDecimal> valores) {
		this.label = label;
		this.valores = valores;
	}

	public ValorResultado(String label, BigDecimal... valores) {
		this.label = label;
		this.valores = Arrays.asList(valores);
	}
	
	public boolean isEmpty() {
		return label == null && valores == null;
	}
	
	@Override
	public String toString() {
		String string = label + " = ";
		for (BigDecimal valor : valores)
			string += SATAUtil.formataNumero(valor) + "; ";
		return string;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(14,30).
	       append(label).
	       append(valores).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<BigDecimal> getValores() {
		return valores;
	}

	public void setValores(List<BigDecimal> valores) {
		this.valores = valores;
	}
}
