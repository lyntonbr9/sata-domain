/*
 * Created on 07/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.data;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import sata.metastock.Excel;
import sata.metastock.exceptions.CelulaInvalidaEX;
import sata.metastock.robos.CotacaoDia;
import sata.metastock.util.AcaoBovespa;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Merge2 {
	
	public static void main(String args[]){
		//ano,mes,dia
		Merge2.merge(2006,10,25);
		
	}
	
	public static void merge(int ano,int mes, int dia){
	
		
		
		String diaStr = "";
		String mesStr = "";
		
		if(dia<10){
			diaStr = "0" + dia;
		}else{
			diaStr = "" + dia;
		}
		
		if(mes<10){
			mesStr = "0" + mes;
		}else{
			mesStr = "" + mes;
		}
		
		CotacaoDia.getCotacaoDia(mes,dia);
		ArrayList cotacoes = CotacaoDia.getCotacoes();
		
		try {
			
		
			
			for(int j=0;j<cotacoes.size();j++){
				
				AcaoBovespa acao = (AcaoBovespa)cotacoes.get(j);
				
				FileWriter output = null;
				
				File arq = new File("D:\\flavio\\bolsa\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
				//File arq = new File(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
				
				
				if(arq.exists()){
					
					FileInputStream file = new FileInputStream("D:\\flavio\\bolsa\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
					//FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
					DataInputStream myInput = new DataInputStream(file);
						
				     String thisLine = "";
				    
				     double aber = Integer.parseInt(acao.getAbertura().replace(',','.'));
				     aber = new BigDecimal(aber).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double max = Integer.parseInt(acao.getMaxima().replace(',','.'));
					 max = new BigDecimal(max).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double min = Integer.parseInt(acao.getMinima().replace(',','.'));
					 min = new BigDecimal(min).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double fech = Integer.parseInt(acao.getFechamento().replace(',','.'));
					 fech = new BigDecimal(fech).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 
				     String novaLinha = acao.getCodigo() + ",D," + ano + mesStr + diaStr + ",000000"  + "," + aber + ","  +
  								max + "," + min + "," + fech + 	"," + acao.getQtd() + "," + "0";

				     
				     StringBuffer saida = new StringBuffer();
				     
				     
				     while ((thisLine = myInput.readLine()) != null){         	
				     	saida.append(thisLine + "\n");
				       	
				     }
				     saida.append(novaLinha + "\n");
				     myInput.close();	
					 output = new FileWriter("D:\\flavio\\bolsa\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");

					 //output = new FileWriter(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
				     
				     output.write(saida.toString());
				     		     
				     output.close();
				}else{
					
				     double aber = Integer.parseInt(acao.getAbertura().replace(',','.'));
				     aber = new BigDecimal(aber).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double max = Integer.parseInt(acao.getMaxima().replace(',','.'));
					 max = new BigDecimal(max).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double min = Integer.parseInt(acao.getMinima().replace(',','.'));
					 min = new BigDecimal(min).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 double fech = Integer.parseInt(acao.getFechamento().replace(',','.'));
					 fech = new BigDecimal(fech).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_DOWN,4).doubleValue();
					 
				     String novaLinha = acao.getCodigo() + ",D," + ano + mesStr + diaStr + ",000000"  + "," + aber + ","  +
  								max + "," + min + "," + fech + 	"," + acao.getQtd() + "," + "0";

				     StringBuffer saida = new StringBuffer();
		     
				     saida.append(novaLinha + "\n");
				     output = new FileWriter("D:\\flavio\\bolsa\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
				    // output = new FileWriter(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao.getCodigo() + "D.txt");
				
				     output.write(saida.toString());
				     output.close();
				}
			}
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
	
	}
	
	public static String getDataString(int ano,int mes, int dia){
		
		String diaStr = "";
		String mesStr = "";
			
		if(dia<10){
			diaStr = "0" + dia;
		}else{
			diaStr = "" + dia;
		}
		
		if(mes == 1){
			mesStr = "Jan";
		}else if(mes==2){
			mesStr = "Feb";
		}else if(mes==3){
			mesStr = "Mar";
		}else if(mes==4){
			mesStr = "Apr";
		}else if(mes==5){
			mesStr = "May";
		}else if(mes==6){
			mesStr = "Jun";
		}else if(mes==7){
			mesStr = "Jul";
		}else if(mes==8){
			mesStr = "Aug";
		}else if(mes==9){
			mesStr = "Sep";
		}else if(mes==10){
			mesStr = "Oct";
		}else if(mes==11){
			mesStr = "Nov";
		}else if(mes==12){
			mesStr = "Dec";
		}
		
		return diaStr + "-" + mesStr + "-0" + ano;
	}
	

}
