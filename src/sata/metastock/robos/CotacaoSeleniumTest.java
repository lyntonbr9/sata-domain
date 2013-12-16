package sata.metastock.robos;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.JUnitCore;

import sata.domain.alert.OperacaoDeAltaVarPoucoTempo;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.simulacao.SimulacaoAcaoAltaVarPoucoTempo;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.SATAUtil;
import sata.metastock.data.ValuesMeta;
import sata.metastock.mail.SendMailUsingAuthentication;

import com.thoughtworks.selenium.SeleneseTestCase;

public class CotacaoSeleniumTest extends SeleneseTestCase {
	
	public void setUp() throws Exception{
		
		setUp("http://www.infomoney.com.br", "*iexplore");
	}

	public void testCotacao() throws Exception{
		
		System.out.println("\nVai importar as cotacoes");
		
//		String htmlSource = "<table cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><th>Data</th><th class=\"numbers\">Histórico<a id=\"ctl00_cphContent_ctl02_lnkHelpNewsHistory\" class=\"ico-helpnews\" helpnewsid=\"562203\"></a></th><th class=\"numbers\">Fech.</th><th class=\"numbers\">Var.Dia(%)</th> <th class=\"numbers\">Abertura</th>       <th class=\"numbers\">Mínimo</th><th class=\"numbers\">Médio</th><th class=\"numbers\">Máximo</th><th class=\"numbers\">Volume</th><th class=\"numbers\">Negócios</th></tr> <tr class=\"even\"><td>13/05/2011</td><td class=\"numbers\">23,52</td><td class=\"numbers\">23,52</td><td class=\"numbers negative\">-0,68</td>      <td class=\"numbers\">23,77</td><td class=\"numbers\">23,48</td><td class=\"numbers\">23,61</td><td class=\"numbers\">23,85</td><td class=\"numbers\">474,08M</td><td class=\"numbers\">19.780</td></tr> <tr class=\"odd\"><td>12/05/2011</td><td class=\"numbers\">23,68</td><td class=\"numbers\">23,68</td><td class=\"numbers negative\">-0,55</td>      <td class=\"numbers\">23,60</td><td class=\"numbers\">23,43</td><td class=\"numbers\">23,68</td><td class=\"numbers\">23,92</td><td class=\"numbers\">555,55M</td><td class=\"numbers\">28.186</td></tr> <tr class=\"even\"><td>11/05/2011</td><td class=\"numbers\">24,01</td><td class=\"numbers\">23,81</td><td class=\"numbers negative\">-2,71</td>      <td class=\"numbers\">24,35</td><td class=\"numbers\">23,81</td><td class=\"numbers\">24,06</td><td class=\"numbers\">24,38</td><td class=\"numbers\">526,88M</td><td class=\"numbers\">20.975</td></tr> <tr class=\"odd\"><td>10/05/2011</td><td class=\"numbers\">24,68</td><td class=\"numbers\">24,47</td><td class=\"numbers positive\">0,33</td>      <td class=\"numbers\">24,51</td><td class=\"numbers\">24,31</td><td class=\"numbers\">24,48</td><td class=\"numbers\">24,65</td><td class=\"numbers\">307,68M</td><td class=\"numbers\">15.940</td></tr> <tr class=\"even\"><td>09/05/2011</td><td class=\"numbers\">24,60</td><td class=\"numbers\">24,40</td><td class=\"numbers positive\">1,61</td>      <td class=\"numbers\">24,04</td><td class=\"numbers\">24,00</td><td class=\"numbers\">24,27</td><td class=\"numbers\">24,48</td><td class=\"numbers\">444,20M</td><td class=\"numbers\">19.193</td></tr></tbody><caption id=\"ctl00_cphContent_ctl02_cptLegend\" align=\"bottom\">Legenda: K - Mil | M - Milhão | B - Bilhão</caption></table>";
	
		IAtivoDAO ativoDAO = SATAFactoryFacade.getAtivoDAO();
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		List<String> listaDeAcoes = ativoDAO.getCodigosAtivos();
		for (String acao : listaDeAcoes) {
//			if (acao.equalsIgnoreCase("CYRE3")) {
				selenium.open("/" + acao + "/historico");
				selenium.waitForPageToLoad("30000");
				
				selenium.select("id=ctl00_cphContent_ctl02_ddlPeriod", "1 Mês");
				selenium.click("id=ctl00_cphContent_ctl02_btnSearch");
				
				//Espera pelo carregamento da pagina
				//Precisa do try catch senao sai do metodo dando erro
				try {
					selenium.waitForPageToLoad("1000");	
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				String htmlSource = selenium.getEval("selenium.browserbot.getCurrentWindow().document.getElementById('results').innerHTML");
				
//				System.out.println("html: " + htmlSource);
				
				//pega o ultimo dia cadastrado da acao
				String dataUltimoCadastroAtivo = SATAUtil.getDataToInfoMoney(caDAO.getDataUltimoCadastro(acao)); //Ex: "2/5/2011" >> "02/05/2011"
				System.out.println("Data de ultimo cadastro da acao " + acao + ": " + dataUltimoCadastroAtivo);
				
				// atualiza as cotacoes da acao
				List<CotacaoAtivoTO> listaCotacoesAtivo = SATAUtil.getCotacoesFromInfoMoney(acao, dataUltimoCadastroAtivo, htmlSource);
				for(CotacaoAtivoTO caTO : listaCotacoesAtivo){
					System.out.println(caTO.getCodigo());
					System.out.println(caTO.getPeriodo());
					System.out.println(caTO.getAno());
					System.out.println(caTO.getTipoPeriodo());
					System.out.println("A: " + caTO.getAbertura());
					System.out.println("Max: " + caTO.getMaxima());
					System.out.println("Min: " + caTO.getMinima());
					System.out.println("F: " + caTO.getFechamento());
					caDAO.insertCotacaoDoAtivo(caTO);
				}
//				System.out.println("EI" + htmlSource);
				
//			}

		}
		System.out.println("Terminou de importar as cotacoes");
	}
	public static void main(String[] args) {
		JUnitCore.main("sata.metastock.robos.CotacaoSeleniumTest");
	}
		
}
