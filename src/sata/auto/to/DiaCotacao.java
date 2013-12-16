package sata.auto.to;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.operacao.ativo.Acao;

public class DiaCotacao {
	
	Dia dia;
	Acao acao;
	
	public DiaCotacao() {}
	
	public DiaCotacao(Dia dia, Acao acao) {
		this.dia = dia;
		this.acao = acao;
	}

	@Override
	public String toString() {
		return acao + "-" + dia;
	}
	
	public Dia getDia() {
		return dia;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(dia).
	       append(acao).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
