/*
 * Created on 21/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import sata.metastock.exceptions.CelulaInvalidaEX;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReadYahoo {

	public static Vector converteVector(Object[][] matrix){
		
		Vector v = new Vector();
		
		for(int i=0;i<matrix.length;i++){
			
			Vector vLinha = new Vector();
			for(int j=0;j<matrix[i].length;j++){
				
				vLinha.add(matrix[i][j]);
				
			}
			
			v.add(vLinha);
			
		}
	
		return v;
	}
	
	public static Object[][] readTxtToMatriz(String path, String acao){
		
		FileInputStream file;
		Object[][] matriz = null;
		try {
			file = new FileInputStream(path + "\\" + acao);
			DataInputStream myInput = new DataInputStream(file );
			
	        String thisLine = "";
	        int i=0;
	        myInput.readLine();
	        while ((thisLine = myInput.readLine()) != null){         	
	        	i++;
	        }
	        myInput.close();
	        file.close();
	        matriz = new Object[i][7];
	        
	        file = new FileInputStream(path + "\\" + acao);
	        DataInputStream myInput2 = new DataInputStream(file );
	        
	        i=matriz.length -1;
	        myInput2.readLine();
	        while ((thisLine = myInput2.readLine()) != null){ 
	        	
	        	
	        	matriz[i][0] = thisLine.substring(0,thisLine.indexOf(","));
	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
			    matriz[i][1] = thisLine.substring(0,thisLine.indexOf(","));
			    thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				matriz[i][2] = thisLine.substring(0,thisLine.indexOf(","));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				matriz[i][3] = thisLine.substring(0,thisLine.indexOf(","));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				matriz[i][4] = thisLine.substring(0,thisLine.indexOf(","));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				matriz[i][5] = thisLine.substring(0,thisLine.indexOf(","));
				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
				matriz[i][6] = thisLine;
				i--;
	        }    
	        myInput2.close();
	        
	        
	        for(int j=0;j<matriz.length;j++){
	        	System.out.println(j);
	        	
	        	
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("1-","01-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("2-","02-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("3-","03-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("4-","04-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("5-","05-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("6-","06-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("7-","07-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("8-","08-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("9-","09-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("101-","11-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("102-","12-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("103-","13-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("104-","14-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("105-","15-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("106-","16-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("107-","17-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("108-","18-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("109-","19-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("201-","21-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("202-","22-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("203-","23-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("204-","24-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("205-","25-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("206-","26-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("207-","27-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("208-","28-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("209-","29-");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("301-","31-");

	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-00","-2000");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-01","-2001");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-02","-2002");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-03","-2003");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-04","-2004");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-05","-2005");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-06","-2006");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-99","-1999");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-98","-1998");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-97","-1997");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-96","-1996");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("-95","-1995");
        	
	        	//matriz[j][1] = ((String)matriz[j][1]).replaceAll("-","/");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Jan","01");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Feb","02");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Mar","03");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Apr","04");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("May","05");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Jun","06");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Jul","07");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Aug","08");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Sep","09");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Oct","10");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Nov","11");
	        	matriz[j][0] = ((String)matriz[j][0]).replaceAll("Dec","12");
	        	
	        	System.out.println((String)matriz[j][0]);
	        	String dia = ((String)matriz[j][0]).substring(0,2);
	        	String mes = ((String)matriz[j][0]).substring(3,5);
	        	String ano = ((String)matriz[j][0]).substring(6,10);
	        	
	        	//matriz[j][1] = mes + "/" + dia +  "/" + ano;
	        	matriz[j][0] = ano + mes + dia;
	        	
	        	
	        }
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return matriz;
		
	}
	
	
	public static void main(String[] args){
		
		try {
			
			File f = new File("D:\\flavio\\bolsa\\");
			
			//String[] acoes = f.list();
			
		//	for(int k=0;k<acoes.length;k++){
				
				Object[][]plan = readTxtToMatriz(f.getPath(), "table.txt"/*acoes[k]*/);
				//System.out.println(acoes[k]);
				/*if(plan.length<1){
					continue;
				}*/
/*				File out = new File("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[0][0] + ".xls");
				
				if(!out.exists()){
					
					Vector meta1 = new Vector();
					meta1.add("MetaStock(tm) data file \\\\" + plan[0][0] + "\\");
					
					Vector meta2 = new Vector();
					meta2.add("TICKER");
					meta2.add("PER");
					meta2.add("DATE");
					meta2.add("TIME");
					meta2.add("OPEN");
					meta2.add("HIGH");
					meta2.add("LOW");
					meta2.add("CLOSE");
					meta2.add("VOLUME");
					meta2.add("O/I");
					
					Vector newAcao = new Vector();
					newAcao.add(meta1);
					newAcao.add(meta2);
					
					
					Excel ex = new Excel(true);
					ex.createExcelFileSheet(null, newAcao);
					ex.writeFile("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[0][0] + ".xls");
				}
			*/	
		//		Excel acao = new Excel("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[0][0] + ".xls");
		//		Vector dadosAcao = Read.converteVector(acao.readFileToMatriz(0));

				Vector dadosAcao = new Vector();
				FileOutputStream file = new FileOutputStream("D:\\flavio\\bolsa\\" + "INDUD" + ".txt");
		        DataOutputStream myOutput = new DataOutputStream(file);

		        myOutput.writeBytes("<TICKER>,<PER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>\n");
		        
				for(int i=0;i<plan.length;i++){

					Vector novaLinha = new Vector();
									
					novaLinha.add("INDU");
					novaLinha.add("D");
					novaLinha.add(plan[i][0]);
					novaLinha.add("00:00:00");
					novaLinha.add(plan[i][1]);
					novaLinha.add(plan[i][2]);
					novaLinha.add(plan[i][3]);
					novaLinha.add(plan[i][4]);   //novaLinha.add(plan[i][7]);
					novaLinha.add(plan[i][5]);
					novaLinha.add("0");
						
					dadosAcao.add(novaLinha);
					
					String linha = "INDU" + ",D," + plan[i][0] +  ",000000" + "," + plan[i][1] +  
					"," + plan[i][2] + "," + plan[i][3] + "," + plan[i][4] + "," + plan[i][5] +
					","  + "0\n" ;
					
					myOutput.writeBytes(linha);
					
				}
				
				myOutput.close();
				
/*				Excel update = new Excel(true);
				update.createExcelFileSheet(null, dadosAcao);
				update.writeFile("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[0][0] + ".xls");*/
				System.out.println(plan[0][0] + " atualizado ");

		//	  }	
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}
	
}
