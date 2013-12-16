package sata.domain.alert;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class OperacaoDeAlta implements IConstants{
	
	//lista com as quatro candles anteriores
	public boolean analisaFazerOperacao(List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros){
		
		int TAM_CAND_ANT_VERDE = 20;
		int TAM_CAND_ANT_ANT_VERMELHA = 65;
		int DIF_FECH_MAX_ANT = 30;
		int DIF_CAND_VERMELHA_ANT_VERDE = 5;
		int TAM_MAX_CAND_ANT_VERDE = 50;
		
		int fechamentoDiaAnterior;
		int maximaDiaAnterior;
		int minimaDiaAnterior;
		boolean corpoCandleAnteriorVerdeGrande=false;
		boolean fechouPertoDaMaxima=false;
		
		int Candles[] = SATAUtil.getCandles(listaDasCotacoes);
		
		fechamentoDiaAnterior = Integer.parseInt(listaDasCotacoes.get(3).getFechamento());
		maximaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(3).getMaxima());
		minimaDiaAnterior = Integer.parseInt(listaDasCotacoes.get(3).getMinima());

		//if (Candles[3] == CANDLE_VERMELHA){
		if (Candles[3] == CANDLE_VERMELHA || (Math.abs(maximaDiaAnterior - minimaDiaAnterior) > TAM_MAX_CAND_ANT_VERDE) ){
			return false; //se a candle anterior for vermelha abandona a operacao
		}
		else
		{
			if(Candles[3] == CANDLE_VERDE && Candles[2] == CANDLE_VERDE
					&& Candles[1] == CANDLE_VERDE && Candles[0] == CANDLE_VERDE){
				return false; //Se veio de uma grande subida nao faz a operacao
			}
			else{
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
					int tamCandleTercAnter = Integer.parseInt(listaDasCotacoes.get(1).getMaxima()) 
											- Integer.parseInt(listaDasCotacoes.get(1).getMinima());
					int tamCandleQuarAnter = Integer.parseInt(listaDasCotacoes.get(0).getMaxima()) 
											- Integer.parseInt(listaDasCotacoes.get(0).getMinima());

					if (Candles[1] == CANDLE_VERMELHA){
						if (tamCandleTercAnter > TAM_CAND_ANT_ANT_VERMELHA)
							temCandleVermelhaGrande = true;
					}
					if (Candles[0] == CANDLE_VERMELHA){
						if (tamCandleQuarAnter > TAM_CAND_ANT_ANT_VERMELHA)
							temCandleVermelhaGrande = true;
					}
					
					//se nao tiver candle vermelha grande anterior
					if(temCandleVermelhaGrande == false){ 
						//Se a candle anterior a anterior eh verde faz a operacao
						if (Candles[2] == CANDLE_VERDE){
							return true;
						}
						else{
							//Se a candle anterior a anterior eh vermelha
                            //Verifica se o corpo da candle vermelha anterior a anterior eh menor 
							//ou igual a candle verde anterior
							int tamCorpoCandleVermelhaAnterACandleAnter = Math.abs(Integer.parseInt(listaDasCotacoes.get(2).getMaxima())
															- Integer.parseInt(listaDasCotacoes.get(2).getMinima()));
							if((tamCorpoCandleAnteriorVerde - tamCorpoCandleVermelhaAnterACandleAnter) > DIF_CAND_VERMELHA_ANT_VERDE){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
		
	}

}
