/*
 * Created on 29/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.simulacao;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import sata.metastock.data.Values;
import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.MediaMovelSimples;
import sata.metastock.indices.Stochastic;



/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Teste {
	
	public static void main(String[] args){
		
		FileInputStream file;
		double[] closes = null;
		double[] stoc = null;
		double[] mediaMovel = null;
		String[] data = null;
	/*	int k = 10;
		int slowingK = 7;
		int mmDays = 3;
		int diasSeguintes = 1;*/

		try{
			Values values = new Values("NETC4.txt");
		
			closes = values.getCloses();
			data = values.getData();
			
			double maiorEficiencia = 0.0;
			double maiorGanhoMedio = 0.0;
			
			for(int k = 3;k<25;k++){			
			for(int slowingK = 1;slowingK<4;slowingK++){	
				System.out.println("\n" + k + " " + slowingK);
			//for(int mmDays = 3;mmDays<20;mmDays++){
			for(int diasSeguintes = 1;diasSeguintes<10;diasSeguintes++){
			for(int taxaCompra = 15;taxaCompra<=30;taxaCompra++){
				
			
		        Stochastic  stochastic =  null;
		                
		        stochastic =  new Stochastic(closes);
		        stochastic.setK(k);
		        stochastic.setSlowingK(slowingK);
		        stoc = stochastic.getStochastic();
		        
	/*	        MediaMovelSimples mms = new MediaMovelSimples(stoc);	        
		        mms.setNDays(mmDays);
		        
		        mediaMovel = mms.calculaMediaMovelSimples();
		      */  
		        boolean[] ptosCompra = new boolean[closes.length];
		        
		        Teste.getComprasAtravessa(stoc,taxaCompra,ptosCompra);
		        
		   //     Teste.getComprasMediaMovel(stoc,mediaMovel,ptosCompra);
		        
		        double eficiencia = getEfeciencia(closes,ptosCompra,diasSeguintes);
		        double ganhoMedio = getGanhoMedio(closes,ptosCompra,diasSeguintes);
		        
		        if(eficiencia > maiorEficiencia){
		        	maiorEficiencia = eficiencia;
		        	System.out.println("\nMaior eficiencia até agora");
			        System.out.println("K="+ k + " SlowingK=" + slowingK + " taxa compra=" + taxaCompra + " Dias Seguintes:  " + diasSeguintes);
		//	        System.out.println("K="+ k + " SlowingK=" + slowingK + " Média Movel=" + mmDays + " Dias Seguintes:  " + diasSeguintes);

			        System.out.println(eficiencia +  "%");
			        System.out.println(getGanhoMedio(closes,ptosCompra,diasSeguintes)+  "% de ganho Médio");
			        System.out.println(getPerdaMedia(closes,ptosCompra,diasSeguintes)+  "% de perda Média");
			        System.out.println(getNumeroPositivas(closes,ptosCompra,diasSeguintes)+  " compras positivas efetuadas");
			        System.out.println(getNumeroNegativas(closes,ptosCompra,diasSeguintes)+  " compras negativas efetuadas");
			        System.out.println(getTaxaAltasAcoes(closes,diasSeguintes)+  "% altas em dias sequintes");
		        }
		       if(ganhoMedio > maiorGanhoMedio){
		        	maiorGanhoMedio = ganhoMedio;
		        	System.out.println("\nMaior ganho médio até agora");
			        System.out.println("K="+ k + " SlowingK=" + slowingK + " taxa compra=" + taxaCompra + " Dias Seguintes:  " + diasSeguintes);
		//	        System.out.println("K="+ k + " SlowingK=" + slowingK + " Média Movel=" + mmDays + " Dias Seguintes:  " + diasSeguintes);
			        System.out.println(eficiencia +  "%");
			        System.out.println(getGanhoMedio(closes,ptosCompra,diasSeguintes)+  "% de ganho Médio");
			        System.out.println(getPerdaMedia(closes,ptosCompra,diasSeguintes)+  "% de perda Média");
			        System.out.println(getNumeroPositivas(closes,ptosCompra,diasSeguintes)+  " compras positivas efetuadas");
			        System.out.println(getNumeroNegativas(closes,ptosCompra,diasSeguintes)+  " compras negativas efetuadas");
			        System.out.println(getTaxaAltasAcoes(closes,diasSeguintes)+  "% altas em dias sequintes");
		        }
			}
			}
			}
			}
	        /*
	        
	        for(int j=0;j<ptosCompra.length;j++){
	        	
	        	if(ptosCompra[j]){
	        		//System.out.println(stoc[j-1] + " " + stoc[j] + " " + data[j]);
	        		System.out.println(mediaMovel[j-1] +" " +stoc[j-1] + " " +mediaMovel[j] + "  " + stoc[j] + " " + data[j]);
	        	}
	        	
	        }
/*	        
	        for(int j=0;j<stoc.length;j++){
	        	//System.out.println(stoc[j] + " " + closes[j] + " " + data[j]);
	        	System.out.println(new BigDecimal(stoc[j]).intValue());
	        }
	        
	        System.out.println("**********************MEDIA MOVEL********************************************");
	        
	        for(int j=0;j<mediaMovel.length;j++){
	        	//System.out.println(stoc[j] + " " + closes[j] + " " + data[j]);
	        	System.out.println(new BigDecimal(mediaMovel[j]).intValue());
	        }
	            */
		} catch (VetorMenor e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static void getComprasAtravessa(double[] stoc,int pontoCompra, boolean[] ptosCompra){
		
		for(int i=0;i<stoc.length;i++){
			
			if(i+1 <stoc.length && stoc[i]!= -1 && stoc[i]<=pontoCompra && stoc[i+1]>=pontoCompra){
				ptosCompra[i+1] = true;
			}
			
		}
	
	}
	
	public static void getComprasMediaMovel(double[] stoc, double[] mm,boolean[] ptosCompra){
			
		for(int i=0;i<stoc.length;i++){
			
			if(i+1 <stoc.length && stoc[i]!= -1 && stoc[i]<mm[i] && stoc[i+1]>mm[i]){
				ptosCompra[i+1] = true;
			}
			
		}		
	}
	
	public static double getPerdaMedia(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double perda = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					perda += new BigDecimal(closes[i] - closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		return new BigDecimal(perda).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static double getGanhoMedio(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double ganho = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					ganho += new BigDecimal(closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).add(new BigDecimal(-1)).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		
		if(comprasPositivas==0){
			return 0.0;
		}
		
		return new BigDecimal(ganho).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static int getNumeroNegativas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasNegativas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					comprasNegativas++;
				}
				
			}
		}
				
		return comprasNegativas;
	} 
	
	public static int getNumeroPositivas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
				
		return comprasPositivas;
	} 
	
	public static double getEfeciencia(double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getTaxaAltasAcoes(double closes[],int days){
		
		int altas = 0;
		for(int i=0;i<closes.length;i++){
			    
				if((i+days) < (closes.length-1) && closes[i+days] > closes[i]){
					altas++;
				}
				
			
		}
		
		double result = new BigDecimal(altas).divide(new BigDecimal(closes.length),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	
}
