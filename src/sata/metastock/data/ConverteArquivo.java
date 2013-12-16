/*
 * Created on 09/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.data;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import sata.metastock.util.AcaoBovespa;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConverteArquivo {
	
	
	
	public static ArrayList convete(String arq){
	
		ArrayList acoes = new ArrayList();
		FileInputStream file;
		ArrayList acoesOrdenada = null;
		
		try {

			file = new FileInputStream("C:\\PRIVADO\\flavio\\bovespa\\historico\\" + arq + "\\" + arq + ".txt");
			//file = new FileInputStream("C:\\PRIVADO\\flavio\\bovespa\\historico\\COTAHIST_A2001\\COTAHIST_A2001.txt");
			DataInputStream myInput = new DataInputStream(file);
				
		     String thisLine = "";
		     		     
		     while ((thisLine = myInput.readLine()) != null){         	
		     	
		     	if(thisLine.substring(0,2).equals("01")){
		     	
			     	AcaoBovespa acao = new AcaoBovespa();		
			     			     	
			     	acao.setData(thisLine.substring(2,10).trim());  //data
			     	acao.setCodigo(thisLine.substring(12,24).trim()); //código
			     	acao.setEmpresa(thisLine.substring(27,39).trim()); //nome
			     	acao.setTipo(thisLine.substring(39,49).trim()); //tipo
			     	acao.setAbertura("" + Integer.parseInt(thisLine.substring(56,69).trim())); //abertura
			     	acao.setMaxima("" + Integer.parseInt(thisLine.substring(69,82).trim())); //máximo
			     	acao.setMinima("" + Integer.parseInt(thisLine.substring(82,95).trim())); //minimo
			     	acao.setMedia("" + Integer.parseInt(thisLine.substring(95,108).trim()));//medio
			     	acao.setFechamento("" + Integer.parseInt(thisLine.substring(108,121).trim()));//fechamento
			     	acao.setOfertaCompra("" + Integer.parseInt(thisLine.substring(121,134).trim()));//melhor oferta compra
			     	acao.setOfertaVenda("" + Integer.parseInt(thisLine.substring(134,147).trim()));//melhor oferta venda
			     	acao.setNegs("" + Integer.parseInt(thisLine.substring(147,152).trim()));//numero de negociações
			     	acao.setQtd("" + Long.parseLong(thisLine.substring(170,188).trim()));//volume
			     	
			     	
			     	if(acao.getCodigo().equalsIgnoreCase("PETR4")){
			     		
			     		acoes.add(acao);
			     	}
			     	
			     	
				   /*  String novaLinha = acao.getCodigo() + acao.getData() + "," + acao.getAbertura().replace(',','.') + ","  +
						acao.getMaxima().replace(',','.') + "," + acao.getMinima().replace(',','.') + 
						"," + acao.getFechamento().replace(',','.') + 	"," + acao.getQtd() + "," + acao.getFechamento();
*/
				    // System.out.println(novaLinha);
		     	}
		     }
		     
		     acoesOrdenada = ordena(acoes);
		     
		     for(int i=0;i<acoesOrdenada.size();i++){
		    	 
		    	 AcaoBovespa acao = (AcaoBovespa)acoesOrdenada.get(i);
		   /* 	 String novaLinha = acao.getCodigo() + acao.getData() + "," + acao.getAbertura().replace(',','.') + ","  +
					acao.getMaxima().replace(',','.') + "," + acao.getMinima().replace(',','.') + 
					"," + acao.getFechamento().replace(',','.') + 	"," + acao.getQtd() + "," + acao.getFechamento();
*/
		    	 String novaLinha = acao.getCodigo() + " " + acao.getFechamento() + " " + acao.getData();
 		    	 
			    // System.out.println(novaLinha);
		     }
		     
		     file.close();
		     myInput.close();
		     
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(acoes.size());
		return acoesOrdenada;
	}

	
	public static ArrayList ordena(ArrayList acoes){
		
		
		ArrayList ordenada = new ArrayList();
		
		int x = acoes.size();
				
		for(int j=0;j<x;j++){
			long menor = 99999999; 
			AcaoBovespa acaoMenor = null;
			int indice = 0;
			for(int i=0;i<acoes.size();i++){
				
				AcaoBovespa acao = (AcaoBovespa)acoes.get(i);
				if(Long.parseLong(acao.getData())<menor){
					
					menor = Long.parseLong(acao.getData());
					acaoMenor = acao;
					indice = i;
				}
				
			}
			ordenada.add(acaoMenor);
			acoes.remove(indice);
		}
		
		return ordenada;
	}
}
