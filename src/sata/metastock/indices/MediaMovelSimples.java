/*
 * Created on 11/06/2006
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
public class MediaMovelSimples {

	private int nDays = 14;
	private double[] prices;
	private double[] mm;
	
	public MediaMovelSimples(double[] prices){
		this.prices = prices;
		this.mm = new double[prices.length];
	}
	
	public MediaMovelSimples(double[] prices, int nDays){
		this.nDays = nDays;
		this.prices = prices;
		this.mm = new double[prices.length];
	}
	
	public void setNDays(int nDays){
		this.nDays = nDays;
	}
	
	public double[] calculaMediaMovelSimples(){
		
		for(int i=0;i<prices.length;i++){
			if(i<nDays-1){
				mm[i] = -1;
			}else{
				double sum = 0.0;
				for(int j=0;j<nDays;j++){
					sum += prices[i-j]; 
				}
				mm[i] = new BigDecimal(sum).divide(new BigDecimal(nDays),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
			}
			
		}
		return mm;
	}
	
}
