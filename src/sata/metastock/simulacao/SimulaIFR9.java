/*
 * Created on 02/07/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.simulacao;


import java.io.FileInputStream;
import java.math.BigDecimal;

import sata.metastock.data.Values;
import sata.metastock.indices.IFR;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimulaIFR9 {

	public static void main(String args[]){
		FileInputStream file;
		double[] closes = null;
		double[] high = null;
		double[] low = null;
		double[] ifr = null;
		double[] valorCompra1 = null;
		double[] valorCompra2 = null;

		String[] data = null;
		double capital = 12000;
		int taxaCompra = 20;
		int diasIFR = 9;
/*		String[] acoes = {"PETR4_2","PETR4_1","VALE5_1","VALE5_2","USIM5","BBDC4_0","BBDC4_1","BBDC4_2","AMBV4_1","AMBV4_2",
				"CMIG4","ITAU4_2","ITAU4_1","TNLP4","ARCZ6","ACES4_2","ACES4_1"};
	*/
		String[] acoes = {"VIVO4"};

		
		try{
		//	for(int l=3;l<19;l++){
				
				diasIFR = 9;
				System.out.println("IFR: " + diasIFR);
				double totalGeral = 0.0;
				int pos = 0;
				int neg = 0;
				
				for(int k=0;k<acoes.length;k++){
					Values values = new Values(acoes[k] + ".txt");
				
					closes = values.getCloses();
					data = values.getData();
					high = values.getHigh();
					low = values.getLow();
					
					double maiorEficiencia = 0.0;
					double maiorGanhoMedio = 0.0;
					
					IFR calcIFR = new IFR(closes,diasIFR,taxaCompra);
					ifr = calcIFR.getIFR();
					valorCompra1 = calcIFR.getValorCompra1();
					valorCompra2 = calcIFR.getValorCompra2();
					
					boolean[] ptosCompra = new boolean[closes.length];
			        
					TesteIFR.getComprasIFR(ifr,taxaCompra,ptosCompra);
					double ganhoTotal = 0.0;
			        for(int j=0;j<ifr.length;j++){
			        	
			        	
			        	if(j+1< ifr.length && ptosCompra[j]){
			        		System.out.print(/*ifr[j] + " " + closes[j] + " " + high[j+1]+ " " +*/ data[j]);
			        					        		
			        		double[] ganho =  simulaCompra(low,high,closes,j);
			        		
			        		ganho[0] = ganho[0]- 0.0027;
			        		capital = capital*(1 + ganho[0]);
			        		if(ganho[0]>0){			        			
			        			pos++;
			        		}else{
			        			neg++;
			        		}
			        		System.out.println(" retorno:" + ganho[0] + " dias: " + ganho[1] );
			        		ganhoTotal += ganho[0]; 
			        	}
			        	
			        	//System.out.println((new BigDecimal(ifr[j])).intValue());
			        }
			        totalGeral += ganhoTotal;
			        System.out.println(acoes[k]);
					System.out.println("Ganho total: " + ganhoTotal);
					
				}
				double efic = new BigDecimal(pos).divide(new BigDecimal(pos+neg),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				System.out.println("Ganho geral: " + totalGeral + " Eficiência " + efic + " número de vezes: " + (pos + neg) + " capital: " + capital );
		//	}
			
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @param low
	 * @param high
	 * @param close
	 * @param pontoCompra
	 * @return double[2] valor ganho em % e número de dias
	 */
	public static double[] simulaCompra(double[] low,double[] high,double[] close,int pontoCompra){
		
		double retorno[] = new double[2];
		
		double maximo = high[pontoCompra + 1];
		double minimo = low[pontoCompra + 1];
		
		double minimoAlcancado = 0.01;
		
		double stopPerda = 0.02;
		
		if(low[pontoCompra + 1] < close[pontoCompra]){
			if(getPerda(close[pontoCompra],low[pontoCompra + 1])>stopPerda){
				retorno[0] = -stopPerda;
				retorno[1] = 1;
				return retorno;
			}
		}
		
		if(close[pontoCompra] > high[pontoCompra + 1]){
			
			retorno[0] = -getPerda(close[pontoCompra],close[pontoCompra+1]);
			if(retorno[0]<=-stopPerda){
				retorno[0] = -stopPerda;
			}

			retorno[1] = 1;
			return retorno;
		}
		
		if(close[pontoCompra] < high[pontoCompra + 1]){
			if(!((new BigDecimal(high[pontoCompra + 1]).divide(new BigDecimal(close[pontoCompra]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() - 1) > minimoAlcancado)){
				
			/*	double stopVenda =  close[pontoCompra];
				int dias = 1;
				boolean comprado = true;
				double ganho = 0.0;
				maximo = close[pontoCompra];
				
				while(comprado){
					
					if(pontoCompra + dias > low.length){
						/*Acabou os dias*/
			/*			comprado = false;
						ganho = getGanho(close[pontoCompra],close[pontoCompra + dias]);
					}else{
						
						
					/*
						if(low[pontoCompra + dias] < close[pontoCompra]){
							
							if(getPerda(close[pontoCompra],low[pontoCompra + dias]) >= 0.01){
								comprado = false;
								ganho = -0.01;
							}
							
						}else if(new BigDecimal(high[pontoCompra + dias]).divide(new BigDecimal(close[pontoCompra]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() - 1 >0.01){
							 if(low[pontoCompra + dias] < stopVenda){
						
								comprado = false;
								ganho = getGanho(close[pontoCompra],stopVenda);
							}else{
								if(high[pontoCompra + dias]>maximo){
									maximo = high[pontoCompra + dias];
									stopVenda =  getStopVenda(maximo, close[pontoCompra]);
								}
								
							}
						}	
						dias++;
					}
				}*/
				
				//retorno[0] = ganho;
				//retorno[1] = dias;
				
				if(close[pontoCompra] <= close[pontoCompra+1]){
					retorno[0] = getGanho(close[pontoCompra],close[pontoCompra+1]);
				}else{
					retorno[0] = -getPerda(close[pontoCompra],close[pontoCompra+1]);
				}
				
				
				if(retorno[0]<=-stopPerda){
					retorno[0] = -stopPerda;
				}
				retorno[1] = 1;
				return retorno;
				
			}
				
		}
		
		if(getGanho(close[pontoCompra],close[pontoCompra + 1])< minimoAlcancado){
			
			if(close[pontoCompra]>=close[pontoCompra + 1]){
				retorno[0] = getGanho(close[pontoCompra],close[pontoCompra + 1]);
			}else{
				retorno[0] = -getPerda(close[pontoCompra],close[pontoCompra + 1]);
			}
			
			if(retorno[0]<=-stopPerda){
				retorno[0] = -stopPerda;
			}
			//retorno[0] = 0.01;
			retorno[1] = 1;

			return retorno;
		}
		
		double stopVenda =  getStopVenda(maximo, close[pontoCompra]);
		
		int dias = 2;
		boolean comprado = true;
		double ganho = 0.0;
		while(comprado){
			
			if(pontoCompra + dias >= low.length){
				/*Acabou os dias*/
				comprado = false;
				ganho = getGanho(close[pontoCompra],close[close.length-1]);
			}else{
			
				if(low[pontoCompra + dias] < stopVenda){
					comprado = false;
					ganho = getGanho(close[pontoCompra],stopVenda);
				}else{
					if(high[pontoCompra + dias]>maximo){
						maximo = high[pontoCompra + dias];
						stopVenda =  getStopVenda(maximo, close[pontoCompra]);
					}
					dias++;
				}
			}
		}
		
		retorno[0] = ganho;
		retorno[1] = dias;

		
		return retorno;
	}
	
	public static double getStopVenda(double maximo, double precoCompra){
		
		double ganho = new BigDecimal(maximo).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	
		double stopPerc = 0.01; 
		
		
		if(ganho>=0.00 && ganho <0.05){
			stopPerc = 0.01;
		}else if(ganho>=0.05 && ganho <0.10){
			stopPerc = 0.02;
		}else if(ganho>=0.10 && ganho <0.15){
			stopPerc = 0.03;
		}else if(ganho>=0.15 && ganho <0.20){
			stopPerc = 0.04;
		}else if(ganho>=0.20 && ganho <0.25){
			stopPerc = 0.05;
		}else if(ganho>=0.25 && ganho <0.30){
			stopPerc = 0.06;
		}else{
			stopPerc = 0.07;
		}
/*		
		if(ganho>=0.00 && ganho <0.05){
			stopPerc = 0.01;
		}else{
			stopPerc = 0.02;
			stopPerc = new BigDecimal(ganho).divide(new BigDecimal(5),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() + 0.01;
		}*/
		
		
		double stopVenda = maximo*(1.0 - stopPerc);
		
		return stopVenda;
	}
	
	public static double getGanho(double compra, double venda){
		
		return new BigDecimal(venda).divide(new BigDecimal(compra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	}
	
	public static double getPerda(double compra, double venda){
		
		return new BigDecimal(compra - venda).divide(new BigDecimal(compra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
	}
	
}
