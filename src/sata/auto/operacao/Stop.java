package sata.auto.operacao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.simulacao.Resultado;
import sata.auto.to.Mes;


public class Stop {
	
	Atributo atributo;
	Operador operacao;
	double valor;
	
	public Stop() {}
	
	public Stop(Atributo atributo, Operador operacao, double valor) {
		this.atributo = atributo;
		this.operacao = operacao;
		this.valor = valor;
	}

	public boolean stop(Mes mes, Resultado resultado) {
		double valorComparacao = 0;
		switch (atributo) {
		case PERCENTUAL_OPERACAO:
			valorComparacao = resultado.getResultadoPercentualMensal(mes).doubleValue();
			break;
		case PERCENTUAL_ACAO:
			valorComparacao = resultado.getResultadoPercentualMensalAcao(mes).doubleValue();
			break;
		}
		return Condicao.verdadeira(valor, valorComparacao, operacao);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1,22).
	       append(atributo).
	       append(operacao).
	       append(valor).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public Atributo getAtributo() {
		return atributo;
	}
	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}
	public Operador getOperacao() {
		return operacao;
	}
	public void setOperacao(Operador operacao) {
		this.operacao = operacao;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
}
