package sata.auto.operacao;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.exception.SATAEX;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoAcao;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.domain.util.SATAUtil;


public class Condicao {
	
	Atributo atributo;
	Operador operacao;
	double valor;
	
	public Condicao() {}
	
	public Condicao(Atributo atributo, Operador operacao, double valor) {
		this.atributo = atributo;
		this.operacao = operacao;
		this.valor = valor;
	}
	
	public boolean verdadeira(Preco preco) throws SATAEX {
		double valorComparacao = 0;
		
		switch (atributo) {
		case PRECO:
			valorComparacao = preco.getValor().doubleValue();
			break;
		case VOLATILIDADE:
			valorComparacao = preco.getVolatilidade().doubleValue();
			break;
		case MEDIA_MOVEL:
			valorComparacao = preco.getMediaMovel((int)valor).doubleValue();
			double precoAcao = 0;
			if (preco instanceof PrecoAcao)
				precoAcao = preco.getValor().doubleValue();
			else if (preco instanceof PrecoOpcao)
				precoAcao = ((PrecoOpcao)preco).getPrecoAcao().doubleValue();
			return verdadeira(valorComparacao, precoAcao, operacao);
		}
		
		return verdadeira(valor, valorComparacao, operacao);
	}
	
	public static boolean verdadeira(double valor, double valorComparacao, Operador operador) {
		switch (operador) {
		case IGUAL:
			return valorComparacao == valor;
			
		case DIFERENTE:
			return valorComparacao != valor;
			
		case MAIOR:
			return valorComparacao > valor;
			
		case MENOR:
			return valorComparacao < valor;
			
		case MAIOR_IGUAL:
			return valorComparacao >= valor;
			
		case MENOR_IGUAL:
			return valorComparacao <= valor;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return atributo.getLabel() + " " + operacao.getSimbol() + " " + SATAUtil.formataNumero(new BigDecimal(valor));
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,28).
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
