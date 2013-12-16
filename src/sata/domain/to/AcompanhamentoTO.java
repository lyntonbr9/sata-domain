package sata.domain.to;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import sata.auto.operacao.ativo.Acao;
import sata.metastock.robos.CotacaoLopesFilho;

@Entity
@Table(name="Acompanhamento")
@Where(clause = "dtExclusao IS NULL")  
@SQLDelete(sql = "UPDATE Acompanhamento SET dtExclusao = NOW() WHERE id = ?")  
public class AcompanhamentoTO implements TO {
	
	@Id @GeneratedValue
	@Column
	private Integer id;
	
	@Column
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="id_investidor")
	private InvestidorTO investidor;
	
	@ManyToOne
	@JoinColumn(name="acao")
	private Acao acao;
	
	@Column(name="dtVencimento")
	private Date dataVencimento;	
	
	@OneToMany(mappedBy="acompanhamento", cascade=CascadeType.ALL)
	private List<AcompOpcaoTO> acompanhamentos;
	
	@Transient
	BigDecimal precoAcaoAtual;
	
	public BigDecimal getPrecoAcaoAtual() {
		if (precoAcaoAtual == null)
			precoAcaoAtual = CotacaoLopesFilho.getCotacao(acao.getNome()).setScale(50);
		return precoAcaoAtual;
	}
	
	public boolean isVencida() {
		if (dataVencimento == null) return false;
		return dataVencimento.compareTo(new Date()) == -1; // Data de vencimento menor que a data atual
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(12,21).
	       append(id).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<AcompOpcaoTO> getAcompanhamentos() {
		return acompanhamentos;
	}
	public void setAcompanhamentos(List<AcompOpcaoTO> acompanhamentos) {
		this.acompanhamentos = acompanhamentos;
	}
	public InvestidorTO getInvestidor() {
		return investidor;
	}
	public void setInvestidor(InvestidorTO investidor) {
		this.investidor = investidor;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public void setPrecoAcaoAtual(BigDecimal precoAcaoAtual) {
		this.precoAcaoAtual = precoAcaoAtual;
	}
}
