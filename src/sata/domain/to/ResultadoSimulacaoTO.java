package sata.domain.to;

public class ResultadoSimulacaoTO {

	public static final int CANDLE_VERDE = 4;
	public static final int CANDLE_VERMELHA = 3;
	
	private int qtdTotalOperacoes;
	private int qtdOperacoesSucesso;
	private int qtdOperacoesFalha;
	private int qtdOperacoesRiscoStop;
	private int valorTotal;
	private int valorGanho;
	private int valorPerda;
	
	public int getQtdOperacoesRiscoStop() {
		return qtdOperacoesRiscoStop;
	}
	public void setQtdOperacoesRiscoStop(int qtdOperacoesRiscoStop) {
		this.qtdOperacoesRiscoStop = qtdOperacoesRiscoStop;
	}
	public int getQtdTotalOperacoes() {
		return qtdTotalOperacoes;
	}
	public void setQtdTotalOperacoes(int qtdTotalOperacoes) {
		this.qtdTotalOperacoes = qtdTotalOperacoes;
	}
	public int getQtdOperacoesSucesso() {
		return qtdOperacoesSucesso;
	}
	public void setQtdOperacoesSucesso(int qtdOperacoesSucesso) {
		this.qtdOperacoesSucesso = qtdOperacoesSucesso;
	}
	public int getQtdOperacoesFalha() {
		return qtdOperacoesFalha;
	}
	public void setQtdOperacoesFalha(int qtdOperacoesFalha) {
		this.qtdOperacoesFalha = qtdOperacoesFalha;
	}
	public int getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(int valorTotal) {
		this.valorTotal = valorTotal;
	}
	public int getValorGanho() {
		return valorGanho;
	}
	public void setValorGanho(int valorGanho) {
		this.valorGanho = valorGanho;
	}
	public int getValorPerda() {
		return valorPerda;
	}
	public void setValorPerda(int valorPerda) {
		this.valorPerda = valorPerda;
	}
	
}
