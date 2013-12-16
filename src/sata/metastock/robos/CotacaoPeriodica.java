/*
 * Created on 27/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import javax.mail.MessagingException;

import sata.metastock.mail.Email;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CotacaoPeriodica {

	
	public static void main(String args[]){
		
		String ativo = "VIVO4";
		for(int i=0;i<2;i++){
			Cotacao.get(ativo);
			
			try {
				new Email().sendSSLMessage(new String[]{"flaviogc@gmail.com"}, "Cotação "+ ativo,
						Cotacao.getCotacao() + " " + Cotacao.getHorario(), "flaviogc@gmail.com");
				Thread.sleep(900000);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}

		
	}
}
