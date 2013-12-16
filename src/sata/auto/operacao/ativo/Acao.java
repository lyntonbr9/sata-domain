package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoAcao;
import sata.auto.to.Dia;
import sata.domain.to.TO;

@Entity
@Table(name="Acao")
public class Acao extends Ativo implements TO {
	
	@Id @Column(name="codigo")
	String nome;
	
	@Column
	String nomeEmpresa;
	
	@Column
	String tipo;
	
	public Acao() {}
	
	public Acao(String nome, String nomeEmpresa, String tipo) {
		this.nome = nome;
		this.nomeEmpresa = nomeEmpresa;
		this.tipo = tipo;
	}
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws SATAEX {
		return new PrecoAcao(this, dia);
	}
	
	@Override
	public String getBundleMessage() {
		return "list.ativo.acao";
	}
	
	@Override
	public String getId() {
		return nome;
	}

	public BigDecimal getPreco(Dia dia) throws SATAEX {
		return calculaPreco(dia, null).getValor();
	}
	
	public BigDecimal getVolatilidade(Dia dia) throws SATAEX {
		return calculaPreco(dia, null).getVolatilidade();
	}
	
	public BigDecimal getMediaMovel(Dia dia, Integer periodo) throws SATAEX {
		return calculaPreco(dia, null).getMediaMovel(periodo);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(nome).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}