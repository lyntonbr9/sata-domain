package sata.metastock.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import sata.metastock.data.Values;
import sata.metastock.indices.IFR;
import sata.metastock.indices.MediaMovel;


public class SimulaMMConjunta {

 
 public static void main(String[] args){
  
		  int mmMen = 4;
		  int mmMai = 9;
		  
		  double[][] close = null;
		  double[][] high = null;
		  double[][] low = null;
		  double[][] mmMenor = null;
		  double[][] mmMaior = null; 
		  double[][] ifr = null;
		  
		  boolean[][] ptoCompras = null;
		  boolean[][] ptoVendas = null;
		  
		  String[][] datas = null;
		  
	//	  String[] acoes = {"PETR4_1"};
		  
	//	  String[] acoes = {"VALE5_2","TNLP4","ARCZ6","ACES4_2"};

	//	  String[] acoes = {"PETR4_2","PETR4_1"};

		  
		  
	//	  String[] acoes = {"PETR4_2","VALE5_2","USIM5","BBDC4_2","AMBV4_1","CMIG4","ITAU4_2","TNLP4","ARCZ6","ACES4_2"};

	/*	  String[] acoes = {"PETR4_2","PETR4_1","VALE5_1","VALE5_2","USIM5","BBDC4_0","BBDC4_1","BBDC4_2","AMBV4_1","AMBV4_2",
					"CMIG4","ITAU4_2","ITAU4_1","TNLP4","ARCZ6","ACES4_2","ACES4_1","CSNA3_1","CSNA3_2","GGBR4_3","GGBR4_2",
					"GGBR4_1","GGBR4_0","GOLL4","NETC4_2","NETC4_1","SDIA4","CESP4","ELET6","EMBR4","NATU3_2"};
*/
	/*	  String[] acoes = {"VALE5","USIM5","BBDC4","AMBV4","CMIG4","ITAU4","TNLP4","ARCZ6","ACES4","CSNA3","GGBR4",
				"GOLL4","SDIA4","ELET6","NATU3","VIVO4","UBBR11","CCRO3","ITSA4","LAME4"};
 */
		  String[] acoes = {"USIM5","AMBV4","ITAU4","CSNA3","GGBR4","GOLL4","NATU3","VIVO4","UBBR11","CCRO3","ITSA4","LAME4"};
		  
		  double ganhoGeral = 0.0;
		  
		  close = new double[acoes.length][];
		  ifr = new double[acoes.length][];
		  datas = new String[acoes.length][];
		  high = new double[acoes.length][];
		  low = new double[acoes.length][];
		  mmMenor = new double[acoes.length][];
		  ptoCompras = new boolean[acoes.length][];
		  
		  for(int k=0;k<acoes.length;k++){
			  Values values = new Values(acoes[k] + ".txt");
			  
			  close[k] = values.getCloses();
			  
			  IFR calculoIfr = new IFR(close[k],9,0.0);
			  ifr[k] = calculoIfr.getIFR();
			  
			  datas[k] = values.getData();
			  System.out.println(acoes[k] + " " + datas[k].length + " " + datas[k][0] + " " + datas[k][datas[k].length-1]);
			  high[k] = values.getHigh();
			  low[k] = values.getLow();
			  
			  MediaMovel mm = new MediaMovel(close[k],4);
			  mmMenor[k] = mm.getMediaMovel();
			  
	/*		  mm = new MediaMovel(close,mmMai); 
			  mmMaior = mm.getMediaMovel();*/
			  
			  ptoCompras[k] = mm.getPtosCompra(close[k],mmMenor[k]);
			  			  
		  }
			  
		  double capitalInicial = 1.0;
				  
		  boolean comprado = false;
		  int ptoCompra = -1;
		  double ganho = 0.0;
		  double ganhoTotal = 0.0;
		  double maximo = 0.0;
		  double stop = 0.0;
		  int positivas = 0;
		  int negativas = 0;
		  int diasInvestidos = 0;
		
		  int acao = 0;
		  for(int i=0;i<ptoCompras[0].length ;i++){
				  
		  			if(!comprado){
		  				acao = getAcaoCompra(ptoCompras,i);
		  			}
		  		   
		  	
				   if(acao!=-1 && ptoCompras[acao][i] && !comprado){
				    comprado = true; 
				    ptoCompra = i;
				    maximo = close[acao][ptoCompra];
				    stop = getStopVenda(maximo,close[acao][ptoCompra],6);
				    ganho = 0.0;
		   			

				   }
				   
				   if(comprado && ptoCompra!=i){
				   			diasInvestidos++;
				   			
				   			
				   			if(low[acao][i]<=stop){				   				
				   				comprado = false;
				   			    if(close[acao][ptoCompra]<=stop){ 
				   			     ganho += getGanho(close[acao][ptoCompra],stop);
				   			     positivas++;
				   			    }else{
				   			     ganho += (-1) * getPerda(close[acao][ptoCompra],stop);
				   			     negativas++;
				   			    }
				   			    

				   			    ganhoTotal += (ganho - 0.0062);
				   			   
				   			    capitalInicial = capitalInicial*(1+ganho);
				   	//		    System.out.println(("" + ganhoTotal).replace('.',',') + " 0," +ano);
				   			    System.out.println("Ganho: " + ganho + " ação: " + acoes[acao] +" Ganho Total:" + ganhoTotal + 
				   			    		" data Compra:" + datas[acao][ptoCompra] + " data venda:" + datas[acao][i] + 
				   						" mm1:" + close[acao][ptoCompra-1] +" mm2:" + mmMenor[acao][ptoCompra-1] + " mm1:" + close[acao][ptoCompra] +" mm2:" + mmMenor[acao][ptoCompra]); 
		
				  			
				   			}
				   			
				   			if(high[acao][i]>maximo){
				   				maximo = high[acao][i];
				   				stop = getStopVenda(maximo,close[acao][ptoCompra],6);
				   			}

				   				   		
				   	}
				   	
	   	}
		  
		double perc = 0.0;
				
		if(!(positivas + negativas == 0)){
			perc = new BigDecimal(positivas).divide(new BigDecimal(positivas + negativas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		}
		double ganhoPorDia = 0.0;
		if(diasInvestidos!=0){				
			ganhoPorDia = new BigDecimal(ganhoTotal).divide(new BigDecimal(diasInvestidos),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		}
				  
		ganhoGeral += ganhoTotal;
		
		System.out.println(" Ganho Total:" + ganhoTotal + " percentual de acerto:" 
					  		+ perc + " total compras:" +(positivas+negativas) + " dias investidos:" + diasInvestidos +
							" ganhoPorDia:" + ganhoPorDia );
		System.out.println("Capital: " + capitalInicial);
			
		
		  
		  
		  
		  

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

	
	public static int getAcaoCompra(boolean[][] ptosCompras,int dia){
		
		ArrayList compras = new ArrayList();
		
		for(int i=0;i<ptosCompras.length;i++){
			
			if(ptosCompras[i].length==ptosCompras[0].length && ptosCompras[i][dia]){
				
				compras.add(new Integer(i));
			
			}
			
		}
	
		if(compras.size()==0){
			
			return -1;
		}
		
		Random generator = new Random();
		return ((Integer)compras.get(generator.nextInt(compras.size()))).intValue();
		
		
	}
	
	
}


