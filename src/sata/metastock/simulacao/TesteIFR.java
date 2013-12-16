/*
 * Created on 17/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.simulacao;


import java.io.FileInputStream;
import java.math.BigDecimal;

import sata.metastock.data.Values;
import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.IFR;
import sata.metastock.indices.Stochastic;
import sata.metastock.util.CalculoUtil;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TesteIFR {

	public static void main(String args[]){
		FileInputStream file;
		double[] closes = null;
		double[] high = null;
		double[] ifr = null;
		double[] mediaMovel = null;
		String[] data = null;
		int taxaCompra = 20;
		int diasSeguintes = 1;
	
		int diasIFR = 9;

		try{
			Values values = new Values("AMBV4.txt");
		
			closes = values.getCloses();
			data = values.getData();
			high = values.getHigh();
			
			double maiorEficiencia = 0.0;
			double maiorGanhoMedio = 0.0;
			
			IFR calcIFR = new IFR(closes,diasIFR,20);
			ifr = calcIFR.getIFR();
			
			boolean[] ptosCompra = new boolean[closes.length];
	        
			TesteIFR.getComprasIFR(ifr,taxaCompra,ptosCompra);
	      
			
	        for(int j=0;j<ifr.length;j++){
	        	
	        	if(j+1< ifr.length && ptosCompra[j]){
	        		System.out.println(ifr[j] + " " + closes[j] + " " + high[j+1]+ " " + data[j]);
	        	}
	        	
	        	//System.out.println((new BigDecimal(ifr[j])).intValue());
	        }

			
	        double eficiencia = CalculoUtil.getEfeciencia(closes,ptosCompra,diasSeguintes);
	        double eficienciaUsandoMaxima = CalculoUtil.getEfecienciaUsandoMaxima(high,closes,ptosCompra,diasSeguintes);
	        double eficienciaUsandoMaximaAcimaDe = CalculoUtil.getEfecienciaUsandoMaximaAcimaDe(0.01,high,closes,ptosCompra,diasSeguintes);
	        double eficiencia12 = CalculoUtil.getEfeciencia12(closes,ptosCompra);
	         
	        System.out.println("\nEficiência usando fechamento e fechamento seguinte: " + eficiencia +  "%");
	        System.out.println("\nEficiência usando fechamento e máxima seguinte: " + eficienciaUsandoMaxima + "%");
	        System.out.println("\nEficiência usando fechamento e máxima seguinte acima de : " + eficienciaUsandoMaximaAcimaDe + "%");
	        System.out.println("\nEficiência usando fechamento lucro nos 2 dias seguintes: " + eficiencia12 + "%");
	        System.out.println(CalculoUtil.getGanhoMedio(closes,ptosCompra,diasSeguintes)+  "% de ganho Médio");
	        System.out.println(CalculoUtil.getPerdaMedia(closes,ptosCompra,diasSeguintes)+  "% de perda Média");
	        System.out.println(CalculoUtil.getNumeroPositivas(closes,ptosCompra,diasSeguintes)+  " compras positivas efetuadas");
	        System.out.println(CalculoUtil.getNumeroNegativas(closes,ptosCompra,diasSeguintes)+  " compras negativas efetuadas");
	        System.out.println(CalculoUtil.getTaxaAltasAcoes(closes,diasSeguintes)+  "% altas em dias sequintes");

	        
	        /*
	        
	        for(int j=0;j<ptosCompra.length;j++){
	        	
	        	if(ptosCompra[j]){
	        		//System.out.println(stoc[j-1] + " " + stoc[j] + " " + data[j]);
	        		System.out.println(mediaMovel[j-1] +" " +stoc[j-1] + " " +mediaMovel[j] + "  " + stoc[j] + " " + data[j]);
	        	}
	        	
	        }*/
	        
	        
/*	        System.out.println("**********************MEDIA MOVEL********************************************");
	        
	        for(int j=0;j<mediaMovel.length;j++){
	        	//System.out.println(stoc[j] + " " + closes[j] + " " + data[j]);
	        	System.out.println(new BigDecimal(mediaMovel[j]).intValue());
	        }
	            */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getComprasIFR(double[] ifr,int pontoCompra, boolean[] ptosCompra){
		
		for(int i=0;i<ifr.length;i++){
			
		/*	if(i+1 <ifr.length && ifr[i]!= -1 && ifr[i]<pontoCompra && ifr[i+1]>pontoCompra){
				ptosCompra[i+1] = true;
			}*/
			
		/*	if(i+2 <ifr.length && ifr[i]!= -1 && ifr[i]>=pontoCompra && ifr[i+1]<=pontoCompra){
				ptosCompra[i+2] = true;
			}*/
			
			if(i+1 <ifr.length && ifr[i]!= -1 && ifr[i]>=pontoCompra && ifr[i+1]<=pontoCompra){
				ptosCompra[i+1] = true;
			}

			
		}
	
	}
	
}
