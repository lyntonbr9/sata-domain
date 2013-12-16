/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;

/*
 * Created on 13/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.MediaMovelSimples;
import sata.metastock.indices.Stochastic;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoadValues {
	
	private double[] closes = null;
	private double[] high = null;
	private double[] low = null;
	private double[] open = null;
	private String[] data = null;
	
	public LoadValues(String acao){
		
		FileInputStream file;

		try {
			file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa\\" + acao);
			DataInputStream myInput = new DataInputStream(file );
			
			
	        String thisLine = "";
	        int i=0;
	        while ((thisLine = myInput.readLine()) != null){         	
	        	i++;
	        }
	        myInput.close();
	        file.close();
	        
	        closes = new double[i];
	        high = new double[i];
	        low = new double[i];
	        open = new double[i];
	        data = new String[i];
	        
	        
	        file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa\\" + acao);
	        DataInputStream myInput2 = new DataInputStream(file );
	       
	        
	        
	        while ((thisLine = myInput2.readLine()) != null){ 
	        	
	        	/*data*/
	        	data[i-1] = thisLine.substring(0,thisLine.indexOf(","));
	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
	        	
	        	/*abertura*/
			    open[i-1]= Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
			    thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
			    
			    /*maxima*/
				high[i-1] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				
				/*minima*/
				low[i-1] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				
				/*fechamento*/
				closes[i-1] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				
				/*volume*/
				thisLine.substring(0,thisLine.indexOf(","));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				
				//closes[i-1] = Double.parseDouble(thisLine);
				
				//System.out.println(closes[i-1]);
				i--;
	        }
        
	        myInput2.close();
	        file.close();

	            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	
	
	
	/**
	 * @return Returns the high.
	 */
	public double[] getHigh() {
		return high;
	}
	/**
	 * @return Returns the closes.
	 */
	public double[] getCloses() {
		return closes;
	}
	/**
	 * @return Returns the data.
	 */
	public String[] getData() {
		return data;
	}
	/**
	 * @return Returns the low.
	 */
	public double[] getLow() {
		return low;
	}
	
	
	/**
	 * @return Returns the open.
	 */
	public double[] getOpen() {
		return open;
	}
}
