package sata.domain.to;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import sata.auto.operacao.ativo.Acao;
import sata.metastock.robos.CotacaoLopesFilho;

@Entity
@Table(name="Opcao")
public class OpcaoTO implements TO {

	@Id @Column
	private String codigo;
	
	@Column
	private BigDecimal precoExercicio;
	
	@ManyToOne
	@JoinColumn(name="acao")
	private Acao acao;
	
	@Column(name="dtVencimento")
	private Date dataVencimento;
	
	@Transient
	private BigDecimal precoAtual;
	
	@Transient
	private BigDecimal blackScholes;
	
	public BigDecimal getPrecoAtual() {
		if (precoAtual == null)
			precoAtual = CotacaoLopesFilho.getCotacao(codigo).setScale(50);
		return precoAtual;
	}
	
	@Override
	public String getId() {
		return codigo;
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public BigDecimal getPrecoExercicio() {
		return precoExercicio;
	}
	public void setPrecoExercicio(BigDecimal precoExercicio) {
		this.precoExercicio = precoExercicio;
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

	public BigDecimal getBlackScholes() {
		return blackScholes;
	}

	public void setBlackScholes(BigDecimal blackScholes) {
		this.blackScholes = blackScholes;
	}
}
