/*
 * Created on 07/10/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.indices;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ATR {
	
	private double[] low = null;
	private double[] high = null;
	private double[] close = null;
	private double[] trueRange = null;
	private double[] atr = null;
	private int average = 10;
	
	
	public ATR(int average,double[] low,double[] high,double[] close){
		
		this.low = low;
		this.high = high;
		this.close = close;
		trueRange = new double[close.length];
		this.average = average;
	
	}
	
	public double[] getATR(){
		
		calcTrueRange();
		MediaMovel mm = new MediaMovel(trueRange,average);
		atr = mm.getMediaMovel();
		return atr;
	
	}
	
	
	private void calcTrueRange(){
		
		for(int i=0;i<close.length;i++){
			
			double maiorRange = high[i] - low[i];
			
			if(i-1>0){
				if(close[i-1]>high[i]){
					if(close[i-1]-high[i]>maiorRange){
						maiorRange = close[i-1]-high[i];
					}
				}else if(close[i-1]<high[i]){
					if(high[i] - close[i-1]>maiorRange){
						maiorRange = high[i] - close[i-1];
					}
				}
				
				if(close[i-1]>low[i]){
					if(close[i-1]-low[i]>maiorRange){
						maiorRange = close[i-1]-low[i];
					}
				}else if(close[i-1]<low[i]){
					if(low[i] - close[i-1]>maiorRange){
						maiorRange = low[i] - close[i-1];
					}
				}
				
			}
			
			trueRange[i]=maiorRange;
		}
	
	
	}
	
	public double[] getEntrySignal(int llv){
		
		double[] entry = new double[close.length];
		for(int i=llv-1;i<close.length && llv<close.length;i++){
			
			double menor = Double.MAX_VALUE;
			for(int j=i;j>i-llv;j--){
				if(low[j]<menor){
					menor=low[j];
				}
			}
			
			entry[i] = close[i] - (menor + 2*atr[i]);
		}
		return entry;
	}
	
	public double[] getExitSignal(int hhv){
		
		double[] exit = new double[close.length];
		for(int i=hhv-1;i<close.length && hhv<close.length;i++){
			
			double maior = 0;
			for(int j=i;j>i-hhv;j--){
				if(high[j]>maior){
					maior=low[j];
				}
			}
			
			exit[i] = close[i] - (maior - 2*atr[i]);
		}
		return exit;
	}

}
