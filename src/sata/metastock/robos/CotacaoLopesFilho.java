/*
 * Created on 02/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.robos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

import sata.domain.util.SATAUtil;
import sata.metastock.http.HTTPSata;

/**
 * @author Flavio
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CotacaoLopesFilho {

	private static String cotacao = "43,80";
	private static String hora = "07:00:00";
	
	public static BigDecimal getCotacao(String codigo) {
		get(codigo);
		return new BigDecimal(cotacao.replace(".","").replace(",", "."));
	}

	public static void get(String codigo) {
		// Populate the hashtable with key value pairs of
		// the parameter name and
		// value. In this case, we only have the parameter
		// named "CONTENT" and the
		// value of CONTENT will be "HELLO JSP !"

		Hashtable h = new Hashtable();
		h.put("papel", codigo);
		// h.put("ONEMORECONTENT", "HELLO POST !");

		// POST it !		
		String html = HTTPSata.POST(
				"https://www.ondeinvestirbylopesfilho.com.br/cli/agr/cot/cotacao.asp",
				h);

		// System.out.println(html);

		int corte = html.indexOf(codigo.toUpperCase());
		if (corte < 0) {

			cotacao = "61,11";
		} else {
			html = html.substring(corte);

			int inicio = html.indexOf("style=\"font-size:11px!important;\">");
			int fim = html.indexOf("</td>", inicio);

			cotacao = html.substring(inicio
					+ "style=\"font-size:11px!important;\">".length(), fim);
		}

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-03:00"));

		String hour24 = "" + cal.get(Calendar.HOUR_OF_DAY); // 0..23
		String min = "" + cal.get(Calendar.MINUTE); // 0..59
		String sec = "" + cal.get(Calendar.SECOND); // 0..59

		if (hour24.length() == 1) {
			hour24 = "0" + hour24;
		}

		if (min.length() == 1) {
			min = "0" + min;
		}

		if (sec.length() == 1) {
			sec = "0" + sec;
		}

		hora = hour24 + ":" + min + ":" + sec;

	}

	/**
	 * @return Returns the cotacao.
	 */
	public static String getCotacao() {
		return cotacao;
	}

	/**
	 * @param cotacao
	 *            The cotacao to set.
	 */
	public static void setCotacao(String cotacao) {
		CotacaoLopesFilho.cotacao = cotacao;
	}

	/**
	 * @return Returns the hora.
	 */
	public static String getHora() {
		return hora;
	}

	/**
	 * @param hora
	 *            The hora to set.
	 */
	public static void setHora(String hora) {
		CotacaoLopesFilho.hora = hora;
	}
}
