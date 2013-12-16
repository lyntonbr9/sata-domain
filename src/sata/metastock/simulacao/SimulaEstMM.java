package sata.metastock.simulacao;

import java.math.BigDecimal;

import sata.metastock.data.Values;
import sata.metastock.data.ValuesMeta;
import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.IFR;
import sata.metastock.indices.MediaMovel;
import sata.metastock.indices.Stochastic;


public class SimulaEstMM {

 
 public static void main(String[] args){
  
		  int mmMen = 4;
		  int mmMai = 9;
		  int mm60int = 60;
		  
		  int stc = 10;
		  int slowK = 3;
		  		  
		  double[] close = null;
		  double[] high = null;
		  double[] low = null;
		  double[] mmMenor = null;
		  double[] mmMaior = null; 
		  double[] mm60 = null;
		  double[] stoc10 =null;
		  double[] ifr = null;
		  
		  boolean[] ptoCompras = null;
		  boolean[] ptoVendas = null;
		  
		  String[] datas = null;
		  
		 
		  
	//	  String[] acoes = {"PETR4"};
		  
	//	  String[] acoes = {"VALE5_2","TNLP4","ARCZ6","ACES4_2"};

	//	  String[] acoes = {"PETR4_2","PETR4_1"};

		  
		  
	//	  String[] acoes = {"PETR4_2","VALE5_2","USIM5","BBDC4_2","AMBV4_1","CMIG4","ITAU4_2","TNLP4","ARCZ6","ACES4_2"};

		//  String[] acoes = {"PETR4_2","PETR4_1",/*"VALE5_1","VALE5_2",*/"USIM5","BBDC4_0","BBDC4_1","BBDC4_2","AMBV4_1","AMBV4_2",
		//			"CMIG4","ITAU4_2","ITAU4_1","TNLP4","ARCZ6","ACES4_2","ACES4_1","CSNA3_1","CSNA3_2","GGBR4_3","GGBR4_2",
		//			"GGBR4_1","GGBR4_0","GOLL4","NETC4_2","NETC4_1","SDIA4","CESP4","ELET6","EMBR4","NATU3_2"};

		/*  String[] acoes = {"PETR4","VALE5","USIM5","BBDC4","AMBV4","CMIG4","ITAU4","TNLP4","ARCZ6","ACES4","CSNA3","GGBR4",
				"GOLL4","SDIA4","ELET6","NATU3","VIVO4","UBBR11","CCRO3","ITSA4","LAME4","SUBA3","PCAR4"};*/
 
		  String[] acoes = {"INDU"};
		  
		  int[][] matriz = new int[10][40];
		  double[][] matriz2 = new double[10][40];
		  
		  double[][] valorAno = new double[acoes.length][20];
		  
		  
		  
		  double ganhoGeral = 0.0;
		  for(int k=0;k<acoes.length;k++){
				  ValuesMeta values = new ValuesMeta(acoes[k] + "D.txt");
				  System.out.println("\n\n\n" + acoes[k]);
				  close = values.getCloses();
				  
				  IFR calculoIfr = new IFR(close,9,0.0);
				  ifr = calculoIfr.getIFR();
				  
				  datas = values.getData();
				  high = values.getHigh();
				  low = values.getLow();
				  double capitalInicial = 1.0;
				  int mais1 = 0;
			  
	
				  MediaMovel mm = new MediaMovel(close,mmMen);
				  mmMenor = mm.getMediaMovel();
				  
				  MediaMovel media60 = new MediaMovel(close,15);
				  mm60 = media60.getMediaMovel();
				  
				  Stochastic stoc = new Stochastic(close, stc,slowK);
			       
			        try {
			        	stoc10 = stoc.getStochastic();
					} catch (VetorMenor e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  
		/*		  mm = new MediaMovel(close,mmMai); 
				  mmMaior = mm.getMediaMovel();*/
				  
				  ptoCompras = mm.getPtosCompra(close,mmMenor);
				  
				  int total = 0;
				  for(int f=0;f<ptoCompras.length;f++){
				  	if(ptoCompras[f]){
				  	//	System.out.println(datas[f]);
				  		total++;
				  	}
				  }
				  
				  //System.out.println(total);
				  
	//			  ptoVendas = mm.getPtosVenda(mmMenor,mmMaior);
				  
	/*			  if(mm.verificaConsistencia(ptoCompras,ptoVendas)){
				   System.out.println ("Inconsistência na compra e venda, simulação terminada");
				   return;
				  }*/
				  
				  boolean comprado = false;
				  int ptoCompra = -1;
				  double ganho = 0.0;
				  double ganhoTotal = 0.0;
				  double maximo = 0.0;
				  double stop = 0.0;
				  int positivas = 0;
				  int negativas = 0;
				  int diasInvestidos = 0;
				  double oscilacao = 0.0;
				  int positiva = 0;
				  boolean ficouPositiva=false;
				  double fundo = low[0];
				  
				  for(int i=0;i<ptoCompras.length ;i++){
				   
				  	oscilacao += getGanho(low[i],high[i]);
				  	//System.out.println(getGanho(low[i],high[i]));
				  	
				   if(i>0 && mm60[i]!=-1 && mm60[i]<close[i] &&  stoc10[i-1]<30 && ptoCompras[i] && !comprado){
				   	
				   	fundo = 100000000;
				   	//fundo = low[i];
				   	for(int h=i;h>i-5;h--){
				   		if(h>=0 && low[h]<fundo){
				   			fundo=low[h];
				   		}
				   	}
				   	
				    comprado = true; 
				    ptoCompra = i;
				    maximo = close[ptoCompra];
				    stop = getStopVenda(maximo,close[ptoCompra],fundo);
				    ganho = 0.0;
		   			

				   }
				   
				   if(comprado && ptoCompra!=i){
				   			diasInvestidos++;
				   			
				   			if(i!=ptoCompra && high[i]>close[ptoCompra] && getGanho(close[ptoCompra],high[i])>0.02 && !ficouPositiva){
				   				ficouPositiva=true;
				   				positiva++;
				   			}
				   			
				   			if(low[i]<=stop){				   				
				   				comprado = false;
				   				ficouPositiva=false;
				   			    if(close[ptoCompra]<=stop){ 
				   			     ganho += getGanho(close[ptoCompra],stop);
				   			     positivas++;
				   			    }else{
				   			     ganho += (-1) * getPerda(close[ptoCompra],stop);
				   			     negativas++;
				   			    }
				   			    
				   			    if(close[ptoCompra]<high[ptoCompra+1]){
				   			    	if(getGanho(close[ptoCompra],high[ptoCompra+1])>=0.01){
				   			    		mais1++;
				   			    		
				   			    	}
				   			    }
				   			    
				   			    
				   			    
				   			    String ano = datas[i].substring(0,4);
				   			    valorAno[k][2006 - Integer.parseInt(ano)] += (ganho - 0.0062);
				   			    ganhoTotal += (ganho - 0.0062);
				   			    ganho = ganho;
				   			    capitalInicial = capitalInicial*(1+ganho);
				   	//		    System.out.println(("" + ganhoTotal).replace('.',',') + " 0," +ano);
				   		/*	    System.out.println("Ganho: " + ganho + " Ganho Total:" + ganhoTotal + 
				   			    		" data Compra:" + datas[ptoCompra] + " data venda:" + datas[i] + 
				   						" mm60:" + mm60[ptoCompra] +" closePtoCompra:" + close[ptoCompra] + " mm1:" + close[ptoCompra] +" mm2:" + mmMenor[ptoCompra]); 
		*/
				  			
				   			}
				   			
				   			if(high[i]>maximo){
				   				maximo = high[i];
				   				stop = getStopVenda(maximo,close[ptoCompra],fundo);
				   			}

				   				   		
				   }
				   
		/*		   if(comprado && ptoVendas[i]){
				    
				    
				    if(close[ptoCompra]<=close[i]){ 
				     ganho += getGanho(close[ptoCompra],close[i]);
				    }else{
				     ganho += (-1) * getPerda(close[ptoCompra],close[i]);
				    }
				    
				    ganhoTotal += ganho;
				    System.out.println("Ganho: " + ganho + " Ganho Total:" + ganhoTotal + 
				    		" data Compra:" + datas[ptoCompra] + " data venda:" + datas[i] + 
							" mm1:" + mmMenor[ptoCompra-1] +" mm2:" + mmMaior[ptoCompra-1] + " mm1:" + mmMenor[ptoCompra] +" mm2:" + mmMaior[ptoCompra]); 
				    comprado = false;
				   }*/
	   			    
				  }
				  double percAtingiuPositiva = 0.0;
				  double perc = 0.0;
				  double percMais1 = 0.0;
				  double mediaOscil = new BigDecimal(oscilacao).divide(new BigDecimal(ptoCompras.length),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  if(!(positivas + negativas == 0)){
					  perc = new BigDecimal(positivas).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					  percMais1 = new BigDecimal(mais1).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					  percAtingiuPositiva = new BigDecimal(positiva).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  }
				  double ganhoPorDia = 0.0;
				  if(diasInvestidos!=0){				
				  	ganhoPorDia = new BigDecimal(ganhoTotal).divide(new BigDecimal(diasInvestidos),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  }
				  
				  ganhoGeral += ganhoTotal;
				  
					  System.out.println(" Ganho Total:" + ganhoTotal + " ação: " + acoes[k] + " percentual de acerto:" 
					  		+ perc + " percentual Atingiu positiva:" + percAtingiuPositiva +" total compras:" +(positivas+negativas) + " dias investidos:" + diasInvestidos +
							" ganhoPorDia:" + ganhoPorDia);
					  System.out.println("Capital: " + capitalInicial);
				/*	  matriz[s][m]++;
					  matriz2[s][m] += ganhoPorDia;*/
				  
		  }
		  
		  
		  for(int q=0;q<matriz.length;q++){
		  	for(int w=0;w<matriz[q].length;w++){
		  		System.out.print(matriz[q][w] + " ");
		  	}
		  	System.out.println();
		  }
		  
		  System.out.println("\n\n\n\n");
		  
		  for(int q=0;q<matriz2.length;q++){
		  	for(int w=0;w<matriz2[q].length;w++){
		  		System.out.print(matriz2[q][w] + " ");
		  	}
		  	System.out.println();
		  }
		  
		  System.out.println("\n\n\n\n");
		  
		  for(int a=0;a<valorAno.length;a++){
		  	System.out.print(acoes[a] + " ");
		  	if(acoes[a].length()==5){
		  		System.out.print("  ");
		  	}
		  	for(int b=0;b<valorAno[a].length;b++){		  	
		  		if(valorAno[a][b]<0){
		  			System.out.print(" " + new BigDecimal(valorAno[a][b]).setScale(BigDecimal.ROUND_HALF_EVEN,5));
		  		}else{
		  			System.out.print("  " + new BigDecimal(valorAno[a][b]).setScale(BigDecimal.ROUND_HALF_EVEN,5));
		  		}	
		  	}	
		  	System.out.println();
		  }
		  
		  
		  for(int a=0;a<valorAno[0].length;a++){
		  	double total = 0.0;
		  	for(int b=0;b<valorAno.length;b++){		  	
		  		total += valorAno[b][a];
		  	}	
		  	System.out.println((2006-a) + " = " + total);
		  }
		  
 }
 
	public static double getStopVenda(double maximo, double precoCompra,double fundo){
		
		double ganho = new BigDecimal(maximo).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	
		boolean stopFundo=false;
		double stopPerc = 0.0001;
		if(ganho<0.015){
			
			stopFundo=true;
			
		}

		
		if(stopFundo){
			return fundo;
		}
		double stopVenda = maximo*(1.0 - stopPerc);
		
		return stopVenda;
	}
/*	
	public static double getStopVenda(double maximo, double precoCompra,double fundo){
		
		double ganho = new BigDecimal(maximo).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	
		boolean stopFundo=false;
		double stopPerc = 0.03; 
		if(ganho<0.015){
			//stopPerc = new BigDecimal(prc).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
			stopFundo=true;
			
		}if(ganho>=0.015 && ganho <0.03){
			stopPerc = 0.005;
		}else if(ganho>=0.03 && ganho <0.05){
			stopPerc = 0.015;
		}else if(ganho>=0.05 && ganho <0.10){
			stopPerc = 0.03;
		}else if(ganho>=0.10 && ganho <0.15){
			stopPerc = 0.04;
		}else if(ganho>=0.15 && ganho <0.20){
			stopPerc = 0.05;
		}else if(ganho>=0.20 && ganho <0.25){
			stopPerc = 0.06;
		}else if(ganho>=0.25 && ganho <0.30){
			
			stopPerc = 0.07;
		}

		
		if(stopFundo){
			return fundo;
		}
		double stopVenda = maximo*(1.0 - stopPerc);
		
		return stopVenda;
	}
	*/
 
	public static double getGanho(double compra, double venda){
		
		return new BigDecimal(venda).divide(new BigDecimal(compra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	}
	
	public static double getPerda(double compra, double venda){
		
		return new BigDecimal(compra - venda).divide(new BigDecimal(compra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
	}

}



