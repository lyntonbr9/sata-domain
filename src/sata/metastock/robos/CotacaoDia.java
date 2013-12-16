/*
 * Created on 02/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import sata.metastock.util.AcaoBovespa;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CotacaoDia {

	private static ArrayList cotacoes = new ArrayList();
	
	public static void main(String args[]){
		
		try {
			
			//System.setProperty("http.proxyHost", "172.31.9.149");
			System.setProperty("http.proxyHost", "172.31.2.218");
	   	    System.setProperty("http.proxyPort", "8080"); 
			
			System.out.println("Conectando...");
		    String link = "http://www.uol.com.br";
			URL url = new URL(link);
			//System.out.println();
			String thisLine;
	        DataInputStream myInput = new DataInputStream(url.openStream());
	        String linha ="";
	        while(linha!=null){
	        	linha = myInput.readLine();
	        	System.out.println(linha);
	        }
	        
	        
	        
	        
	/*        System.out.println("Recuperou página....");
	        StringBuffer html = new StringBuffer();
	        String line = "";
	        
	        
	        BufferedReader d = new BufferedReader(new InputStreamReader(url.openStream()));

	        System.out.println("concatenando HTML...");
	        while ((line=d.readLine())!=null ){ 
	       	   // System.out.println(line);
	       	    html.append(line + "\n");
	        }    
	        
	        System.out.println("Extraindo valores...");
	        //extraiValores(html.toString());
	        
	        System.out.println(html);      */
	                  
	                
	} catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	
	
	}

	/**
	 * 
	 * @param mes 1 - 31
	 * @param dia 1 - 12
	 */
	public static void getCotacaoDia(int mes,int dia){
		
		try {
			
			String data = "";
			
			if(mes<10){
				data = "0" + mes;
			}else{
				data = "" + mes;
			}
			
			if(dia<10){
				data += "0" + dia;
			}else{
				data += "" + dia;
			}
					
			System.out.println("Conectando...");
			System.setProperty("http.proxyHost", "172.31.2.219");
            System.setProperty("http.proxyPort", "8080"); 
		    String link = "http://www.bovespa.com.br/InstDados/Negociacao/ltp0" + data + ".htm";

			URL url = new URL(link);
			//System.out.println();
			String thisLine;
	      //  DataInputStream myInput = new DataInputStream(url.openStream());
	        System.out.println("Recuperou página....");
	        StringBuffer html = new StringBuffer();
	        String line = "";
	       
	        BufferedReader d = new BufferedReader(new InputStreamReader(url.openStream()));

	        System.out.println("concatenando HTML...");
	        while ((line=d.readLine())!=null ){ 
	       	   // System.out.println(line);
	       	    html.append(line + "\n");
	        }    
	        
	        System.out.println("Extraindo valores...");
	        extraiValores(html.toString());
	        
	  //      System.out.println(html);      
	                  
	                
	} catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	
	
	}
	
	public static void extraiValores(String html){
		
		System.out.println(html);
		cotacoes = new ArrayList();
		
		String  resto = html;
		
		int corte1 = resto.indexOf("<tr valign=top align=right bgcolor=white>");
		
		while(corte1!=-1){
			resto = resto.substring(corte1);
			
			AcaoBovespa acao = new AcaoBovespa();
			
			for(int i=0;i<3;i++){
				int inicio = resto.indexOf("<td align=\"left\"><font class=\"fbd\">") + "<td align=\"left\"><font class=\"fbd\">".length();
				int fim = resto.indexOf("</font></td>");
				//System.out.println(inicio + " " + fim);
				String conteudo = resto.substring(inicio,fim).trim();
				System.out.println(conteudo);
				conteudo.replaceAll("        #","");
				if(i==0){
					acao.setCodigo(conteudo);
				}else if(i==1){
					acao.setEmpresa(conteudo);
				}else if(i==2){
					acao.setTipo(conteudo);
				}
				resto = resto.substring(fim + "</font></td>".length());
				System.out.print(conteudo + " ");
			}
			
			for(int i=0;i<10;i++){
				int inicio = resto.indexOf("<td><font class=\"fbd\">") + "<td><font class=\"fbd\">".length();
				int fim = resto.indexOf("</font></td>");
				//System.out.println(inicio + " " + fim);
				String conteudo = resto.substring(inicio,fim).trim();
				if(i==0){
					acao.setAbertura(conteudo.replaceAll(",",""));
				}else if(i==1){
					acao.setMinima(conteudo.replaceAll(",",""));
				}else if(i==2){
					acao.setMaxima(conteudo.replaceAll(",",""));
				}else if(i==3){
					acao.setMedia(conteudo.replaceAll(",",""));
				}else if(i==4){
					acao.setFechamento(conteudo.replaceAll(",",""));
				}else if(i==5){
					acao.setOscilacao(conteudo);
				}else if(i==6){
					acao.setOfertaCompra(conteudo.replaceAll(",",""));
				}else if(i==7){
					acao.setOfertaVenda(conteudo.replaceAll(",",""));
				}else if(i==8){
					acao.setNegs(conteudo);
				}else if(i==9){
					acao.setQtd(conteudo);
				}
				
				resto = resto.substring(fim + "</font></td>".length());
				System.out.print(conteudo + " ");
			}
			
			cotacoes.add(acao);
			
			corte1 = resto.indexOf("<tr valign=top align=right bgcolor=white>");
			System.out.println();
		}
	
	} 
	
	
	

	/**
	 * @return Returns the cotacoes.
	 */
	public static ArrayList getCotacoes() {
		return cotacoes;
	}
	/**
	 * @param cotacoes The cotacoes to set.
	 */
	public static void setCotacoes(ArrayList cotacoes) {
		CotacaoDia.cotacoes = cotacoes;
	}
}
