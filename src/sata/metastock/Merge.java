/*
 * Created on 27/05/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import sata.metastock.exceptions.CelulaInvalidaEX;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Merge {
	
	public static void main(String args[]){
	
		Excel e;
		Object[][]plan = null;
		try {
			e = new Excel("H:\\flavio\\bolsa\\bovespa\\folha\\cotacao04092006.xls");
			plan = e.readFileToMatriz(0);
		
			for(int j=0;j<plan.length;j++){
				System.out.println(plan[j][0]);
				
				FileWriter output = null;
				
				File arq = new File("H:\\flavio\\bolsa\\bovespa\\historico\\atual2\\" + plan[j][0] + ".txt");
				
				if(arq.exists()){
					output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\atual\\" + plan[j][0] + ".txt");
					
					FileInputStream file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\atual2\\" + plan[j][0] + ".txt");
	/*				FileInputStream file2 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente2\\" + plan[j][0] + ".txt");
					FileInputStream file3 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente3\\" + plan[j][0] + ".txt");
					FileInputStream file4 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente4\\" + plan[j][0] + ".txt");
					FileInputStream file5 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente5\\" + plan[j][0] + ".txt");
					FileInputStream file6 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente6\\" + plan[j][0] + ".txt");
					FileInputStream file7 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente7\\" + plan[j][0] + ".txt");
					FileInputStream file8 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente8\\" + plan[j][0] + ".txt");
					FileInputStream file9 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente9\\" + plan[j][0] + ".txt");
					FileInputStream file10 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente10\\" + plan[j][0] + ".txt");
					FileInputStream file11 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente11\\" + plan[j][0] + ".txt");
					FileInputStream file12 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente12\\" + plan[j][0] + ".txt");
					FileInputStream file13 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\recente13\\" + plan[j][0] + ".txt");
					*/
					DataInputStream myInput = new DataInputStream(file);
						
				     String thisLine = "";
				    
				     String novaLinha = "04-Sep-06," + plan[j][5] + ","  +
				     					plan[j][7] + "," + plan[j][8] + "," + plan[j][2] + 
				     					"," + new Double(Double.parseDouble((String)plan[j][9])).intValue() + "," + plan[j][2];
				     output.write(novaLinha + "\n");
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				       	
				     }
				     
				    
				     myInput.close();
				     
	/*				 myInput = new DataInputStream(file2);
				    
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();
				     
					 myInput = new DataInputStream(file3);					
				     thisLine = "";
				     
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();
	
					 myInput = new DataInputStream(file4);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();	
				     
					 myInput = new DataInputStream(file5);				
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file6);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();	
				     
					 myInput = new DataInputStream(file7);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();	
				     
					 myInput = new DataInputStream(file8);
					 thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file9);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file10);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file11);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file12);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
					 myInput = new DataInputStream(file13);					
				     thisLine = "";
				     while ((thisLine = myInput.readLine()) != null){         	
				     	output.write(thisLine + "\n");
				     }
				     myInput.close();			     
	
				     */
				     output.close();
				}
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
}
