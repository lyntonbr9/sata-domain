package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

@Deprecated
public class SimulacaoAcaoOperacaoDeAltaBasico implements ISimulacao{
	
	private int stopGain;
	private int stopLoss;
	private int qtdTotalOperacoesRiscoStop = -1;
	private double probabilidadeStopGain;
	private double probabilidadeStopLoss;

	private List<CotacaoAtivoTO> listaCotacoes;

	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		this.qtdTotalOperacoesRiscoStop = qtdTotalOperacoesRiscoStop;
	}	
	
	public void setQtdTotalOperacoesRiscoStop(List<CotacaoAtivoTO> listaDasCotacoes) {
		
		int aberturaDiaAnterior=0;
		int fechamentoDiaAnterior=0;
		int aberturaDoisDiasAntes=0;
		int fechamentoDoisDiasAntes=0;
		int maxima=0;
		int minima=0;
		int qtdOperRiscoStop=0;
		boolean fazerOperacao;
		
		for (int i=2; i < listaCotacoes.size(); i++  ){
			aberturaDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getAbertura());
			fechamentoDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getFechamento());
			aberturaDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getAbertura());
			fechamentoDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getFechamento());
			maxima = Integer.parseInt(listaCotacoes.get(i).getMaxima());
			minima = Integer.parseInt(listaCotacoes.get(i).getMinima());
			
			//testa a operacao de alta
			if (fechamentoDiaAnterior > aberturaDiaAnterior 
					&& fechamentoDoisDiasAntes > aberturaDoisDiasAntes)
				fazerOperacao = true;
			else
				fazerOperacao = false;
			
			if (fazerOperacao){
				// os stops estao na candle do proximo dia
				if(((fechamentoDiaAnterior + stopGain) <= maxima) 
					&& ((fechamentoDiaAnterior + stopGain) >= minima)
					&& ((fechamentoDiaAnterior - stopLoss) <= maxima)
					&& ((fechamentoDiaAnterior - stopLoss) >= minima))
				{
					qtdOperRiscoStop++;
				}
			}
		}
		this.qtdTotalOperacoesRiscoStop = qtdOperRiscoStop;
		
	}
	
	//TODO: Continuar esta joça
	//public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, int stopGain, int stopLoss, double probabilidadeStopLoss){
	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros){
		
		//definicao dos parametros
		this.stopGain = (Integer) parametros[0];
		this.stopLoss = (Integer) parametros[1];
		this.probabilidadeStopLoss = (Double) parametros[2];
		this.listaCotacoes = listaDasCotacoes;
		ResultadoSimulacaoTO resultado = new ResultadoSimulacaoTO();
		
		boolean fazerOperacao;
		int aberturaDiaAnterior;
		int fechamentoDiaAnterior;
		int aberturaDoisDiasAntes;
		int fechamentoDoisDiasAntes;
		int maxima;
		int minima;
		int abertura;
		int qtdTotalOper=0;
		int qtdOperSucesso=0;
		int qtdOperFalha=0;
		int qtdOperRiscoStop=0;
		int valorGanho=0;
		int valorPerda=0;
		int valorTotal=0;
		
		//calcula a qtdTotalDeOperacoesDeRisco caso esta nao tenha sido inicializada
		if (this.qtdTotalOperacoesRiscoStop == -1)
			setQtdTotalOperacoesRiscoStop(listaDasCotacoes);
		
		//faz a simulacao		
		for (int i=2; i < listaCotacoes.size(); i++  ){
			aberturaDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getAbertura());
			fechamentoDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getFechamento());
			aberturaDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getAbertura());
			fechamentoDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getFechamento());
			maxima = Integer.parseInt(listaCotacoes.get(i).getMaxima());
			minima = Integer.parseInt(listaCotacoes.get(i).getMinima());
			abertura = Integer.parseInt(listaCotacoes.get(i).getAbertura());
			
			//testa a operacao de alta
			if (fechamentoDiaAnterior > aberturaDiaAnterior 
					&& fechamentoDoisDiasAntes > aberturaDoisDiasAntes)
				fazerOperacao = true;
			else
				fazerOperacao = false;
			
			if (fazerOperacao){
				// os stops estao na candle do proximo dia
				if(((fechamentoDiaAnterior + stopGain) <= maxima) 
					&& ((fechamentoDiaAnterior + stopGain) >= minima)
					&& ((fechamentoDiaAnterior - stopLoss) <= maxima)
					&& ((fechamentoDiaAnterior - stopLoss) >= minima))
				{
					qtdOperRiscoStop++;
					if (qtdOperRiscoStop < ((this.qtdTotalOperacoesRiscoStop*this.probabilidadeStopLoss) + 1))
						valorPerda = valorPerda + ((stopLoss)*-1);
					else
						valorGanho = valorGanho + stopGain;
				}
				else{
					// o stopGain sera disparado
					if(((fechamentoDiaAnterior + stopGain) <= maxima) 
							&& ((fechamentoDiaAnterior + stopGain) >= minima)){
						qtdOperSucesso++;
						valorGanho = valorGanho + stopGain;
					}
					else{
						//abriu com gap para acima do stopGain
						if((fechamentoDiaAnterior + stopGain) <= minima){
							qtdOperSucesso++;
							valorGanho = valorGanho + (abertura - fechamentoDiaAnterior);
						}
						else{
							//quando existe perda na operacao
							qtdOperFalha++;
							if( ((abertura - fechamentoDiaAnterior)*-1) > stopLoss)
								valorPerda = valorPerda + (abertura - fechamentoDiaAnterior);
							else
								valorPerda = valorPerda + ((stopLoss)*-1); 															
						}
					}	
				}
			}			
		}
		qtdTotalOper = qtdOperRiscoStop + qtdOperSucesso + qtdOperFalha;
		valorTotal = valorGanho + valorPerda;
		
		resultado.setQtdOperacoesFalha(qtdOperFalha);
		resultado.setQtdOperacoesRiscoStop(qtdOperRiscoStop);
		resultado.setQtdOperacoesSucesso(qtdOperSucesso);
		resultado.setQtdTotalOperacoes(qtdTotalOper);
		resultado.setValorGanho(valorGanho);
		resultado.setValorPerda(valorPerda);
		resultado.setValorTotal(valorTotal);
		
		return resultado;
	}

	
}
