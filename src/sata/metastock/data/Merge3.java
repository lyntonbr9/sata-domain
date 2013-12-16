/*
 * Created on 09/09/2006
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

import sata.metastock.robos.CotacaoDia;
import sata.metastock.util.AcaoBovespa;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Merge3 {
	public static void main(String args[]){
		
		//Merge3.merge();
		Merge3.uneBases();
		//Merge3.ordena();
		
	}
	
	public static void ordena(){
		
		File dir = new File("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2");

		String[] acoes = dir.list();
		ArrayList cotacoes = new ArrayList();
		
		for(int i=0;i<acoes.length;i++){
			
			FileInputStream file;
			try {
				file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acoes[i]);
				DataInputStream myInput = new DataInputStream(file);
				
				String thisLine = "";
		    
				System.out.println("recupeando arquivo... " + acoes[i]);
				cotacoes.clear();
			     while ((thisLine = myInput.readLine()) != null){         	
			     	
			     	cotacoes.add(thisLine);
			       	
			     }
			     
			     System.out.println("ordenando... " + acoes[i]);
			     posiciona(cotacoes);
			     
			     StringBuffer str = new StringBuffer();
			     str.append("<TICKER>,<PER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>");
			     for(int j=0;j<cotacoes.size();j++){
			     	str.append((String)cotacoes.get(j) + "\n");
			     }
			     
			     System.out.println("escrevendo... " + acoes[i]);
			     FileWriter output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acoes[i]);
					
			     
			     output.write(str.toString());
			    
			     				    
			     		     
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
	
	public static ArrayList eliminaRepetidos(ArrayList cot){
		
		ArrayList cot2 = new ArrayList();
		
		for(int i=0;i<cot.size();i++){
			
			String linha = (String)cot.get(i);
			int virgula2Pos = linha.indexOf(',',linha.indexOf(',')+1);
			
			long str1 = Long.parseLong(linha.substring(virgula2Pos+1,virgula2Pos+9));
			
			boolean repetido = false;
			for(int j=0;j<cot2.size();j++){
				
				
				String linha2 = (String)cot.get(j);
				virgula2Pos = linha2.indexOf(',',linha2.indexOf(',')+1);
				
				long str2 = Long.parseLong(linha2.substring(virgula2Pos+1,virgula2Pos+9));

				if(str1 == str2){
					repetido = true;
				}
				
				
			}
			
			if(!repetido){
				cot2.add(linha);
			}
			
		}
		
		return cot2;
		
	}
	
	private static void uneBases(){
		
		File dir = new File("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2");

		String[] acoes = dir.list();
		ArrayList cotacoes = new ArrayList();
		
		for(int i=0;i<acoes.length;i++){
			
			FileInputStream file;
			FileInputStream file2;
			try {
				file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acoes[i]);
				DataInputStream myInput = new DataInputStream(file);
				
				String thisLine = "";
		    
				System.out.println("recupeando arquivo... " + acoes[i]);
				cotacoes.clear();
				
			    while ((thisLine = myInput.readLine()) != null){         	
			     	
			    	cotacoes.add(thisLine);
			       	
			    }
			    
				File arq = new File("H:\\flavio\\bolsa\\bovespa\\historico\\BaseMeta\\" + acoes[i].replaceAll(".txt","D.txt"));

			    if(arq.exists()){
					file2 = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseMeta\\" + acoes[i].replaceAll(".txt","D.txt"));
					DataInputStream myInput2 = new DataInputStream(file2);
					
					thisLine = "";
			    
					System.out.println("recupeando arquivo... " + acoes[i]);
					
					myInput2.readLine(); // Despreza cabeçalho 
				    while ((thisLine = myInput2.readLine()) != null){         	
				     	
				    	cotacoes.add(0,thisLine);
				       	
				    }
			    }
			    
			     ArrayList novasCotacoes = eliminaRepetidos(cotacoes);
			     posiciona(novasCotacoes);
			     
			     StringBuffer str = new StringBuffer();
			     str.append("<TICKER>,<PER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,<OPENINT>\n");
			     for(int j=0;j<novasCotacoes.size();j++){
			     	str.append((String)novasCotacoes.get(j) + "\n");
			     }
			     
			     System.out.println("escrevendo... " + acoes[i]);
			     FileWriter output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa3\\" + acoes[i].replaceAll(".txt","D.txt"));
					
			     
			     output.write(str.toString());
			    
			     				    
			     		     
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
	
	
	private static void posiciona(ArrayList cot){
		
		 
		
		for(int i=0;i<cot.size();i++){
			long menorStr = Long.MAX_VALUE;
			for(int j=i;j<cot.size();j++){
				
				
				String linha = (String)cot.get(j);
				int virgula2Pos = linha.indexOf(',',linha.indexOf(',')+1);
				
				long str1 = Long.parseLong(linha.substring(virgula2Pos+1,virgula2Pos+9));
				
				if(str1<menorStr){
					String temp = (String)cot.remove(j);
					cot.add(i,temp);
					menorStr = str1;
				}
				
			}
			
			
		}
		
		
	}
	
	
	public static void merge(){
	
		
		
		System.out.println("Extraindo dados.....");
		ArrayList cotacoes = ConverteArquivo.convete("");
		System.out.println("Gerando base local.....");
		
		try {
			
		
			
			for(int j=0;j<cotacoes.size();j++){
				
				AcaoBovespa acao = (AcaoBovespa)cotacoes.get(j);
				
				FileWriter output = null;
				
				File arq = new File("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acao.getCodigo() + ".txt");

				System.out.println(acao.getCodigo());
				if(arq.exists()){
					
					FileInputStream file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acao.getCodigo() + ".txt");
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
					 
				     String novaLinha = acao.getCodigo() + ",D," + acao.getData() + ",000000"  + "," + aber + ","  +
  								max + "," + min + "," + fech + 	"," + acao.getQtd() + "," + "0";
				     StringBuffer saida = new StringBuffer();
				     
				     saida.append(novaLinha + "\n");
				     while ((thisLine = myInput.readLine()) != null){         	
				     	saida.append(thisLine + "\n");
				       	
				     }
				     myInput.close();	
					output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acao.getCodigo() + ".txt");
						
				     
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
					 
				     String novaLinha = acao.getCodigo() + ",D," + acao.getData() + ",000000"  + "," + aber + ","  +
  								max + "," + min + "," + fech + 	"," + acao.getQtd() + "," + "0";
					
				     StringBuffer saida = new StringBuffer();
		     
				     saida.append(novaLinha + "\n");
				     output = new FileWriter("H:\\flavio\\bolsa\\bovespa\\historico\\BaseBovespa2\\" + acao.getCodigo() + ".txt");
				
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
