package sata.auto.operacao.ativo;


public class Put extends Opcao {
	
	public Put() {}
	
	public Put(Acao acao, Integer ordem) {
		this.acao = acao;
		this.ordem = ordem;
	}
	
	public Put(Acao acao, Integer ordem, boolean volatil) {
		this.acao = acao;
		this.ordem = ordem;
		this.volatil = volatil;
	}

	@Override
	boolean isCall() {
		return false;
	}
	
	@Override
	public String getBundleMessage() {
		return "list.ativo.put";
	}
}
