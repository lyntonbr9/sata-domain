package sata.auto.to;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.operacao.ativo.Acao;

public class AnoCotacao {
	
	Integer ano;
	Acao acao;
	
	public AnoCotacao() {}
	
	public AnoCotacao(Integer ano, Acao acao) {
		this.ano = ano;
		this.acao = acao;
	}
	
	@Override
	public String toString() {
		return acao + "/" + ano;
	}
	
	@Override
	public int hashCode() {
		 return new HashCodeBuilder(17,27).
	       append(ano).
	       append(acao).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
