package sata.domain.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import sata.auto.to.Dia;
import sata.domain.alert.AcompOpcoes;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.ICotacaoOpcaoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.CotacaoOpcaoTO;

public class DataManagement {
	
	private FileInputStream fisArqListaCotacoesDoAtivo;
	
	public void setArquivoListaCotacaoDeAtivos(String pathArqListaDeCotacoesDoAtivo){
		try {
			fisArqListaCotacoesDoAtivo = new FileInputStream(pathArqListaDeCotacoesDoAtivo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/*
	public void importarArqCotacaoToDB(String codigoAtivo, String ano){
		
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		if (this.fisArqListaCotacoesDoAtivo != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacoesDoAtivo);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				String conteudoLinha = "";
				while((conteudoLinha = brEntrada.readLine()) != null){
					ICotacaoAtivoDAO cotacaoAtivoDAO = daoFactory.getCotacaoAtivoDAO();
					String[] cotacaoDoAtivo = conteudoLinha.split(" ");
					CotacaoAtivoTO caTO = new CotacaoAtivoTO();
					caTO.setCodigo(cotacaoDoAtivo[0]);
					caTO.setAbertura(cotacaoDoAtivo[1]);
					caTO.setMaxima(cotacaoDoAtivo[2]);
					caTO.setMinima(cotacaoDoAtivo[3]);
					caTO.setFechamento(cotacaoDoAtivo[4]);
					caTO.setPeriodo(cotacaoDoAtivo[5]);
					caTO.setTipoPeriodo("D"); //cotacao diaria
					caTO.setAno(ano);
					cotacaoAtivoDAO.insertCotacaoDoAtivo(caTO);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}*/

	public void importarArqCotacaoHistoricaBovespaToDB(String codigoAtivo, String ano){
		
		//tenta abrir o arquivo de cotacoes
		try {
			fisArqListaCotacoesDoAtivo = new FileInputStream("COTAHIST_A" + ano + ".TXT");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (this.fisArqListaCotacoesDoAtivo != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacoesDoAtivo);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				
				int tamStrNomeAcaoOpcao;
				int tamStrNomeParametro;
				
				Date agora = new Date();
				long t1 = agora.getTime();
				System.out.println("Processando...");

				if (codigoAtivo.length() == 5) {
					tamStrNomeAcaoOpcao = 5;
					tamStrNomeParametro = 5;
				} else {
					tamStrNomeAcaoOpcao = 7;
					tamStrNomeParametro = 4;
				}
				
				ICotacaoAtivoDAO cotacaoAtivoDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
				String conteudoLinha = "";
				String periodo="";
				while((conteudoLinha = brEntrada.readLine()) != null)
				{
					if (conteudoLinha.substring(0, 2).equals("01")) 
					{
						if (conteudoLinha.substring(12, 24).trim().length() == tamStrNomeAcaoOpcao
							&& conteudoLinha.substring(12, 24).trim().substring(0,tamStrNomeParametro).equalsIgnoreCase(codigoAtivo)) 
						{
							periodo = String.valueOf(Integer.valueOf(conteudoLinha.substring(2, 10).trim()));
							//verifica se a cotacao nao existe
							Dia diaPeriodo = new Dia(periodo);
							if(cotacaoAtivoDAO.existeCotacao(codigoAtivo, diaPeriodo.formatoBanco()) == false){
								CotacaoAtivoTO caTO = new CotacaoAtivoTO();
								caTO.setCodigo(conteudoLinha.substring(12, 24).trim());
								caTO.setAbertura(String.valueOf(Integer.valueOf(conteudoLinha.substring(56, 69).trim())));
								caTO.setMaxima(String.valueOf(Integer.valueOf(conteudoLinha.substring(69, 82).trim())));
								caTO.setMinima(String.valueOf(Integer.valueOf(conteudoLinha.substring(82, 95).trim())));
								caTO.setFechamento(String.valueOf(Integer.valueOf(conteudoLinha.substring(108, 121).trim())));
								caTO.setVolume(String.valueOf(Long.parseLong(conteudoLinha.substring(170,188).trim())));
								caTO.setPeriodo(periodo);
								caTO.setTipoPeriodo("D"); //cotacao diaria
								caTO.setAno(ano);
								cotacaoAtivoDAO.insertCotacaoDoAtivo(caTO);
							}
						}
					}
				}
				
				agora = new Date();
				long tempoDuracao = agora.getTime() - t1;
				
				System.out.println("Tempo de processamento: " + tempoDuracao);
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	public void importarArqCotacaoHistoricaOpcoesBovespaToDB(String parteCodigoOpcao, String codigoAcao, String ano){
		
		//tenta abrir o arquivo de cotacoes
		try {
			fisArqListaCotacoesDoAtivo = new FileInputStream("COTAHIST_A" + ano + ".TXT");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (this.fisArqListaCotacoesDoAtivo != null)
		{
			DataInputStream disEntrada = new DataInputStream(this.fisArqListaCotacoesDoAtivo);
			BufferedReader brEntrada = new BufferedReader(new InputStreamReader(disEntrada));
			try {
				
				int tamStrNomeOpcao;
				int tamStrNomeParametro;
				
				Date agora = new Date();
				long t1 = agora.getTime();
				System.out.println("Processando...");

				tamStrNomeOpcao = 7;
				tamStrNomeParametro = 4;
				
				ICotacaoOpcaoDAO cotacaoOpcaoDAO = SATAFactoryFacade.getCotacaoOpcaoDAO();
				String conteudoLinha = "";
				String periodo="";
				String codigoOpcao="";
				while((conteudoLinha = brEntrada.readLine()) != null)
				{
					if (conteudoLinha.substring(0, 2).equals("01")) 
					{
						if (conteudoLinha.substring(12, 24).trim().length() == tamStrNomeOpcao
							&& conteudoLinha.substring(12, 24).trim().substring(0,tamStrNomeParametro).equalsIgnoreCase(parteCodigoOpcao)) 
						{
							periodo = String.valueOf(Integer.valueOf(conteudoLinha.substring(2, 10).trim()));
							codigoOpcao = conteudoLinha.substring(12, 24).trim();
							Dia diaPeriodo = new Dia(periodo);
							//verifica se a cotacao nao existe
							if(cotacaoOpcaoDAO.existeCotacao(codigoOpcao, diaPeriodo.formatoBanco()) == false){
								CotacaoOpcaoTO caTO = new CotacaoOpcaoTO();
								caTO.setCodigo(codigoOpcao);
								caTO.setAbertura(String.valueOf(Integer.valueOf(conteudoLinha.substring(56, 69).trim())));
								caTO.setMaxima(String.valueOf(Integer.valueOf(conteudoLinha.substring(69, 82).trim())));
								caTO.setMinima(String.valueOf(Integer.valueOf(conteudoLinha.substring(82, 95).trim())));
								caTO.setFechamento(String.valueOf(Integer.valueOf(conteudoLinha.substring(108, 121).trim())));
								caTO.setVolume(String.valueOf(Long.parseLong(conteudoLinha.substring(170,188).trim())));
								caTO.setPeriodo(periodo);
								caTO.setTipoPeriodo("D"); //cotacao diaria
								caTO.setAno(ano);
								caTO.setPrecoExercicio(String.valueOf(Integer.valueOf(conteudoLinha.substring(189, 201).trim())));
								caTO.setDataVencimento(String.valueOf(Integer.valueOf(conteudoLinha.substring(202, 210).trim())));
								caTO.setCodigoAcao(codigoAcao);
								caTO.processaValores();
								cotacaoOpcaoDAO.insertCotacaoDaOpcao(caTO);
							}
						}
					}
				}
				
				agora = new Date();
				long tempoDuracao = agora.getTime() - t1;
				
				System.out.println("Tempo de processamento: " + tempoDuracao);
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					brEntrada.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	public void atualizarOpcoes(String codigoOpcao, String codigoAcao, String ano){

		try {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			ICotacaoOpcaoDAO coDAO = SATAFactoryFacade.getCotacaoOpcaoDAO();
			//recupera as cotacoes da acao de todos os dias
//			List<CotacaoAtivoTO> cotacoesAcao =  caDAO.getCotacoesDoAtivo(codigoAcao, Integer.valueOf(ano));
			//atualiza a acao das opcoes
			List<CotacaoAtivoTO> cotacoesAcao = caDAO.getCotacoesDoAtivo(codigoAcao, "2012-08-01", "2012-10-01");
			for(CotacaoAtivoTO caTO : cotacoesAcao){
				List<CotacaoOpcaoTO> cotacoesOpcao = coDAO.getCotacoesDaOpcao(codigoOpcao, caTO.getPeriodo(), caTO.getPeriodo());
				CotacaoOpcaoTO cotacaoOpcaoATM = AcompOpcoes.getCotacaoOpcaoMaisATM(cotacoesOpcao, caTO);
				if(cotacaoOpcaoATM != null){
					cotacaoOpcaoATM.setEhATM(true);
					coDAO.updateCotacaoDaOpcao(cotacaoOpcaoATM);
				}
			}
			
			//recupera as cotacoes da acao de todos os dias
//			List<CotacaoAtivoTO> cotacoesAcao =  caDAO.getCotacoesDoAtivo(codigoAcao, Integer.valueOf(ano));
			//atualiza a acao das opcoes
//			List<CotacaoOpcaoTO> cotacoesOpcao = coDAO.getCotacoesDaOpcao(codigoOpcao, "2012-09-01", "2012-10-01");
//			for(CotacaoOpcaoTO coTO : cotacoesOpcao){
//				coTO.processaValores();
//				coDAO.updateCotacaoDaOpcao(coTO);
//			}
//				CotacaoOpcaoTO cotacaoOpcaoATM = AcompOpcoes.getCotacaoOpcaoMaisATM(cotacoesOpcao, caTO);
//				if(cotacaoOpcaoATM != null){
//					cotacaoOpcaoATM.setEhATM(true);
//					coDAO.updateCotacaoDaOpcao(cotacaoOpcaoATM);
//				}
			
//			System.out.println("funciona");
			//para cada cotacao acao calcula quem eh a opcao ATM
			
			//atualiza a tabela
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		


	}

//	public static void main(String[] args) {
//		DataManagement dm = new DataManagement();
////		String ano= new String("2011");
//		
////		dm.atualizarOpcoes(codigoOpcao, codigoAcao, ano)
////		IAtivoDAO ativoDAO = SATAFactoryFacade.getAtivoDAO();
////		List<String> listaCodigosOpcoesLiquidas = ativoDAO.getCodigosOpcoesLiquidas(ano);
////		for(String nomeOpcao: listaCodigosOpcoesLiquidas)
////			dm.importarArqCotacaoHistoricaBovespaToDB(nomeOpcao, ano);
//		
//		for(int i=2000; i < 2011; i++){
//			dm.importarArqCotacaoHistoricaBovespaToDB("PETR4", String.valueOf(i));	
//		}
		
//		dm.importarArqCotacaoHistoricaOpcoesBovespaToDB("PETR", "PETR4", ano);
//		dm.atualizarOpcoes("PETR", "PETR4", ano);
		
//		DataManagement dm = new DataManagement();
//		String periodo = "20100104";
//		Timestamp ts = dm.getTimeStampPeriodoCotacao(periodo);
//		System.out.println(dm.getTimeStampFormatado(ts, true));
//		
		//String periodo = "2011-01-04 00:00:00.0";
		//String periodoFormatado = periodo.substring(0,4) + "-" + periodo.substring(4,6) + "-" + periodo.substring(6,8);
//		Calendar cal = new GregorianCalendar();
//		cal.set(2011, 1, 4);
//		System.out.println(cal.get(Calendar.DAY_OF_MONTH)+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
	
		//cal.getTime();
		//System.out.println(periodoFormatado + " 00:00:00");
		//Timestamp ts = Timestamp.valueOf(periodo);
//		Timestamp ts = new Timestamp(cal.getTime().getTime());
//		try {	
//			DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//			
//			System.out.println(df.parse(periodo).getDay()+ "-" + df.parse(periodo).getMonth());
//			//Timestamp ts = new Timestamp(df.parse(periodo).getTime());			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		//Timestamp ts = new DataManagement().getTimeStampPeriodoCotacao(periodo);
//		cal.setTime(ts);
//		System.out.println(cal.get(Calendar.DAY_OF_MONTH)+ "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR));
//		
//		System.out.println(ts.getDay()+ "-" + ts.getMonth() + "-" + ts.getYear());		
//		
//	}

	public static void main(String[] args) {
		
		long APOSTA = 200;
		long MESES = 120;
		long CORRETAGEM = 12;
		long CAIXA_INICIAL = 3000;
	
		long caixaInicial = CAIXA_INICIAL;
		long caixa = caixaInicial; //caixa para cobrir prejuizo
		long perda = 0;
		long aposta = APOSTA;
		System.out.println("caixa: " + caixa + " perda: " + perda  + " aposta futura: " + aposta);
		for(int i=0; i < MESES; i++) {
			if(Math.random() <= 0.5) { //sucesso
				caixa = caixa + aposta;
				aposta = Math.round((float) caixa*0.05);
				perda = 0;
			} else { //fracasso
				perda = aposta * (-1);
				aposta = Math.abs(perda) + Math.abs(aposta);
				caixa = caixa + perda;
			}
			caixa = caixa - CORRETAGEM;
			System.out.println("caixa: " + caixa + " perda: " + perda  + " aposta futura: " + aposta);
		}
		System.out.println("caixa inicial: " + caixaInicial);
		System.out.println("caixa final: " + caixa);
		System.out.println("lucro: " + (caixa - caixaInicial));
		
		double rendMensal = 0.0;
		double potencia = ((double) 1 / (double) MESES);
		rendMensal = Math.pow((caixa / caixaInicial), potencia) - 1;
		System.out.println("renda mensal: " + rendMensal*100);
	}

}
