package sata.auto.operacao.ativo;



public class Call extends Opcao {
	
	public Call() {}
	
	public Call(Acao acao, Integer ordem) {
		this.acao = acao;
		this.ordem = ordem;
	}
	
	public Call(Acao acao, Integer ordem, boolean volatil) {
		this.acao = acao;
		this.ordem = ordem;
		this.volatil = volatil;
	}

	@Override
	boolean isCall() {
		return true;
	}
	
	@Override
	public String getBundleMessage() {
		return "list.ativo.call";
	}
}
