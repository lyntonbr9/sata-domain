package sata.domain.simulacao.to;

public class IndicadoSimulacaoAltaVarPoucoTempoTO {
	
	// indice na lista de ativos
	private int indice;
	// indica se eh de alta
	private boolean isAlta;
	// indica se eh baixa
	private boolean isBaixa;
	// indica se eh a saida de operacao de alta
	private boolean isEntradaAlta;
	// indica se eh a saida de operacao de alta
	private boolean isSaidaAlta;
	// indica se eh a saida de operacao de alta
	private boolean isNaoEntrarAlta;
	
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public boolean isAlta() {
		return isAlta;
	}
	public void setAlta(boolean isAlta) {
		this.isAlta = isAlta;
	}
	public boolean isBaixa() {
		return isBaixa;
	}
	public void setBaixa(boolean isBaixa) {
		this.isBaixa = isBaixa;
	}
	public boolean isEntradaAlta() {
		return isEntradaAlta;
	}
	public void setEntradaAlta(boolean isEntradaAlta) {
		this.isEntradaAlta = isEntradaAlta;
	}
	public boolean isSaidaAlta() {
		return isSaidaAlta;
	}
	public void setSaidaAlta(boolean isSaidaAlta) {
		this.isSaidaAlta = isSaidaAlta;
	}
	public boolean isNaoEntrarAlta() {
		return isNaoEntrarAlta;
	}
	public void setNaoEntrarAlta(boolean isNaoEntrarAlta) {
		this.isNaoEntrarAlta = isNaoEntrarAlta;
	}

}
