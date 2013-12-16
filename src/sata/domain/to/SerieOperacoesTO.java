package sata.domain.to;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
@Table(name="SerieOperacoes")
@SQLDelete(sql = "UPDATE SerieOperacoes SET dtExclusao = NOW() WHERE id = ?")  
public class SerieOperacoesTO implements TO {

	@Id	@GeneratedValue
	@Column
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_alerta")
	private AlertaTO alerta;
	
	@ManyToOne
	@JoinColumn(name="id_investidor")
	private InvestidorTO investidor;
	
	@Column
	private Date dataExecucao;	
	
	@ManyToOne
	@JoinColumn(name="acao")
	private Acao acao;
	
	@Column
	private Date dataVencimento;
	
	@Column
	private Integer qtdLotesAcao;
	
	@Column
	private BigDecimal precoAcao;
	
	@Transient
	private BigDecimal precoAcaoAtual;
	
	@Column(name="ativo")
	private boolean ativa;
	
	@OneToMany(mappedBy="serie")
	@Where(clause = "dtExclusao IS NULL") 
	private List<OperacaoRealizadaTO> operacoes;
	
	public BigDecimal getPrecoAcaoAtual() {
		if (precoAcaoAtual == null)
			precoAcaoAtual = CotacaoLopesFilho.getCotacao(acao.getNome()).setScale(50);
		return precoAcaoAtual;
	}
	
	public boolean isAtiva() {
		return ativa && !isVencida();
	}
	
	public boolean isVencida() {
		if (dataVencimento == null) return false;
		return dataVencimento.compareTo(new Date()) == -1; // Data de vencimento menor que a data atual
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,27).
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
	public AlertaTO getAlerta() {
		return alerta;
	}
	public void setAlerta(AlertaTO alerta) {
		this.alerta = alerta;
	}
	public InvestidorTO getInvestidor() {
		return investidor;
	}
	public void setInvestidor(InvestidorTO investidor) {
		this.investidor = investidor;
	}
	public Date getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	public Integer getQtdLotesAcao() {
		return qtdLotesAcao;
	}
	public void setQtdLotesAcao(Integer qtdLotesAcao) {
		this.qtdLotesAcao = qtdLotesAcao;
	}
	public BigDecimal getPrecoAcao() {
		return precoAcao;
	}
	public void setPrecoAcao(BigDecimal precoAcao) {
		this.precoAcao = precoAcao;
	}
	public List<OperacaoRealizadaTO> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(List<OperacaoRealizadaTO> operacoes) {
		this.operacoes = operacoes;
	}
	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	public void setPrecoAcaoAtual(BigDecimal precoAcaoAtual) {
		this.precoAcaoAtual = precoAcaoAtual;
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
}
