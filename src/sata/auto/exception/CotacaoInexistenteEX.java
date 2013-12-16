package sata.auto.exception;

public class CotacaoInexistenteEX extends SATAEX {

	private static final long serialVersionUID = -2960190940395814091L;
	
	public CotacaoInexistenteEX() {
		super("Cota��o n�o existente!");
	}
	
	public CotacaoInexistenteEX(String msg){
		super(msg);
	}
}
