package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

@Deprecated
public class SimulacaoAcaoOperacaoDeAlta implements ISimulacao, IConstants {

	
//	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, int stopGain, int stopLoss, double probabilidadeStopLoss) {
	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		//definicao dos parametros passados
		int stopGain = (Integer) parametros[0];
		int stopLoss = (Integer) parametros[1];
		
		ResultadoSimulacaoTO resultado = new ResultadoSimulacaoTO();
		
		int abertura;
		int fechamentoDiaAnterior;
		int maximaDiaAnterior;
		int minimaDiaAnterior;
		
		int qtdTotalOper=0;
		int qtdOperSucesso=0;
		int qtdOperFalha=0;
		int qtdOperRiscoStop=0;
		int valorGanho=0;
		int valorPerda=0;
		int valorTotal=0;
		int contadorOperSeguidas=0;
		
		int Candles[] = SATAUtil.getCandles(listaDasCotacoes);
		
		int VALOR_GAP_ABERT_FECH = 15;
		int TAM_CAND_ANT_VERDE = 20;
		int DIF_FECH_MAX_ANT = 30;
		int DIF_CAND_VERMELHA_ANT_VERDE = 5;
		int TAM_MAX_CAND_ANT_VERDE = 80;
		
		boolean corpoCandleAnteriorVerdeGrande;
		boolean fechouPertoDaMaxima;
		
		for (int i=1; i < listaDasCotacoes.size(); i++  )
		{
			abertura = Integer.parseInt(listaDasCotacoes.get(i).getAbertura());
			fechamentoDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getFechamento());
			maximaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getMaxima());
			minimaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getMinima());
			
			corpoCandleAnteriorVerdeGrande=false;
			fechouPertoDaMaxima=false;

			if (Candles[i-1] == CANDLE_VERMELHA || (Math.abs(maximaDiaAnterior - minimaDiaAnterior) > TAM_MAX_CAND_ANT_VERDE) ){
			//if (Candles[i-1] == CANDLE_VERMELHA){
				continue; //se a candle anterior for vermelha abandona a operacao
			}
			else
			{
				if(i > 3)
				{
					if(Candles[i-1] == CANDLE_VERDE && Candles[i-2] == CANDLE_VERDE
							&& Candles[i-3] == CANDLE_VERDE && Candles[i-4] == CANDLE_VERDE){
						i = i + 2; //Se veio de uma grande subida nao faz a operacao e pula 2
						continue;
					}else{
						//Verifica se nao abriu com grande gap em relacao a candle anterior
						boolean abriuGapCandleAnterior = false;
						if ((abertura - fechamentoDiaAnterior) > (VALOR_GAP_ABERT_FECH))
							abriuGapCandleAnterior = true;
						
						if(abriuGapCandleAnterior == false)
						{
							//verifica o tamanho do corpo da candle verde anterior
							int tamCorpoCandleAnteriorVerde = Math.abs(maximaDiaAnterior - minimaDiaAnterior);
							if (tamCorpoCandleAnteriorVerde > TAM_CAND_ANT_VERDE){
								corpoCandleAnteriorVerdeGrande = true;
							}
							
							//verifica se no dia anterior fechou perto da maxima
							int difFechamentoMaximaCandleAnterior = Math.abs(fechamentoDiaAnterior - maximaDiaAnterior);
							if(difFechamentoMaximaCandleAnterior < DIF_FECH_MAX_ANT){
								fechouPertoDaMaxima = true;
							}
							
							if(corpoCandleAnteriorVerdeGrande && fechouPertoDaMaxima){
								//Verifica se não tem nenhuma candle vermelha com tamanho grande entre as candles anteriores
								boolean temCandleVermelhaGrande = false;
								int tamCandleTercAnter = Integer.parseInt(listaDasCotacoes.get(i-3).getMaxima()) 
														- Integer.parseInt(listaDasCotacoes.get(i-3).getMinima());
								int tamCandleQuarAnter = Integer.parseInt(listaDasCotacoes.get(i-4).getMaxima()) 
														- Integer.parseInt(listaDasCotacoes.get(i-4).getMinima());

								if (Candles[i-3] == CANDLE_VERMELHA){
									if (tamCandleTercAnter > 65)
										temCandleVermelhaGrande = true;
								}
								if (Candles[i-4] == CANDLE_VERMELHA){
									if (tamCandleQuarAnter > 65)
										temCandleVermelhaGrande = true;
								}
								
								//se nao tiver candle vermelha grande anterior
								if(temCandleVermelhaGrande == false){ 
									//Se a candle anterior a anterior eh verde faz a operacao
									if (Candles[i-2] == CANDLE_VERDE)
									{
										//calcula o valor da operacao
										int valorOperacao = SATAUtil.getValorGanho(listaDasCotacoes.get(i), i, fechamentoDiaAnterior, stopGain, stopLoss);
										
										if (valorOperacao > 0){
											valorGanho = valorGanho + valorOperacao;
											qtdOperSucesso++;
										}
										else{
											valorPerda = valorPerda + valorOperacao;
											qtdOperFalha++;
										}
										contadorOperSeguidas++;
										
									}else{
										//Se a candle anterior a anterior eh vermelha
                                        //Verifica se o corpo da candle vermelha anterior a anterior eh menor 
										//ou igual a candle verde anterior
										int tamCorpoCandleVermelhaAnterACandleAnter = Math.abs(Integer.parseInt(listaDasCotacoes.get(i-2).getMaxima())
																		- Integer.parseInt(listaDasCotacoes.get(i-2).getMinima()));
										
										if((tamCorpoCandleAnteriorVerde - tamCorpoCandleVermelhaAnterACandleAnter) > DIF_CAND_VERMELHA_ANT_VERDE)
										{
											//calcula o valor da operacao
											int valorOperacao = SATAUtil.getValorGanho(listaDasCotacoes.get(i), i, fechamentoDiaAnterior, stopGain, stopLoss);
											
											if (valorOperacao > 0){
												valorGanho = valorGanho + valorOperacao;
												qtdOperSucesso++;
											}
											else{
												valorPerda = valorPerda + valorOperacao;
												qtdOperFalha++;
											}
											contadorOperSeguidas++;
										}
									}
								}
							}
						}
					}
				}
				if(contadorOperSeguidas == 2){
					i = i + 2;
					contadorOperSeguidas = 0;
				}
			}
		}
		
		qtdTotalOper = qtdOperSucesso + qtdOperFalha;
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

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}

	
}
