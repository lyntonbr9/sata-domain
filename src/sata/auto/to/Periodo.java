package sata.auto.to;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Periodo {
	
	Dia diaInicial;
	Dia diaFinal;
	
	public Periodo() {}
	
	public Periodo(Dia diaInicial, Dia diaFinal) {
		this.diaInicial = diaInicial;
		this.diaFinal = diaFinal;
	}

	@Override
	public String toString() {
		return diaInicial.formatoPadrao() + " - " + diaFinal.formatoPadrao();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(13,27).
	       append(diaInicial).
	       append(diaFinal).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public Dia getDiaInicial() {
		return diaInicial;
	}
	public void setDiaInicial(Dia diaInicial) {
		this.diaInicial = diaInicial;
	}
	public Dia getDiaFinal() {
		return diaFinal;
	}
	public void setDiaFinal(Dia diaFinal) {
		this.diaFinal = diaFinal;
	}
}
