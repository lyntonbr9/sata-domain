package sata.metastock.simulacao;

import java.math.BigDecimal;

import sata.metastock.data.Values;
import sata.metastock.indices.IFR;
import sata.metastock.indices.MediaMovel;


public class SimulaMediaMovel {

 
 public static void main(String[] args){
  
		  int mmMen = 4;
		  int mmMai = 9;
		  
		  double[] close = null;
		  double[] high = null;
		  double[] low = null;
		  double[] mmMenor = null;
		  double[] mmMaior = null; 
		  double[] ifr = null;
		  
		  boolean[] ptoCompras = null;
		  boolean[] ptoVendas = null;
		  
		  String[] datas = null;
		  
		 
		  
	//	  String[] acoes = {"PETR4_1"};
		  
	//	  String[] acoes = {"VALE5_2","TNLP4","ARCZ6","ACES4_2"};

	//	  String[] acoes = {"PETR4_2","PETR4_1"};

		  
		  
	//	  String[] acoes = {"PETR4_2","VALE5_2","USIM5","BBDC4_2","AMBV4_1","CMIG4","ITAU4_2","TNLP4","ARCZ6","ACES4_2"};

	/*	  String[] acoes = {"PETR4_2","PETR4_1","VALE5_1","VALE5_2","USIM5","BBDC4_0","BBDC4_1","BBDC4_2","AMBV4_1","AMBV4_2",
					"CMIG4","ITAU4_2","ITAU4_1","TNLP4","ARCZ6","ACES4_2","ACES4_1","CSNA3_1","CSNA3_2","GGBR4_3","GGBR4_2",
					"GGBR4_1","GGBR4_0","GOLL4","NETC4_2","NETC4_1","SDIA4","CESP4","ELET6","EMBR4","NATU3_2"};
*/
		  String[] acoes = {"PETR4","VALE5","USIM5","BBDC4","AMBV4","CMIG4","ITAU4","TNLP4","ARCZ6","ACES4","CSNA3","GGBR4",
				"GOLL4","SDIA4","ELET6","NATU3","VIVO4","UBBR11","CCRO3","ITSA4","LAME4"};
 
		  int[][] matriz = new int[10][40];
		  double[][] matriz2 = new double[10][40];
		  
		  double[][] valorAno = new double[acoes.length][7];
		  
		  
		  
		  double ganhoGeral = 0.0;
		  for(int k=0;k<acoes.length;k++){
			  Values values = new Values(acoes[k] + ".txt");
			  System.out.println("\n\n\n" + acoes[k]);
			  close = values.getCloses();
			  
			  IFR calculoIfr = new IFR(close,9,0.0);
			  ifr = calculoIfr.getIFR();
			  
			  datas = values.getData();
			  high = values.getHigh();
			  low = values.getLow();
			  double capitalInicial = 1.0;
			  int mais1 = 0;
			  
		  for(int m=4;m<5;m++){
		  for(int s=6;s<7;s++){
				  MediaMovel mm = new MediaMovel(close,m);
				  mmMenor = mm.getMediaMovel();
				  
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
				  for(int i=0;i<ptoCompras.length ;i++){
				   
				  	oscilacao += getGanho(low[i],high[i]);
				  	//System.out.println(getGanho(low[i],high[i]));
				  	
				   if(ptoCompras[i] && !comprado){
				    comprado = true; 
				    ptoCompra = i;
				    maximo = close[ptoCompra];
				    stop = getStopVenda(maximo,close[ptoCompra],s);
				    ganho = 0.0;
		   			if(datas[i].equals("14-Jul-03")){
		   			//	System.out.println("ATENÇÃO");
		   			
		   			}

				   }
				   
				   if(comprado && ptoCompra!=i){
				   			diasInvestidos++;
				   			
				   			
				   			if(low[i]<=stop){				   				
				   				comprado = false;
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
				   			    
				   			    
				   			    
				   			    String ano = datas[i].substring(datas[i].length()-2,datas[i].length());
				   			    valorAno[k][Integer.parseInt(ano)] += (ganho - 0.0062);
				   			    ganhoTotal += (ganho - 0.0062);
				   			    ganho = ganho;
				   			    capitalInicial = capitalInicial*(1+ganho);
				   	//		    System.out.println(("" + ganhoTotal).replace('.',',') + " 0," +ano);
				   		/*	    System.out.println("Ganho: " + ganho + " Ganho Total:" + ganhoTotal + 
				   			    		" data Compra:" + datas[ptoCompra] + " data venda:" + datas[i] + 
				   						" mm1:" + close[ptoCompra-1] +" mm2:" + mmMenor[ptoCompra-1] + " mm1:" + close[ptoCompra] +" mm2:" + mmMenor[ptoCompra]); 
		*/
				  			
				   			}
				   			
				   			if(high[i]>maximo){
				   				maximo = high[i];
				   				stop = getStopVenda(maximo,close[ptoCompra],s);
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
				  double perc = 0.0;
				  double percMais1 = 0.0;
				  double mediaOscil = new BigDecimal(oscilacao).divide(new BigDecimal(ptoCompras.length),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  if(!(positivas + negativas == 0)){
					  perc = new BigDecimal(positivas).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					  percMais1 = new BigDecimal(mais1).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  }
				  double ganhoPorDia = 0.0;
				  if(diasInvestidos!=0){				
				  	ganhoPorDia = new BigDecimal(ganhoTotal).divide(new BigDecimal(diasInvestidos),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
				  }
				  
				  ganhoGeral += ganhoTotal;
				  
					  System.out.println(" Ganho Total:" + ganhoTotal + " média móvel:" + m + " stop:" + s + "Ganho geral " + ganhoGeral + " ação: " + acoes[k] + " percentual de acerto:" 
					  		+ perc + " total compras:" +(positivas+negativas) + " dias investidos:" + diasInvestidos +
							" ganhoPorDia:" + ganhoPorDia + " Media Oscilação : " + mediaOscil);
					  System.out.println("Capital: " + capitalInicial);
					  matriz[s][m]++;
					  matriz2[s][m] += ganhoPorDia;
				  
		  }
		  }
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
 }
 
	public static double getStopVenda(double maximo, double precoCompra,int prc){
		
		double ganho = new BigDecimal(maximo).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1;
	
		double stopPerc = 0.01; 
		if(ganho==0){
			stopPerc = new BigDecimal(prc).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		}else if(ganho>0.00 && ganho <0.01){
			stopPerc = 0.01;
		}else if(ganho>=0.01 && ganho <0.05){
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
		}else{
			stopPerc = 0.08;
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


