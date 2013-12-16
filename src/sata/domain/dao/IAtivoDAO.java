package sata.domain.dao;

import java.util.List;

import sata.domain.to.AtivoTO;

public interface IAtivoDAO {
	
	public AtivoTO getAtivo(String codigo);
	public List<String> getCodigosAtivos();
	public List<String> getCodigosOpcoesLiquidas(String ano);
	
}
