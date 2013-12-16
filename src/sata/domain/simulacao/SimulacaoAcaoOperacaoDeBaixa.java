package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;

@Deprecated
public class SimulacaoAcaoOperacaoDeBaixa implements ISimulacao, IConstants{

	//TODO: Ver o algoritmo parece meio ruim para operacao de baixa
	//public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, int stopGain, int stopLoss, double probabilidadeStopLoss) {
	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		//definicao dos parametros
		int stopGain = (Integer) parametros[0];
		
		ResultadoSimulacaoTO resultado = new ResultadoSimulacaoTO();
		
		int abertura;
		int minima;
		int fechamento;
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
		
		//boolean fazerOperacao=false;
		int Candles[] = new int[listaDasCotacoes.size()];
		
		//seta as candles
		for (int i=1; i < listaDasCotacoes.size(); i++  )
		{
			abertura = Integer.parseInt(listaDasCotacoes.get(i).getAbertura());
			fechamento = Integer.parseInt(listaDasCotacoes.get(i).getFechamento());
			Candles[i-1] = (fechamento > abertura) ? CANDLE_VERDE : CANDLE_VERMELHA; 
		}
		
		int VALOR_GAP_ABERT_FECH = 15;
		int TAM_CAND_ANT_VERM = 20;
		int DIF_FECH_MIN_ANT = 30;
		int DIF_CAND_VERD_ANT_VERM = 5;
		boolean corpoCandleAnteriorVermelhaGrande;
		boolean fechouPertoDaMinima;
		
		for (int i=1; i < listaDasCotacoes.size(); i++  ){
			abertura = Integer.parseInt(listaDasCotacoes.get(i).getAbertura());
			minima = Integer.parseInt(listaDasCotacoes.get(i).getMinima());
			fechamento = Integer.parseInt(listaDasCotacoes.get(i).getFechamento());
			fechamentoDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getFechamento());
			maximaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getMaxima());
			minimaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(i-1).getMinima());
			
			corpoCandleAnteriorVermelhaGrande=false;
			fechouPertoDaMinima=false;

			if (Candles[i-1] == CANDLE_VERDE){ 
				continue; //se a candle anterior for verde abandona a operacao
			}
			else
			{
				if(i > 6)
				{
					if(Candles[i-1] == CANDLE_VERMELHA && Candles[i-2] == CANDLE_VERMELHA
							&& Candles[i-3] == CANDLE_VERMELHA && Candles[i-4] == CANDLE_VERMELHA){
						i = i + 2; //Se veio de uma grande queda nao faz a operacao e pula 2
						continue;
					}else{
						//Verifica se nao abriu com grande gap em relacao a candle anterior
						boolean abriuGapCandleAnterior = false;
						if ((abertura - fechamentoDiaAnterior) < (VALOR_GAP_ABERT_FECH * -1))
							abriuGapCandleAnterior = true;
						
						if(abriuGapCandleAnterior == false){
							
							//verifica o tamanho do corpo da candle vermelha anterior
							int tamCorpoCandleAnteriorVermelha = Math.abs(maximaDiaAnterior - minimaDiaAnterior);
							if (tamCorpoCandleAnteriorVermelha > TAM_CAND_ANT_VERM){
								corpoCandleAnteriorVermelhaGrande = true;
							}
							
							//verifica se no dia anterior fechou perto da mínima
							int difFechamentoMinimaCandleAnterior = Math.abs(fechamentoDiaAnterior - minimaDiaAnterior);
							if(difFechamentoMinimaCandleAnterior < DIF_FECH_MIN_ANT){
								fechouPertoDaMinima = true;
							}
							
							if(corpoCandleAnteriorVermelhaGrande && fechouPertoDaMinima){
								//Verifica se não tem nenhuma candle verde com tamanho grande entre as candles anteriores
								boolean temCandleVerdeGrande = false;
								int tamCandleTercAnter = Integer.parseInt(listaDasCotacoes.get(i-3).getMaxima()) 
														- Integer.parseInt(listaDasCotacoes.get(i-3).getMinima());
								int tamCandleQuarAnter = Integer.parseInt(listaDasCotacoes.get(i-4).getMaxima()) 
														- Integer.parseInt(listaDasCotacoes.get(i-4).getMinima());

								if (Candles[i-3] == CANDLE_VERDE){
									if (tamCandleTercAnter > 65)
										temCandleVerdeGrande = true;
								}
								if (Candles[i-4] == CANDLE_VERDE){
									if (tamCandleQuarAnter > 65)
										temCandleVerdeGrande = true;
								}
								
								//se nao tiver candle verde grande anterior
								if(temCandleVerdeGrande == false){ 
									//Se a candle anterior a anterior eh vermelha faz a operacao
									if (Candles[i-2] == CANDLE_VERMELHA){
										//seta o valor
										if ((abertura - stopGain) >= minima){
											valorGanho = valorGanho + stopGain;
											qtdOperSucesso++;
										}
										else{
											valorPerda = valorPerda + (fechamento - abertura);
											qtdOperFalha++;
										}
										contadorOperSeguidas++;
									}else{
										//Se a candle anterior a anterior eh verde
                                        //Verifica se o corpo da candle verde anterior a anterior eh menor 
										//ou igual a candle vermelha anterior
										int tamCorpoCandleVerdeAnterACandleAnter = Math.abs(Integer.parseInt(listaDasCotacoes.get(i-2).getMaxima())
																		- Integer.parseInt(listaDasCotacoes.get(i-2).getMinima()));
										if((tamCorpoCandleAnteriorVermelha - tamCorpoCandleVerdeAnterACandleAnter) > DIF_CAND_VERD_ANT_VERM){
											//seta o valor
											if ((abertura - stopGain) >= minima){
												valorGanho = valorGanho + stopGain;
												qtdOperSucesso++;
											}
											else{
												valorPerda = valorPerda + (fechamento - abertura);
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
				}else
					i++;
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
