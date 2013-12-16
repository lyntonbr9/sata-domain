package sata.auto.exception;

public class BancoDadosEX extends SATAEX {

	private static final long serialVersionUID = -6883243700413487645L;

	public BancoDadosEX() {
		super("Erro de acesso ao banco de dados!");
	}
	
	public BancoDadosEX(String msg){
		super(msg);
	}
	
	public BancoDadosEX(Throwable e){
		super(e);
	}
}
