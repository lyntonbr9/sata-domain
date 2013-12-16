package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public interface ISimulacao {
		
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop);
	//public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, int stopGain, int stopLoss, double probabilidadeStopLoss);
	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros);
	
}
