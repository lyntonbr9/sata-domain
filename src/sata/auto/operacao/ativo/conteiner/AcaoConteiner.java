package sata.auto.operacao.ativo.conteiner;

import java.util.ArrayList;
import java.util.List;

import sata.auto.operacao.ativo.Acao;
import sata.domain.dao.SATAFactoryFacade;

public class AcaoConteiner {
	
	private static List<Acao> acoes = new ArrayList<Acao>();
	
	static {
		try {
			acoes = SATAFactoryFacade.getAcaoDAO().listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Acao get(String codigoAcao) {
		for (Acao acao: acoes)
			if (acao.getNome().equals(codigoAcao))
				return acao;
		return null;
	}

	public static List<Acao> getAcoes() {
		return acoes;
	}
}
