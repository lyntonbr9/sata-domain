package sata.domain.to;

public class AtivoTO {
	
	private String codigo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public AtivoTO(String codigo){
		this.codigo = codigo;
	}
}
