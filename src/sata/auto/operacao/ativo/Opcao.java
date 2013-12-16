package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;

public abstract class Opcao extends Derivado {
	
	Integer ordem;
	boolean volatil;
	
	abstract boolean isCall();
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws SATAEX {
		return new PrecoOpcao(isCall(), this, operacao.getDiasParaVencimento(dia), dia, operacao.getPrecoExercicioOpcao(dia, this));
	}
	
	public Integer getOrdem(BigDecimal volatilidade) {
		if (volatil && ordem > 0) {
			int ordemVolatil = (int) Math.round(volatilidade.doubleValue() * 10);
			return ordem+ordemVolatil-1;
		}
		return ordem;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + ordem + ")";
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(acao).
	       append(ordem).
	       append(volatil).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public boolean isVolatil() {
		return volatil;
	}
	public void setVolatil(boolean volatil) {
		this.volatil = volatil;
	}
	
	//se call - true, se put - false
	public static boolean getTipoOpcao(String codigoOpcao){
		//recupera a serie da opcao
		String serieOpcao = String.valueOf(codigoOpcao.charAt(4));
		
		//transforma as series de call disponiveis
		String seriesCallDisponiveis =  String.valueOf(IConstants.SERIES_CALL_CHAR);
		
		//se existe nas series possiveis de CALL retorna true
		return seriesCallDisponiveis.contains(serieOpcao);
	}
}
