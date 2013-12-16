package sata.auto.to;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class Mes implements Comparable<Mes>{
	private Integer mes;
	private Integer ano;
	
	public Mes() {}
	
	public Mes(Integer mes, Integer ano) {
		this.mes = mes;
		this.ano = ano;
	}
	
	public Mes getMesAnterior() {
		return getMesAnterior(1);
	}
	
	public Mes getMesAnterior(int qtd) {
		if (mes > qtd)
			return new Mes(mes-qtd, ano);
		else
			return new Mes(12-(mes-qtd), ano-1);
	}
	
	public Mes getMesPosterior() {
		if (mes < 12)
			return new Mes(mes+1, ano);
		else
			return new Mes(1, ano+1);
	}
	
	public Dia getDia(Integer dia) {
		return new Dia (dia, this);
	}
	
	public Dia getDiaInicial() {
		return getDia(1);
	}
	
	public Dia getDiaFinal() {
		return getDia(31);
	}
	
	@Override
	public String toString() {
		return getMesFormatado() + "/" + ano;
	}
	
	@Override
	public int compareTo(Mes other) {
		 return new CompareToBuilder()
	        .append(ano, other.ano)
	        .append(mes, other.mes)
	        .toComparison();
	}
	
	private String getMesFormatado() {
		return String.format("%02d", mes);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(ano).
	       append(mes).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
}
