package sata.metastock.robos;

import java.util.Hashtable;

import sata.domain.interfaces.IBuscaCotacao;
import sata.metastock.http.HTTPSata;

public class BuscaCotacao implements IBuscaCotacao {
	

	@Override
	public String getCotacao(String codigo) {
		String cotacao;
		Hashtable<String, String> h = new Hashtable<String, String>();
//		h.put("intIdiomaXsl", "0");
//		h.put("txtCodigo", codigo);
//		h.put("image.x", "1");
//		h.put("image.y", "13");
	
//		String html = HTTPSata.POST(
//				"http://www.bmfbovespa.com.br/Pregao-online/ExecutaAcaoCotRapXSL.asp?gstrCA=&intIdiomaXsl=0",
//				h);
		String html = HTTPSata.POST(
				"http://www.bmfbovespa.com.br/Pregao-online/ExecutaAcaoCotRapXSL.asp?txtCodigo=" + codigo,
				h);
		

		int corte = html.indexOf(codigo.toUpperCase());
		if (corte < 0) {
			cotacao = "9999,99";
		} else {
			html = html.substring(corte);

			int inicio = html.indexOf("R$");
			int fim = html.indexOf("</td>", inicio);

			cotacao = html.substring(inicio + "R$ ".length(), fim);
		}
		
		return cotacao;
	}
	
	public static void main(String[] args) {
		BuscaCotacao bc = new BuscaCotacao();
		bc.getCotacao("PETRA23");
	}
	
}
