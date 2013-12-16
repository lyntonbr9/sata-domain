package sata.metastock.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.mail.MessagingException;

import sata.metastock.mail.Email;
import sata.metastock.mail.SendMailUsingAuthentication;
import sata.metastock.robos.CotacaoLopesFilho;

public class SimulaGanhoOpcoes {

	static String[] opcoesPETR4 = { "petri16", "petri18", "petri19", "petri20",
			"petri21", "petrj19", "petrj20", "petrj21", "petrj22" };
	
	static double[] strikesPETR4 = { 16.00, 17.83, 19.00, 19.83, 20.83,
		19.00, 19.83, 20.83, 21.83 };

/*	static double[] strikesPETR4 = { 20.00, 22.00, 22.83, 24.00, 24.83, 20.00,
			22.00, 23.00, 24.00, 25.00 }; */

	static String[] opcoesVALE5 = { "valei37", "valei38", "valei39", "valei40",
			"valej36", "valej38", "valej40", "valej41", "valej43" };
	
	static double[] strikesVALE5 = { 36.00, 38.00, 39.00, 40.00, 36.00, 38.00,
			40.00, 41.00, 43.00 };

	static String[] emails = { "flaviogc@gmail.com",
			"flaviogc@br-petrobras.com.br", "tobebendo@gmail.com" };
	
	static int precisao = 3;
	
	private static String getResultadoSimulacao(String codigoAcao, String[] opcoesDaAcao, double[] strikesDasOpcoes){
		
		String resultado = "";
		String msgTemp="";
		CotacaoLopesFilho.get(codigoAcao);

		String cotacaoAcao=CotacaoLopesFilho.getCotacao();
		System.out.println("Cotação " + codigoAcao + ": " +  cotacaoAcao + "\n");
		resultado = "Cotação " + codigoAcao + ": " +  cotacaoAcao + "\n";
		BigDecimal precoCompraAcao = new BigDecimal(cotacaoAcao.replace(',', '.'));

		for (int i = 0; i < opcoesDaAcao.length; i++) {

			System.out.print(opcoesDaAcao[i]);
			resultado += opcoesDaAcao[i];

			CotacaoLopesFilho.get(opcoesDaAcao[i]);
			String cotacaoOpcao = CotacaoLopesFilho.getCotacao();
			System.out.println(": " + cotacaoOpcao);
			resultado += ": " + cotacaoOpcao + " ";

			BigDecimal precoOpcao = new BigDecimal(cotacaoOpcao.replace(',', '.'));

			BigDecimal VE = null;
			if (precoCompraAcao.doubleValue() <= strikesDasOpcoes[i]) {
				VE = precoOpcao;
			} else {
				VE = new BigDecimal(strikesDasOpcoes[i]).add(precoOpcao).subtract(precoCompraAcao);
			}
			System.out.println("VE: " + VE);
			resultado += "Strike: " + strikesDasOpcoes[i] + " VE: " + VE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + " ";

			BigDecimal prcVE = VE.divide(precoCompraAcao,BigDecimal.ROUND_HALF_EVEN, precisao).multiply(new BigDecimal(100));
			msgTemp = "PRC_VE: " + prcVE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + "% ";
			System.out.println(msgTemp);
			resultado += msgTemp;

			BigDecimal VI = null;
			if (precoCompraAcao.doubleValue() - strikesDasOpcoes[i] <0)
				VI = new BigDecimal(0);
			else
				VI = precoCompraAcao.subtract(new BigDecimal(strikesDasOpcoes[i]));

			System.out.println("VI: " + VI);
			resultado += "VI: " + VI.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + " ";

			//porcentagem do VI em relacao a acao
			BigDecimal prcVI = VI.divide(precoCompraAcao,BigDecimal.ROUND_HALF_EVEN, precisao).multiply(new BigDecimal(100));
			msgTemp = "PRC_VI: " + prcVI.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + "% ";
			System.out.println(msgTemp);
			resultado += msgTemp;
			
			//razao do VI com o VE
			
			String alerta=" ";
			BigDecimal razaoVI_VE;
			if(VE.doubleValue() != 0.0)
				razaoVI_VE = VI.divide(VE, BigDecimal.ROUND_HALF_EVEN, precisao);
			else
				razaoVI_VE = new BigDecimal(0);
			if (razaoVI_VE.doubleValue() >= 2.0 && razaoVI_VE.doubleValue() <= 3.0)
				alerta+= " <========";
			msgTemp = "VI/VE: " + razaoVI_VE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + alerta + "\n";
			System.out.println(msgTemp);
			resultado += msgTemp;
		}
		return resultado;
	}
	
	public static void SimulaGanhoEmOpcoes() throws InterruptedException, MessagingException 
	{
		
		while (true) 
		{
			Date dt = new Date();
			System.out.println("Hora: " + dt.getHours());
//			Thread.currentThread().sleep(60000);

//			if (dt.getHours() >= 10 && dt.getHours() < 18) 
//			{
				String mensagem = "";
				
				mensagem+= getResultadoSimulacao("petr4", opcoesPETR4, strikesPETR4);
				mensagem+= getResultadoSimulacao("vale5", opcoesVALE5, strikesVALE5);

				System.out.println(mensagem);
				
				System.out.println("Vai enviar o e-mail de alerta");
//				SendMailUsingAuthentication.sendEmailSimulaGanhoOpcoes(mensagem);	


				System.out.println("t1");
				Thread.currentThread().sleep(1800000);
				System.out.println("t2");
//			}
		}
	}

	public static void main(String[] args) throws InterruptedException, MessagingException 
	{
		SimulaGanhoEmOpcoes();
	}
}