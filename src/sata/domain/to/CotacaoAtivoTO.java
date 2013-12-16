package sata.domain.to;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.CompareToBuilder;

@Entity
@Table(name="CotacaoAtivo")
public class CotacaoAtivoTO implements TO, Comparable<CotacaoAtivoTO>, Serializable {
	private static final long serialVersionUID = -7631873400428727309L;

	@Id @Column(name="codigoAtivo")
	private String codigo;

	@Id @Column
	private String periodo;
	
	@Column
	private String abertura;
	
	@Column
	private String maxima;
	
	@Column
	private String minima;
	
	@Column
	private String fechamento;
	
	@Column
	private String tipoPeriodo;
	
	@Column
	private String ano;
	
	@Column
	private String volume;
	
	@Column
	private double volatilidadeAnual;
	
	@Column
	private double volatilidadeMensal;
	
	@Transient
	private int split;
	
	@Override
	public String getId() {
		return codigo + "-" + periodo;
	}

	public BigDecimal getValorFechamento() {
		return new BigDecimal(Double.parseDouble(fechamento)/(100*split));
	}
	
	public BigDecimal getValorVolatilidadeAnual() {
		return new BigDecimal(volatilidadeAnual);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof CotacaoAtivoTO))
			return false;
		return ((CotacaoAtivoTO)other).codigo.equals(codigo) 
			&& ((CotacaoAtivoTO)other).periodo.equals(periodo);
	}
	
	@Override
	public int compareTo(CotacaoAtivoTO other) {
		return new CompareToBuilder()
	       .append(codigo, other.codigo)
	       .append(periodo, other.periodo)
	       .toComparison();
	}
	
	@Override
	public String toString() {
		return codigo + " " + periodo + " = " + fechamento;
	}
	
	public double getVolatilidadeAnual() {
		return volatilidadeAnual;
	}

	public void setVolatilidadeAnual(double volatilidadeAnual) {
		this.volatilidadeAnual = volatilidadeAnual;
	}

	public double getVolatilidadeMensal() {
		return volatilidadeMensal;
	}

	public void setVolatilidadeMensal(double volatilidadeMensal) {
		this.volatilidadeMensal = volatilidadeMensal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getMaxima() {
		return maxima;
	}

	public void setMaxima(String maxima) {
		this.maxima = maxima;
	}

	public String getMinima() {
		return minima;
	}

	public void setMinima(String minima) {
		this.minima = minima;
	}

	public String getFechamento() {
		return fechamento;
	}

	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}
}
