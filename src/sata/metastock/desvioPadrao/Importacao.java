package sata.metastock.desvioPadrao;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class Importacao {
	
	static int maximo = 6;
	static String[] minimas = new String[maximo];
	
	public static void main(String args[]){
		
		FileInputStream file;
				
		try {

			file = new FileInputStream("C:\\Temp\\DP.txt");
			//file = new FileInputStream("C:\\PRIVADO\\flavio\\bovespa\\historico\\COTAHIST_A2001\\COTAHIST_A2001.txt");
			DataInputStream myInput = new DataInputStream(file);
				
		     String thisLine = "";
		     int j = 0;   
		     while ((thisLine = myInput.readLine()) != null){ 
		    //	System.out.println(thisLine);
		    	String[] valores = thisLine.split(",");
		    	
		    	for(int i=0;i<valores.length;i++){		    		
		    		if(i==3){
		    			
		    			if(j<maximo){
		    				minimas[j] = valores[i];
		    				System.out.println(minimas[j]);
		    			}
		    			j++;
		    		}		
		    	}
		     }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		calculaDesvioPadrao();
		
	}
	
	public static void calculaDesvioPadrao(){
		BigDecimal soma = new BigDecimal(0);
		//String[] somaVariancia = new String[maximo];
		
		for(int i =0;i<maximo;i++){			
			
			soma = soma.add(new BigDecimal(minimas[i]));
		}	
		
		BigDecimal media = soma.divide(new BigDecimal(maximo),BigDecimal.ROUND_HALF_EVEN,6);		
		BigDecimal somaVariancia = new BigDecimal(0);
		
		for(int i =0;i<maximo;i++){			
			somaVariancia = somaVariancia.add(new BigDecimal((new BigDecimal(minimas[i])).subtract(media).doubleValue()).pow(2));
			soma = soma.add(new BigDecimal(minimas[i]));
		}	
				
		System.out.println(somaVariancia);
		System.out.println(Math.sqrt(somaVariancia.doubleValue()));
	
	}
}
