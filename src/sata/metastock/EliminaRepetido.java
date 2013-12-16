/*
 * Created on 27/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sata.metastock.exceptions.CelulaInvalidaEX;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EliminaRepetido {

	public static void main(String args[]){
		
		Excel e;
		Object[][]plan = null;
		try {
			e = new Excel("H:\\flavio\\bolsa\\bovespa\\folha\\cotacao10052006.xls");
			plan = e.readFileToMatriz(0);
		
			for(int j=0;j<plan.length;j++){
				System.out.println("eliminando repetidos da ação :" + plan[j][0]);
			
				FileWriter output = null;
				output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\" + plan[j][0] + ".txt");
	
				FileInputStream file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\inteira\\" + plan[j][0] + ".txt");
				
				DataInputStream myInput = new DataInputStream(file);
					
			     String thisLine = "";
			     ArrayList list = new ArrayList();
			     while ((thisLine = myInput.readLine()) != null){         	
			     	  incluiSeNaoTem(list,thisLine);		       	
			     }
			     
			     for(int i=0;i<list.size();i++){			     
			     	output.write((String)list.get(i) + "\n");
			     }
			     
			     output.close();
				
			}
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

	
	}
	
	public static void incluiSeNaoTem(ArrayList list,String thisLine){
		
		for(int i=0;i<list.size();i++){
			
			if(((String)list.get(i)).substring(0,10).equals(thisLine.substring(0,10))){
				return;
			}
			
		
		}
		
		list.add(thisLine);
	}
		
		
}
