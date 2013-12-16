/*
 * Created on 17/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.indices;

import java.math.BigDecimal;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IFR {

	private int diasIFR;
	private double[] prices;
	private double[] ifr;
	private double[] valorCompra1;
	private double[] valorCompra2;
	private double[] priceChange;
	private double indicaCompra =20;
	
	public IFR(double[] prices,int dias, double indicaCompra){
		this.prices = prices;
		this.diasIFR = dias;
		this.ifr = new double[prices.length];
		this.valorCompra1 = new double[prices.length];
		this.valorCompra2 = new double[prices.length];
		this.priceChange = new double[prices.length];
		this.indicaCompra = indicaCompra;
	}
	
	public double[] getValorCompra1(){
		
		calcPriceChange();
		for(int i=0;i<prices.length;i++){
			
			if(i<diasIFR-1){
				valorCompra1[i]=-1;
			}else{
				valorCompra1[i] = calcValorParaIFRIndica(i,false);
			}
		}
		
		return valorCompra1;
	}
	
	public double[] getValorCompra2(){
		
		calcPriceChange();
		for(int i=0;i<prices.length;i++){
			
			if(i<diasIFR-1){
				valorCompra2[i]=-1;
			}else{
				valorCompra2[i] = calcValorParaIFRIndica(i,true);
			}
		}
		
		return valorCompra2;
	}
	
	public double[] getIFR(){
		
		calcPriceChange();
		for(int i=0;i<prices.length;i++){
			
			if(i<diasIFR-1){
				ifr[i]=-1;
			}else{
				ifr[i] = calcIFR(i);
			}
		}
		
		return ifr;
	}
	
	private void calcPriceChange(){
		priceChange[0] = 0;
		for(int i=1;i<prices.length;i++){
			if(prices[i-1]<=prices[i]){
				priceChange[i] = prices[i] - prices[i-1];
			}else{
				priceChange[i] =  prices[i] - prices[i-1];
			}
		}
		/*priceChange[0] = 0;
		for(int i=1;i<prices.length;i++){
			if(prices[i-1]<=prices[i]){
				priceChange[i] = new BigDecimal(prices[i]).divide(new BigDecimal(prices[i-1]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() -1;
			}else{
				priceChange[i] = -1*(1 - new BigDecimal(prices[i]).divide(new BigDecimal(prices[i-1]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue());
			}
		}*/
	}
	
	private double calcValorParaIFRIndica(int dia, boolean pos){
		
		if(dia ==priceChange.length -1 ){
			System.out.println("");
		}
		
		double somaPositivos = 0.0;
		double somaNegativos = 0.0;
		for(int i=dia-1;i>dia-diasIFR;i--){
			if(priceChange[i]>=0){
				somaPositivos += priceChange[i];
				
			}else{
				somaNegativos += (-1)*priceChange[i];
				
			}
		}
				
		double valorPos = somaNegativos*(new BigDecimal(indicaCompra).divide(new BigDecimal(100-indicaCompra),BigDecimal.ROUND_HALF_EVEN,6)).doubleValue() - somaPositivos;
		double valorNeg = somaPositivos*(new BigDecimal(100-indicaCompra).divide(new BigDecimal(indicaCompra),BigDecimal.ROUND_HALF_EVEN,6)).doubleValue() - somaNegativos;
		
		double valor = 0.0;
		//priceChange[i] = prices[i] - prices[i-1];
		if(pos){
			valor = valorPos + prices[dia-1];
		}else{
			valor = (-1)*valorNeg + prices[dia-1];
		}
		
		
		return valor;
	}
	
	private double calcIFR(int dia){
		
		if(dia ==priceChange.length -1 ){
			System.out.println("");
		}
		
		double somaPositivos = 0.0;
		double somaNegativos = 0.0;
		for(int i=dia;i>dia-diasIFR;i--){
			if(priceChange[i]>=0){
				somaPositivos += priceChange[i];
				
			}else{
				somaNegativos += (-1)*priceChange[i];
				
			}
		}
				
		if(somaNegativos!=0.0){
			double taxa = 1 + new BigDecimal(somaPositivos).divide(new BigDecimal(somaNegativos),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
			return (100 - new BigDecimal(100).divide(new BigDecimal(taxa),BigDecimal.ROUND_HALF_EVEN,6).doubleValue());
		}else{
			return 100.00;
		}
		
	}
}
