package sata.metastock.opcoes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sata.metastock.bovespa.Menu;


public class BoiBaster {

/*	
	public static double cotacaoBase = 45.2;
	public static double cotacaoBaseOpcaoVendida = 1.08;
	public static double cotacaoBaseOpcaoComprada = 0.39;
*/
	
	//public static double cotacaoBase = 37.55;
	public static double cotacaoBaseOpcaoVendida = 1.97;
	public static double cotacaoBaseOpcaoComprada = 1.0;

	public static double cotacaoBase = 40.85;
		
	public static double[] cotacao;
	public static double[] opcao1 ;
	public static double[] opcao2 ;
	public static double[] retornoAcao ;
	public static double[] retornoOpcao ;

	
	public static void main(String args[]){
		
		leArq();
		
		System.out.println(opcao1.length);
		for(int i=0;i<opcao1.length;i++){
			System.out.print(cotacaoBase + " " + cotacaoBaseOpcaoVendida + " " + cotacaoBaseOpcaoComprada + " ");
			System.out.print(opcao1[i] + " " + opcao2[i] + " " + cotacao[i] +" retorno Ação:" );
			retornoAcao[i] = new  Double(new BigDecimal(cotacao[i]-cotacaoBase).divide(new BigDecimal(cotacaoBase),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()).doubleValue();
			System.out.print(retornoAcao[i] + " retorno opção:");
			retornoOpcao[i] = calcRetorno(cotacaoBase,cotacaoBaseOpcaoVendida,cotacaoBaseOpcaoComprada,
					opcao1[i],opcao2[i]).doubleValue();
			System.out.println(retornoOpcao[i]);			
		}
		
	    // Create the frame
	    String title = "Gráfico Pontos ";
	    JFrame frame = new JFrame(title);
	    
	    //frame = new JTransFrame(0,0,0);
	    // Create a component to add to the frame
	    JComponent comp = new JTextArea();
	    	    
	    // Show the frame
	    int width = 1024;
	    int height = 730;
	    frame.setSize(width, height);	    
	    
	    frame.getContentPane().add((new BoiBaster()).new Grafico2(), BorderLayout.CENTER);
	    frame.setBackground(Color.WHITE);	    
	    frame.setVisible(true);
			
	}
	
	public static BigDecimal calcRetorno(double cotacaoAcao,double vendido1,double comprado1,double vendido2,double comprado2){
			
		//double taxaInicio = new  Double(new BigDecimal(vendido1).divide(new BigDecimal(comprado1),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()).doubleValue();
		double taxaInicio = 4.0;
		double taxaFim = new  Double(new BigDecimal(vendido2).divide(new BigDecimal(comprado2),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()).doubleValue();
		double comprouLotes = 1000*taxaInicio;
		double vendeu = comprado2*comprouLotes;
		double comprou = 1000*vendido2;
		double sobrou = vendeu - comprou;
		return new BigDecimal(sobrou).divide(new BigDecimal(1000*cotacaoAcao),BigDecimal.ROUND_HALF_EVEN,6).multiply(new BigDecimal(100));
		
	}
	
	public class Grafico2 extends JPanel{
		
		public void paintComponent(Graphics g) {
			
			g.setColor(Color.BLACK);
			
			//System.out.println(new Double(retornoAcao[5]*1000).intValue());
			
			g.drawLine(0,400,2000,400);
			g.drawLine(100,0,100,600);
			
			g.setColor(Color.RED);
			
			for(int i=10;i<400;i+=10){
				g.drawLine(97,400-i,103,400-i);
				
			}
			
			for(int i=10;i<400;i+=10){
				g.drawLine(100+i,400-3,100+i,400+3);			
			}
			
			g.setColor(Color.blue);
			
			for(int i=0;i<retornoOpcao.length;i++){
				g.drawLine(100 + new Double(retornoAcao[i]*1000).intValue(),400-new Double(retornoOpcao[i]*10).intValue(),
						100 + new Double(retornoAcao[i]*1000).intValue(),400-new Double(retornoOpcao[i]*10).intValue());	
			}	
		}	
	} 
	
	public static void leArq(){
			
			FileInputStream file;

			try {
				file = new FileInputStream("D:\\flavio\\bolsa\\valoresOpcao2.txt");
				DataInputStream myInput = new DataInputStream(file);
				
				
		        String thisLine = "";
		        int i=0;
		        while ((thisLine = myInput.readLine()) != null){         	
		        	i++;
		        }
		        myInput.close();
		        file.close();
		           
		        
		    	cotacao = new double[i]; 
		    	opcao1  = new double[i];
		    	opcao2  = new double[i];
		    	retornoAcao = new double[i];
		    	retornoOpcao = new double[i];
		        
		        
		        file = new FileInputStream("D:\\flavio\\bolsa\\valoresOpcao2.txt");
		        DataInputStream myInput2 = new DataInputStream(file);
		       
		        
		        i=0;
		        while ((thisLine = myInput2.readLine()) != null){ 	
		        	opcao1[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(" ")).replace(',','.'));
		        	thisLine = thisLine.substring(thisLine.indexOf(" ")+1);  
		        	opcao2[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(" ")).replace(',','.'));
		        	thisLine = thisLine.substring(thisLine.indexOf(" ")+1);
		        	cotacao[i] = Double.parseDouble(thisLine.replace(',','.'));
		        	i++;
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
		
}
