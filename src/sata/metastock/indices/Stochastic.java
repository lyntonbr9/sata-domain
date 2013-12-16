/*
 * Created on 29/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.indices;

import java.math.BigDecimal;

import sata.metastock.exceptions.VetorMenor;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Stochastic {

	private int k = 5;
	private int slowingK = 3;
	private int d = 3;
	private double[] closes;
	private double[] stoc;
	
	public Stochastic(double[] closes){
		
		this.closes = closes;
		this.stoc = new double[closes.length];
	}
	
	public Stochastic(double[] closes, int k){
		this.closes = closes;
		this.k = k;
		this.stoc = new double[closes.length];
	
	}
		
	public Stochastic(double[] closes, int k,int slowingK){
		this.closes = closes;
		this.k = k;
		this.slowingK = slowingK;
		this.stoc = new double[closes.length];
	
	}
	
	public Stochastic(double[] closes, int k,int slowingK, int d){
	
		this.closes = closes;
		this.k = k;
		this.slowingK = slowingK;
		this.d = d;	
		this.stoc = new double[closes.length];
	}
	
	public double[] getStochastic() throws VetorMenor{
		
		for(int i=0;i<closes.length;i++){
			
				if(i<k +slowingK -1){
					stoc[i] = -1;
				}else{
					stoc[i]= getValue(i);			
					
				}
		}
		
		return stoc;
	}
	
	private double getValue(int day) throws VetorMenor{
		
		if(closes.length < (k +slowingK)){
			//throw new VetorMenor("Vetor menor que o necessário");
			
			return 1;
		}
	
		double q = 0.0;
		double p = 0.0;
		double sum = 0.0;
		for(int i=0;i<slowingK;i++){
			int end = day-i;
			q = closes[end] - getLLV(end-k+1,end);
			p = getHHV(end-k+1,end) - getLLV(end-k+1,end);
			if(p==0.0){
				sum += 1.0;
			}else{
				sum += new BigDecimal(q).divide(new BigDecimal(p),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
			}
			
		}
		
		return new BigDecimal(sum).divide(new BigDecimal(slowingK),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()*100;
	}
	
	public double getHHV(int init, int end){
		
		double hhv = 0.0;
		for(int i = init;i<=end;i++){
			
			if(closes[i]>hhv){
				hhv = closes[i];
			}
		}
		return hhv;
	}
	
	public double getLLV(int init, int end){
		
		double llv = 10000000000.0;
		for(int i = init;i<=end;i++){
			
			if(closes[i]<llv){
				llv = closes[i];
			}
		}
		return llv;
	}
	
	/**
	 * @return Returns the d.
	 */
	public int getD() {
		return d;
	}
	/**
	 * @param d The d to set.
	 */
	public void setD(int d) {
		this.d = d;
	}
	/**
	 * @return Returns the k.
	 */
	public int getK() {
		return k;
	}
	/**
	 * @param k The k to set.
	 */
	public void setK(int k) {
		this.k = k;
	}
	/**
	 * @return Returns the slowingK.
	 */
	public int getSlowingK() {
		return slowingK;
	}
	/**
	 * @param slowingK The slowingK to set.
	 */
	public void setSlowingK(int slowingK) {
		this.slowingK = slowingK;
	}
}
