/*
 * Created on 20/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import sata.metastock.exceptions.CelulaInvalidaEX;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Yahoo {

	public static void main(String args[]){
		
		Excel e;
		Object[][]plan = null;
		try {
			e = new Excel("H:\\flavio\\bolsa\\bovespa\\folha\\cotacao10052006.xls");
			plan = e.readFileToMatriz(0);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (CelulaInvalidaEX e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int j=0;j<plan.length;j++){
			System.out.println(plan[j][0]);
		
			FileWriter output = null;
			try {
				output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\atual\\" + plan[j][0] + ".txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String parte1 = "http://ichart.finance.yahoo.com/table.csv?s=";
			String parte2 = (String)plan[j][0];
			String parte3 = ".SA&a=04&b=1&c=";
			int parte4 = 2006;
			String parte5 = "&d=07&e=25&f=";
			int parte6 = 2006;
			String parte7 = "&g=d&ignore=.csv";
			
		//	URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=PETR4.SA&a=00&b=3&c=2004&d=11&e=31&f=2004&g=d&ignore=.csv");
			
		//	for(int i = 2006;i >= 1995;i--){
			
					try {
						//System.out.println("baixando " + i);
						URL url = new URL(parte1 + parte2 + parte3 + 2006 + parte5 + 2006 + parte7);
						System.out.println(parte1 + parte2 + parte3 + 2006 + parte5 + 2006 + parte7);
						String thisLine;
				        DataInputStream myInput = new DataInputStream(url.openStream());
				        myInput.readLine();
				        while ((thisLine = myInput.readLine()) != null){ 
				       	    
				       	    output.write(thisLine + "\n");
				        }    
				        		        
				        
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		//	}		
			try {
				output.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
}
