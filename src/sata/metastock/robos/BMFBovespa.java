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
			h.put("__EVENTTARGET","");
			h.put("__EVENTARGUMENT","");
			h.put("__LASTFOCUS","");
			h.put("__VIEWSTATE",SATAPropertyLoader.getProperty("viewState"));
			h.put("ctl00$ucTopo$btnBusca","Busca");
			h.put("ctl00$menuBOVESPASecundario","");
			h.put("ctl00$contentPlaceHolderConteudo$tabOpcoes","{\"State\"=%s&\"+{},\"TabState\"=%s&\"+{\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabPosicoesAberto\"=%s&\"+{\"Selected\"=%s&\"+true},\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabPosicoesAberto_tabOpcoesEmp\"=%s&\"+{\"Selected\"=%s&\"+true},\"ctl00_contentPlaceHolderConteudo_tabOpcoes_tabSeriesAutorizadas_tabSeriesAutEmp\"=%s&\"+{\"Selected\"=%s&\"+true}}}");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$","rbTodos");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$cmbVcto","0");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$txtConsultaData$txtConsultaData","2013-11-19");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$txtConsultaData$txtConsultaData$dateInput","2013-11-19-00-00-00");
			h.put("ctl00_contentPlaceHolderConteudo_posicoesAbertoEmp_txtConsultaData_txtConsultaData_calendar_SD","[]");
			h.put("ctl00_contentPlaceHolderConteudo_posicoesAbertoEmp_txtConsultaData_txtConsultaData_calendar_AD","[[2013,9,23],[2013,11,19],[2013,11,19]]");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$txtConsultaEmpresa",acao.getNomeEmpresa());
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$btnBuscarEmpresa","buscar");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$txtConsultaDataDownload$txtConsultaDataDownload","2013-11-19");
			h.put("ctl00$contentPlaceHolderConteudo$posicoesAbertoEmp$txtConsultaDataDownload$txtConsultaDataDownload$dateInput","2013-11-19-00-00-00");
			h.put("ctl00_contentPlaceHolderConteudo_posicoesAbertoEmp_txtConsultaDataDownload_txtConsultaDataDownload_calendar_SD","[]");
			h.put("ctl00_contentPlaceHolderConteudo_posicoesAbertoEmp_txtConsultaDataDownload_txtConsultaDataDownload_calendar_AD","[[2013,9,23],[2013,11,19],[2013,11,19]]");
			h.put("ctl00$contentPlaceHolderConteudo$mpgOpcoes_Selected","0");
			h.put("cboAgentesCorretorasNome","#");
			h.put("cboAgentesCorretorasCodigo","#");
			
			String url = "http://www.bmfbovespa.com.br/opcoes/opcoes.aspx?Idioma=pt-br";
			
//			h.put("papel","PETR4");
//			h.put("btBuscar.x","0");
//			h.put("btBuscar.y","0");
//			String url = "http://www.ondeinvestirbylopesfilho.com.br/cli/spl/cot/cotacao.asp";

			String html = SATAUtil.removeExcessoEspacos(HTTPSata.POST(url, h));

			/*if (acao.getNome().equals("PETR4")) {
				BufferedReader in = new BufferedReader(new FileReader("C:\\FILES\\pessoal\\projetos\\sata\\workspace\\sata\\logs\\BMFSource.txt"));
				String str;
				String html = "";
				while ((str = in.readLine()) != null) {
					html += str + "\n";
				}
				in.close();*/	
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
		atualizarCadastroOpcoes();
	}
}
