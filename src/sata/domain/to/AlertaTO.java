package sata.domain.to;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.domain.util.IConstants;

@Entity
@Table(name="AlertaOperacao")
@Where(clause = "dtExclusao IS NULL")  
@SQLDelete(sql = "UPDATE AlertaOperacao SET dtExclusao = NOW() WHERE id = ?")  
public class AlertaTO implements TO,IConstants {
	
	@Id	@GeneratedValue
	@Column
	private Integer id;
	
	@Column
	private String nome;
	
	@Column
	private Integer porcentagemGanho;
	
	@Column
	private Integer porcentagemPerda;
	
	@Enumerated(EnumType.STRING)
	private TipoCalculoValorInvestido tipoCalculoVI;
	
	@Column
	private Integer percCalculoVI = 100;
	
	@Column
	private boolean ativo;
	
	@OneToMany(mappedBy="alerta")
	@Where(clause = "dtExclusao IS NULL")  
	private List<SerieOperacoesTO> series;
	
	public boolean alertaPorcentagemGanho(BigDecimal porcentagemSerie) {
		BigDecimal percGanho = new BigDecimal(porcentagemGanho);
		return porcentagemSerie.doubleValue() >= percGanho.doubleValue();
	}

	public boolean alertaPorcentagemPerda(BigDecimal porcentagemSerie) {
		BigDecimal percPerda = new BigDecimal(porcentagemPerda);
		return porcentagemSerie.doubleValue() <= percPerda.negate().doubleValue();
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getPorcentagemGanho() {
		return porcentagemGanho;
	}
	public void setPorcentagemGanho(Integer porcentagemGanho) {
		this.porcentagemGanho = porcentagemGanho;
	}
	public Integer getPorcentagemPerda() {
		return porcentagemPerda;
	}
	public void setPorcentagemPerda(Integer porcentagemPerda) {
		this.porcentagemPerda = porcentagemPerda;
	}
	public TipoCalculoValorInvestido getTipoCalculoVI() {
		return tipoCalculoVI;
	}
	public void setTipoCalculoVI(TipoCalculoValorInvestido tipoCalculoVI) {
		this.tipoCalculoVI = tipoCalculoVI;
	}
	public Integer getPercCalculoVI() {
		return percCalculoVI;
	}
	public void setPercCalculoVI(Integer percCalculoVI) {
		this.percCalculoVI = percCalculoVI;
	}
	public List<SerieOperacoesTO> getSeries() {
		return series;
	}
	public void setSeries(List<SerieOperacoesTO> series) {
		this.series = series;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
