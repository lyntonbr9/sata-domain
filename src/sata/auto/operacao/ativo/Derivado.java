package sata.auto.operacao.ativo;


public abstract class Derivado extends Ativo {
	
	Acao acao;
	
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
