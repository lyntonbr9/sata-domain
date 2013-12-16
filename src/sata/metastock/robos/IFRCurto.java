/*
 * Created on 27/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;


import javax.mail.MessagingException;

import sata.metastock.indices.IFR;
import sata.metastock.mail.Email;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 11.47 
11.39 
11.39 
11.37 
11.38 
11.34 
11.31 
11.34 
11.37 
11.35 

 */
public class IFRCurto {
	
	private static int horaInicio = 10;
	private static int nIFR = 9;
	private static int periodicidade = 15; //minutos
	private static double close[] = new double[nIFR+1];
	private static double ifrs[] = new double[40];
	private static String ativo = "CCRO3";
	private static int emailCount = 0;
	private static String ifrsDia = "";
	private static String closesDia = "";
	
	public static void main(String args[]){
		
		leArquivo();
		
		int count = 0;
		double ultimaCotacao  = 11.50;
		for(int i=0;i<430;i++){
			CotacaoLopesFilho.get(ativo);
			double cotacao = Double.parseDouble(CotacaoLopesFilho.getCotacao().replace(',','.'));
			String horarioStr = "07:00:00";
			if(!(CotacaoLopesFilho.getHora()== null || CotacaoLopesFilho.getHora().equals(" ") || CotacaoLopesFilho.getHora().equals(""))){
				horarioStr = CotacaoLopesFilho.getHora();
			}
			int horas = Integer.parseInt(horarioStr.substring(0,2)) -  horaInicio;
			int minutos = Integer.parseInt(horarioStr.substring(3,5)) + horas*60;
			
			if(minutos >=0 && minutos<=435){
				
				if(minutos/periodicidade > count){
					
					while(minutos/periodicidade > count){						
						enfileira(ultimaCotacao);
						gravaArquivo(ultimaCotacao + " "  + horarioStr);
						double[] ifr = new IFR(close,nIFR,0.0).getIFR();
						ifrs[count] = ifr[ifr.length-1];
						System.out.println(ifrs[count]);
						ifrsDia +=  ifrs[count] + "\n";
						closesDia += ultimaCotacao + "\n"; 
						enviaEmail("IFR da " + ativo  + " : " +ifrs[count] + " Cotação: " + cotacao + " às " + CotacaoLopesFilho.getHora() + "\n" + ifrsDia + "\n"  + closesDia);
						count++;
					}
					
				}
				ultimaCotacao = cotacao;
				
			}
			
			
			double[] ifr = new IFR(close,nIFR,0.0).getIFR();
			
			
			System.out.println("cotação:" + cotacao +  " " + horarioStr + " IFR 15 minutos: " + ifr[ifr.length-1] + " " + minutos + " " + (minutos/periodicidade));
			
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		}

		
	}
	
	public static void enviaEmail(String texto){
		try {
			
			if(emailCount<40){
				new Email().sendSSLMessage(new String[]{"flaviogc@gmail.com"}, "IFR: "+ ativo,
						 texto, "flaviogc@gmail.com");
				emailCount++;
			}

		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
	public static void enfileira(double valor){
		
		for(int i=0;i<close.length-1;i++){
			close[i] = close[i+1];
		}
		close[close.length-1] = valor;
	}

	public static void leArquivo(){
		
//		 Get all system properties
	    Properties props = System.getProperties();
	    
	    String path = props.getProperty("user.dir");
	    
	    path += "\\cotacao15minutos.txt"; 
	  
	    
	    FileInputStream file;
		try {
			
			file = new FileInputStream(path);
			
			DataInputStream myInput = new DataInputStream(file );
					
	        String thisLine = "";
	        
	        int i=close.length-1;
	        while ((thisLine = myInput.readLine()) != null){         	
	        	String valor = thisLine.substring(0,thisLine.indexOf(" ")).replace(',','.');
	        	if(i>=0){
	        		close[i--]= Double.parseDouble(valor);
	        	}
	        	
	        }
	        
	        for(int j=i;j>0;j++){
	        	close[j] = 1.0;
	        }
	        
	        myInput.close();
	        file.close();
			
	       
	       			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	
	}
	
	
	public static void gravaArquivo(String acresc){
	
//		 Get all system properties
	    Properties props = System.getProperties();
	    
	    String path = props.getProperty("user.dir");
	    
	    path += "\\cotacao15minutos.txt"; 
	   
	    
	    FileInputStream file;
		try {
			
			file = new FileInputStream(path);
			
			DataInputStream myInput = new DataInputStream(file );
					
	        String thisLine = "";
	        String conteudo = "";
	        int i=0;
	        while ((thisLine = myInput.readLine()) != null){         	
	        	conteudo += thisLine + "\n";
	        }
	        myInput.close();
	        file.close();
			
	        System.out.println(conteudo);
	        
	        FileWriter output = null;
			
			File arq = new File(path);
			
			if(arq.exists()){
				output = new FileWriter(path);
				output.write(acresc + "\n" + conteudo);
			}
			output.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
}
