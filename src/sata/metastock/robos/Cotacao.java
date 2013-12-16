/*
 * Created on 26/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;

import sata.metastock.mail.Email;



/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Cotacao {

	private static String cotacao = "6,41";
	private static String horario = "07:00:00";
	
	public static void get(String ativo){
		
		
			try {
				
			    String link = "http://www.bovespa.com.br/Home/FormConsultaCotacaoRapida.asp?Pagina=" + "/Cotacoes2000/CotacaoRapidaHome.Asp" +"&PaginaReferente=PosicaoGeralPregao.Asp&txtCodigo="+ ativo;// +'&<%=strParamURL%>';
	
				URL url = new URL(link);
				System.out.println(link);
				String thisLine;
		        DataInputStream myInput = new DataInputStream(url.openStream());
		        String html = "";
		        String line = "";
		        while ((line = myInput.readLine()) != null){ 
		       	    
		       	    html += line + "\n";
		        }    
		        		        
		       // System.out.println(html);
		        
		        int corte = html.indexOf("Atraso de 15 minutos");
		        if(corte < 0){
		        	horario = "07:00:00";
		        	cotacao = "61,11";
		        }else{
			        html = html.substring(corte);
			        
			        int inicioTempo = html.indexOf("align=\"right\">");
			        int fimTempo = html.indexOf("&nbsp;&nbsp;");
			        
			        horario = html.substring(inicioTempo + "align=\"right\">".length(),fimTempo);
			        
			        int inicio = html.indexOf("R$&nbsp;");
			        int fim = html.indexOf("</font>",inicio);
			        
			        cotacao = html.substring(inicio + "R$&nbsp;".length(),fim);

		        }
		       // System.out.println(cotacao + " " + horario);
		        
		          
		        
	        
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
	}
	
	/**
	 * @return Returns the cotacao.
	 */
	public static String getCotacao() {
		return cotacao;
	}
	/**
	 * @param cotacao The cotacao to set.
	 */
	public static void setCotacao(String cotacao) {
		Cotacao.cotacao = cotacao;
	}
	/**
	 * @return Returns the horario.
	 */
	public static String getHorario() {
		//System.out.println(horario);
		return horario;
	}
	/**
	 * @param horario The horario to set.
	 */
	public static void setHorario(String horario) {
		Cotacao.horario = horario;
	}
}
