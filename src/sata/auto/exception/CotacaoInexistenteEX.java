package sata.auto.exception;

public class CotacaoInexistenteEX extends SATAEX {

	private static final long serialVersionUID = -2960190940395814091L;
	
	public CotacaoInexistenteEX() {
		super("Cotação não existente!");
	}
	
	public CotacaoInexistenteEX(String msg){
		super(msg);
	}
}
