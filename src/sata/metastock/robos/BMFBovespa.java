package sata.metastock.robos;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.OpcaoTO;
import sata.domain.util.SATAPropertyLoader;
import sata.domain.util.SATAUtil;
import sata.metastock.http.HTTPSata;

public class BMFBovespa {

	public static void atualizarCadastroOpcoes() throws IOException, ParseException, SQLException {
		for (OpcaoTO opcao : getOpcoes()) {
			SATAFactoryFacade.getOpcaoDAO().salvar(opcao);
		}
	}

	public static List<OpcaoTO> getOpcoes() throws IOException, ParseException {
		List<OpcaoTO> opcoes = new ArrayList<OpcaoTO>();
		for (Acao acao : AcaoConteiner.getAcoes()) {
			opcoes.addAll(getOpcoes(acao));
		}
		return opcoes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<OpcaoTO> getOpcoes(Acao acao) throws IOException, ParseException {
		List<OpcaoTO> opcoes = new ArrayList<OpcaoTO>();

		if (acao != null) {
			Hashtable h = new Hashtable();
			h.put("__EVENTTARGET","ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$rptResultadoBuscaEmpresa$ctl03$lnkEmpresa");
			h.put("__EVENTARGUMENT","");
			h.put("__VIEWSTATE",SATAPropertyLoader.getProperty("viewState"));
			h.put("ctl00$ucTopo$btnBusca","Busca");
			h.put("ctl00$menuBOVESPASecundario","");
			h.put("ctl00$contentPlaceHolderConteudo$tabOpcoes","{\"State\":{},\"TabState\":{\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabPosicoesAberto\":{\"Selected\":true},\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabPosicoesAberto_tabOpcoesEmp\":{\"Selected\":true},\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabSeriesAutorizadas_tabSeriesAutEmp\":{\"Selected\":true}}}");
			h.put("ctl00$contentPlaceHolderConteudo$mpgOpcoes_Selected","0");
			
			String url = "http://www.bmfbovespa.com.br/opcoes/opcoes.aspx?Idioma=pt-br";

			String html = SATAUtil.removeExcessoEspacos(HTTPSata.POST(url, h));

			int corte = html.indexOf("de Compra</h2>");
			if (corte > -1) {
				html = html.substring(corte, html.indexOf("de Venda</h2>"));
				int corteSerie = html.indexOf(acao.getTipo());
				do {
					String htmlSerie = html.substring(corteSerie);
					String dataSerie = htmlSerie.substring(htmlSerie.indexOf("Vencimento:")+11, htmlSerie.indexOf("</a>")).trim();
					System.out.println("dataSerie="+dataSerie);
					htmlSerie = htmlSerie.substring(htmlSerie.indexOf("<td align=\"center\">"),htmlSerie.indexOf("</li>"));
					int corteOpcao = 0;
					do {
						String htmlOpcao = htmlSerie.substring(corteOpcao+19, htmlSerie.indexOf("</tr>", corteOpcao));
						String codOpcao = htmlOpcao.substring(0, htmlOpcao.indexOf("</td>")).trim();
						htmlOpcao = htmlOpcao.substring(htmlOpcao.indexOf("<td align=\"right\">"));
						BigDecimal precoExercicio = new BigDecimal(htmlOpcao.substring(18,htmlOpcao.indexOf("</td>")).trim().replace(",", "."));
						htmlOpcao = htmlOpcao.substring(htmlOpcao.indexOf("<td align=\"right\">",19));
						int coberto = Integer.parseInt(htmlOpcao.substring(18,htmlOpcao.indexOf("</td>")).trim().replace(".", ""));
						corteOpcao = htmlSerie.indexOf("<td align=\"center\">",corteOpcao+19);
						if (coberto != 0 && !codOpcao.contains(" E")) {
							OpcaoTO opcao = new OpcaoTO();
							opcao.setAcao(acao);
							opcao.setCodigo(codOpcao);
							opcao.setPrecoExercicio(precoExercicio);
							opcao.setDataVencimento(SATAUtil.converteToDate(dataSerie));
							opcoes.add(opcao);
							System.out.println("codOpcao="+codOpcao+"; precoEx="+precoExercicio+"; coberto="+coberto);
						}
					} while(corteOpcao > -1);

					corteSerie = html.indexOf(acao.getTipo(), corteSerie+2);
				} while (corteSerie > -1);
			}
		}
		System.out.println("Atualizando opcoes da " + acao.getNome() + " ...");
		return opcoes;
	}

	public static void main(String[] args) throws IOException, ParseException, SQLException {
//		atualizarCadastroOpcoes();
		Acao acao = new Acao();
		acao.setNomeEmpresa("PETROBRAS");
		acao.setTipo("PN");
		List<OpcaoTO> opcoes = new ArrayList<OpcaoTO>();
		opcoes.addAll(getOpcoes(acao));
	}
}
