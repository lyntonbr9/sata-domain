package sata.domain.util;

import java.sql.SQLException;
import java.util.List;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;

public class SATAAjustarCotacoes {
	
	
	public static void main(String[] args) throws SQLException {
		
		ajustaCotacaoPETR4();
	}
	
	public static void ajustaCotacaoPETR4() throws SQLException
	{
		//PETR4 teve split dividido por 2 em 25/04/2008
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		//atualizacao da cotacao na PETR4 por causa do split
//		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", "20060101", "20071228");
//		for(CotacaoAtivoTO caTO : listaCotacoesAcao)
//		{
//			int aberturaAtualizado = Integer.valueOf(caTO.getAbertura()) / 2;
//			int fechamentoAtualizado = Integer.valueOf(caTO.getFechamento()) / 2;
//			int maximaAtualizado = Integer.valueOf(caTO.getMaxima()) / 2;
//			int minimaAtualizado = Integer.valueOf(caTO.getMinima()) / 2;
//			System.out.println(caTO.getCodigo() + ": " + " F: " + caTO.getFechamento() + " Data: " + caTO.getPeriodo() + " novaCotacao: " + fechamentoAtualizado);
//			caTO.setAbertura(String.valueOf(aberturaAtualizado));
//			caTO.setFechamento(String.valueOf(fechamentoAtualizado));
//			caTO.setMaxima(String.valueOf(maximaAtualizado));
//			caTO.setMinima(String.valueOf(minimaAtualizado));
//			caDAO.updateCotacaoDoAtivo(caTO);
//		}
		
//		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", "20000623", "20050831");
//		for(CotacaoAtivoTO caTO : listaCotacoesAcao)
//		{
//			int aberturaAtualizado = Integer.valueOf(caTO.getAbertura()) / 4;
//			int fechamentoAtualizado = Integer.valueOf(caTO.getFechamento()) / 4;
//			int maximaAtualizado = Integer.valueOf(caTO.getMaxima()) / 4;
//			int minimaAtualizado = Integer.valueOf(caTO.getMinima()) / 4;
//			System.out.println(caTO.getCodigo() + ": " + " F: " + caTO.getFechamento() + " Data: " + caTO.getPeriodo() + " novaCotacao: " + fechamentoAtualizado);
//			caTO.setAbertura(String.valueOf(aberturaAtualizado));
//			caTO.setFechamento(String.valueOf(fechamentoAtualizado));
//			caTO.setMaxima(String.valueOf(maximaAtualizado));
//			caTO.setMinima(String.valueOf(minimaAtualizado));
//			caDAO.updateCotacaoDoAtivo(caTO);
//		}
		
		List<CotacaoAtivoTO> listaCotacoesAcao = caDAO.getCotacoesDoAtivo("PETR4", "19980316", "20000621");
		for(CotacaoAtivoTO caTO : listaCotacoesAcao)
		{
			int aberturaAtualizado = Integer.valueOf(caTO.getAbertura()) / 40;
			int fechamentoAtualizado = Integer.valueOf(caTO.getFechamento()) / 40;
			int maximaAtualizado = Integer.valueOf(caTO.getMaxima()) / 40;
			int minimaAtualizado = Integer.valueOf(caTO.getMinima()) / 40;
			System.out.println(caTO.getCodigo() + ": " + " F: " + caTO.getFechamento() + " Data: " + caTO.getPeriodo() + " novaCotacao: " + fechamentoAtualizado);
			caTO.setAbertura(String.valueOf(aberturaAtualizado));
			caTO.setFechamento(String.valueOf(fechamentoAtualizado));
			caTO.setMaxima(String.valueOf(maximaAtualizado));
			caTO.setMinima(String.valueOf(minimaAtualizado));
			caDAO.updateCotacaoDoAtivo(caTO);
		}
	}

}
