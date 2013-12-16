package sata.auto.exception;

public class SATAEX extends Exception {

	private static final long serialVersionUID = -6883243700413487645L;

	public SATAEX() {
		super("Ocorreu um erro inesperado... Favor nos informar caso o mesmo persista!");
	}
	
	public SATAEX(String msg){
		super(msg);
	}
	
	public SATAEX(Throwable e){
		super(e);
	}
}
