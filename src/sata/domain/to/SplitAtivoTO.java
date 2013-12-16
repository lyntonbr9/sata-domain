package sata.domain.to;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name="SplitAtivo")
public class SplitAtivoTO implements TO, Serializable {
	private static final long serialVersionUID = 5939301860593681755L;
	
	@Id @Column
	private String codigoAtivo;
	
	@Id @Column
	private Date periodo;
	
	@Column
	private Integer split;

	@Override
	public String getId() {
		return codigoAtivo + "-" + periodo;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,27).
	       append(codigoAtivo).
	       append(periodo).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getCodigoAtivo() {
		return codigoAtivo;
	}
	public void setCodigoAtivo(String codigoAtivo) {
		this.codigoAtivo = codigoAtivo;
	}
	public Date getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}
	public Integer getSplit() {
		return split;
	}
	public void setSplit(Integer split) {
		this.split = split;
	}
}
