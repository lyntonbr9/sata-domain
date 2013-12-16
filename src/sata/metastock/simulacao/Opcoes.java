package sata.metastock.simulacao;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sata.metastock.data.ConverteArquivo2;
import sata.metastock.util.AcaoBovespa;

public class Opcoes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArrayList vencimento = getDiaVencimentoOpcoes(2007);
		System.out.println("Convertendo");
		ArrayList acao = ConverteArquivo2.converte("PETR4",5);
		System.out.println("Convertido");
		
		String[] letraOpcao = {"B","C","D","E","F","G","H","I","J","K","L"};
		
		System.out.println(vencimento.size() +  "  " + acao.size());
				
		for(int i=0;i<vencimento.size()-1;i++){
			System.out.println("Vencimento: " + vencimento.get(i));
			for(int j=0;j<acao.size();j++){
				
				if(((String)vencimento.get(i)).equals(((AcaoBovespa)acao.get(j)).getData()) &&  ((AcaoBovespa)acao.get(j)).getCodigo().equals("PETR4")){
					
					String dataV = (String)vencimento.get(i);
					int valor = Integer.parseInt(((AcaoBovespa)acao.get(j)).getFechamento());
					int valorInteiro = (valor/100) ;
					
					if(valorInteiro%2 != 0){					
						valorInteiro = valorInteiro -1;
					}
					
					String opcao = "PETR" + letraOpcao[i] + valorInteiro;
					System.out.println("convertendo opção: " + opcao );
					ArrayList vOpcoes = ConverteArquivo2.converte(opcao,7);
					
					for(int k=0;k<vOpcoes.size();k++){
						System.out.println(dataV + " " + ((AcaoBovespa)vOpcoes.get(k)).getCodigo() + " " + ((AcaoBovespa)vOpcoes.get(k)).getData()+ " " + ((AcaoBovespa)vOpcoes.get(k)).getFechamento());
					}
					
					//int vVencOpcao = new BigDecimal(valorInteiro).intValue();
					//BigDecimal vVencOpcao = new BigDecimal(valorInteiro).divide(new BigDecimal(2),BigDecimal.ROUND_HALF_EVEN,6).;
					System.out.print(valor + " " + valorInteiro);
					System.out.println(dataV + " " + ((AcaoBovespa)acao.get(j)).getCodigo() + " " + ((AcaoBovespa)acao.get(j)).getData() );
				}
				
			}
		}
	
	}
		
	public static ArrayList getDiaVencimentoOpcoes(int ano){
		
		 ArrayList vencimento = new ArrayList();
		
		 try {
			    int intervalo = 0;  
			    int i = 0;  
			      
			    Calendar periodo1 = Calendar.getInstance();  
			    periodo1.set(ano,1-1,1);   
			      
			    Calendar periodo2 = Calendar.getInstance();  
			    periodo2.set(ano,12-1,31);   
			      
			    intervalo = (periodo2.get(periodo2.DAY_OF_YEAR) - periodo1.get(periodo1.DAY_OF_YEAR)) + 1;  
			    
			    Locale.setDefault (new Locale ("pt", "BR"));  
				DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");  
			         
				
				 int nSegundas = 0;
				 int mes = (periodo1.get(periodo1.MONTH) + 1);
				 int contMes = mes;
				
				 			 
			     for (;i<intervalo;i++) {  
			    	    	
			    	 mes = (periodo1.get(periodo1.MONTH) + 1);
			    	
			    	  if(contMes != (mes)){
			    		  contMes=mes;
			    		 // System.out.println("Novo mes");
			    		  nSegundas = 0;
			 		  }
			    	 
			    	  String dia  = periodo1.get(periodo1.DATE) + "/" + (periodo1.get(periodo1.MONTH) + 1) + "/" + periodo1.get(periodo1.YEAR); 
			    	  Date dt = df.parse (dia);  
			 		  DateFormat df2 = new SimpleDateFormat ("EEEE");  
			 		//  System.out.println (dia + " " + df2.format (dt)); 
			 		  
			 		  if(df2.format (dt).equals("Segunda-feira")){
			 			 nSegundas++;
			 			if(nSegundas==3){
				 			 
			 				if((periodo1.get(periodo1.MONTH) + 1)<10){
			 					//System.out.println(periodo1.get(periodo1.YEAR) + "0" + (periodo1.get(periodo1.MONTH) + 1) + "" + periodo1.get(periodo1.DATE));
			 					vencimento.add(periodo1.get(periodo1.YEAR) + "0" + (periodo1.get(periodo1.MONTH) + 1) + "" + periodo1.get(periodo1.DATE));
			 				}else{
			 					//System.out.println(periodo1.get(periodo1.YEAR) + "" + (periodo1.get(periodo1.MONTH) + 1) + "" + periodo1.get(periodo1.DATE));
			 					vencimento.add(periodo1.get(periodo1.YEAR) + "" + (periodo1.get(periodo1.MONTH) + 1) + "" + periodo1.get(periodo1.DATE));
			 				}
			 			}
			 		  }
			 		  		 		  
			 		  periodo1.add(periodo1.DATE,1);
			 		 
			     }     
			
			
			 } catch (Exception e) {  
			     // TODO Auto-generated catch block  
			     e.printStackTrace();  
			 }
		
		return vencimento;
	}
	
	
	

}
