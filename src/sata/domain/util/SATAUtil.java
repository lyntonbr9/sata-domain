package sata.domain.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import sata.auto.to.Dia;
import sata.auto.web.util.LocaleUtil;
import sata.domain.to.CotacaoAtivoTO;
import sata.metastock.http.HTTPSata;
import sata.metastock.mail.SendMailUsingAuthentication;

public final class SATAUtil implements IConstants{
	
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
	
	public static String lerArquivo(String filename) throws IOException {
		File file = new File(filename);
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String text = null;
		while ((text = reader.readLine()) != null) {
			contents.append(text).append("\n");
		}
		if (reader != null) {
			reader.close();
		}
		return contents.toString();
	}
	
	public static void sendMail(String assunto, String msg, String... remetentes) throws MessagingException {
		if (isAmbienteProducao())
			SendMailUsingAuthentication.sendMail(remetentes, assunto, msg);
	}
	
	public static String encrypt(String value) {   
		try {   
			MessageDigest md = MessageDigest.getInstance("MD5");   
			md.update(value.getBytes());   
			byte[] hash = md.digest();   
			StringBuffer hexString = new StringBuffer();   
			for (int i = 0; i < hash.length; i++) {   
				if ((0xff & hash[i]) < 0x10)   
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));   
				else hexString.append(Integer.toHexString(0xFF & hash[i]));   
			}   
			value = hexString.toString();

		} catch (NoSuchAlgorithmException nsae) {   
			nsae.printStackTrace();   
		}   
		return value;   
	} 

	/**
	 * Retorna a diferença em dias.
	 * @param dia1 primeiro dia no formato YYYYmmdd.
	 * @param dia2 segundo dia no formato YYYYmmdd.
	 * @return
	 */
	public static int getDiferencaDias(String dia1, String dia2) {
		Dia diaUm = new Dia(dia1);
		Dia diaDois = new Dia(dia2);
		return getDiferencaDias(diaUm.getDate(), diaDois.getDate());
	}
	
	public static int getDiferencaDias(Dia dia1, Dia dia2) {
		return getDiferencaDias(dia1.getDate(), dia2.getDate());
	}
	
	public static int getDiferencaDias(java.util.Date dia1, java.util.Date dia2) {
		return getDiferencaDias(converteToSQLDate(dia1),converteToSQLDate(dia2));
	}
	
	public static int getDiferencaDias(Date dia1, Date dia2) {
		return getDiferencaDias(converteToCalendar(dia1), converteToCalendar(dia2));
	}
	
	public static int getDiferencaDias(Calendar dia1, Calendar dia2) {
		long longDia = dia1.getTimeInMillis();
		long longFechamento = dia2.getTimeInMillis();
//		return (int) (((longFechamento - longDia) / (24*60*60*1000)) + 1);
		return (int) (((longFechamento - longDia) / (24*60*60*1000)));
	}
	
	public static java.util.Date converteToDate(String data) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		return (java.util.Date) formatter.parse(data); 
	}
	
	public static Calendar converteToCalendar(Date data) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data);
		return cal;
	}
	
	public static java.sql.Date converteToSQLDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());  
	}
	
	private static double taxaDeJuros = TAXA_DE_JUROS;
	
	public static double getTaxaDeJuros(Integer ano) {
		return getTaxaDeJuros();
	}
	
	public static double getTaxaDeJuros() {
		return taxaDeJuros;
	}
	
	public static void setTaxaDeJuros(double taxaJuros) {
		taxaDeJuros = taxaJuros;
	}
	
	//Ex: se passado 5 retorna 05
	public static String getStrDoisDigitos(int valor){		 		
		return (valor < 10) ? "0" + valor : String.valueOf(valor);
	}
	
	/**
	 * Copia uma lista para um array
	 */
	public static <T> T[] copyToArray(List<T> list, T[] array) {
		int i=0;
		for (T item : list) {
			array[i++] = item;
		}
		return array;
	}
	
	/**
	 * Reverte os itens do array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] reverte(T[] array) {
		List<T> lista = Arrays.asList(array);
		Collections.reverse(lista);
		return (T[]) lista.toArray();
	}
	
	/**
	 * Formata um número com duas casas decimais
	 * @param numero o número a ser formatado
	 * @return o número formatado
	 */
	public static String formataNumero(BigDecimal numero, Locale locale) {
		NumberFormat nf;
		if (locale == null)
			nf = NumberFormat.getInstance();
		else nf = NumberFormat.getInstance(locale);
        return nf.format(numero);
	}
	
	public static String formataNumero(BigDecimal numero) {
		return SATAUtil.formataNumero(numero, LocaleUtil.getCurrentLocale());
	}
	
	public static String getMessage(String key, String... arguments) throws MissingResourceException {
		String pattern = LocaleUtil.getBundle().getString(key);
		if(arguments == null)
			return pattern;
		return getMessageFormat(pattern).format(bundleArguments(arguments));
	}	
	
	private static Cache<String, MessageFormat> patterns = new Cache<String, MessageFormat>(20);
	
	private static MessageFormat getMessageFormat(String pattern){
    	MessageFormat formatter = (MessageFormat) patterns.get(pattern);
    	if(formatter == null){
            synchronized(patterns){
                formatter = patterns.get(pattern);
                if(formatter == null){
                    formatter = new MessageFormat(pattern);
                    patterns.put(pattern, formatter);
                }
            }
        }
    	return formatter;
    }
	
	private static String[] bundleArguments(String[] arguments) {
		for (int i=0; i<arguments.length; i++) {
			try {
				arguments[i] = getMessage(arguments[i]);
			} catch (MissingResourceException e) {}
		}
		return arguments;
	}
	
	/**
	 * Retorna o n-ésimo dia do mês que caia no dia da semana no mês informado 
	 * @param ano o ano do mês desejado
	 * @param mes o mês desejado
	 * @param diaSemana o dia da semana desejado
	 * @param n o n que define o n-ésimo dia da semana
	 * @return o n-ésimo dia do mês que caia no dia da semana no mês informado
	 */
	public static Integer getDiaMes(int ano, int mes, int diaSemana, int n) {
		int posicao = 1;
		for (int i = 1; i <= 31; i++) {
			GregorianCalendar calendar = new GregorianCalendar(ano, mes-1, i);
			if (calendar.get(Calendar.DAY_OF_WEEK) == diaSemana) {
				if (n == posicao++) {
					return calendar.get(Calendar.DAY_OF_MONTH);
				}
			}
		}
		return null;
	}
	
	/**
	 * Converte yyyyMMdd em TimeStamp.
	 * @param periodo Data no formato yyyyMMdd.
	 * @return A data no formato TimeStamp.
	 */
	public static Timestamp getTimeStampPeriodoCotacao(String periodo){
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.valueOf(periodo.substring(0,4)), Integer.valueOf(periodo.substring(4,6)) - 1, Integer.valueOf(periodo.substring(6,8)),0,0,0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		return ts;
	}
	
	/**
	 * Formata a data em TimeStamp para dd/MM/yyyy.
	 * @param ts TimeStamp que se deseja formatar.
	 * @param comHora Se true retorna a data com a hora hh:mm:ss.
	 * @return Retorna a data no formato dd/MM/yyyy, sendo antes em TimeStamp. 
	 */
	public static String getTimeStampFormatado(Timestamp ts, boolean comHora){
		Calendar cal = new GregorianCalendar();
		cal.setTime(ts);
		String tempoFormatado = "";
		String diaStr = getStrDoisDigitos(cal.get(Calendar.DAY_OF_MONTH));
		String mesStr = getStrDoisDigitos(cal.get(Calendar.MONTH) + 1);
		
		if (comHora == false){
			tempoFormatado = diaStr + "/" + mesStr + "/" + cal.get(Calendar.YEAR);
		}
		else{
			tempoFormatado = diaStr + "/" + mesStr  + "/" + cal.get(Calendar.YEAR)
			+ " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		}
		return tempoFormatado;
	}
	
	/**
	 * Formata dd/MM/yyyy em yyyyMMdd.
	 * @param data Data no formato dd/MM/yyyy.
	 * @return Retorna a data formatada em yyyyMMdd.
	 */
	public static String getDataFormatadaParaBD(String data){
		
		return data.substring(6,10) + data.substring(3,5) + data.substring(0,2);
	}
	
	public static int[] getCandles(List<CotacaoAtivoTO> listaDasCotacoes){
		
		int Candles[] = new int[listaDasCotacoes.size()];

		int abertura;
		int fechamento;

		//seta as candles
		for (int i=0; i < listaDasCotacoes.size() ; i++  )
		{
			abertura = Integer.parseInt(listaDasCotacoes.get(i).getAbertura());
			fechamento = Integer.parseInt(listaDasCotacoes.get(i).getFechamento());
			Candles[i] = (fechamento > abertura) ? CANDLE_VERDE : CANDLE_VERMELHA; 
		}
		
		return Candles;
	}
	
	public static int getValorGanho(CotacaoAtivoTO caTO, int posicaoListaCotacoes, int fechamentoDiaAnterior, int stopGain, int stopLoss){
		int abertura = Integer.parseInt(caTO.getAbertura());
		int maxima = Integer.parseInt(caTO.getMaxima());
		int minima = Integer.parseInt(caTO.getMinima());
		int fechamento = Integer.parseInt(caTO.getFechamento());
		int valorGanho;
		
		if ((fechamentoDiaAnterior + stopGain) <= maxima)
		{
			//ver qtd de operacoes de risco
			if( ((fechamentoDiaAnterior - stopLoss) >= minima) ){
				System.out.println("Fez a operacao dia RISCO: " + caTO.getPeriodo() + " posicaoListaCotacoes = " + posicaoListaCotacoes);
			}
//			else
//			{
				if (abertura < (fechamentoDiaAnterior + stopGain))
					valorGanho = stopGain;
				else
					valorGanho = (abertura - fechamentoDiaAnterior);
				
				System.out.println("Fez a operacao dia SUCESSO: " + valorGanho  + " " + caTO.getPeriodo() 
						+ " posicaoListaCotacoes = " + posicaoListaCotacoes + " A: " + abertura + " Max: " + maxima
						+ " Min: " + minima + " F: " + fechamento);
			
//			}
		}
		else{
			//valorPerda = valorPerda + (fechamento - abertura);
			valorGanho = (stopLoss * -1);
			System.out.println("Fez a operacao dia FRACASSO: " + valorGanho  + " " + caTO.getPeriodo() 
					+ " posicaoListaCotacoes = " + posicaoListaCotacoes + " A: " + abertura + " Max: " + maxima
					+ " Min: " + minima + " F: " + fechamento);
		}
		return valorGanho;
	}

	//Formata dd/MM/yyyy para yyyyMMdd
	public static String getDataSemFormato(String data)
	{
		
		String[] vetorData = data.split("/"); //a data é separada [0] = dia, [1] = mes, [2] = ano
		
		if(Integer.parseInt(vetorData[1]) < 10) //formatando o mes de 1...9 para 01...09
			vetorData[1] = "0" + vetorData[1]; 
		
		if(Integer.parseInt(vetorData[0]) < 10) //formatando o dia de 1...9 para 01...09
			vetorData[0] = "0" + vetorData[0]; 
		
		System.out.println("Data formatada: " + vetorData[2] + vetorData[1] + vetorData[0]);
		return vetorData[2] + vetorData[1] + vetorData[0];
		
	}
	
	public static Calendar getDataAtual(){
		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");  
        TimeZone.setDefault(tz);
        Calendar calendario = GregorianCalendar.getInstance(tz); 
//        if (isAmbienteProducao()) {
//        	calendario.add(Calendar.MINUTE, -26);
//        }
		return calendario;		
	}
	
	public static boolean isAmbienteProducao() {
		return SATAPropertyLoader.getProperty(PROP_SATA_AMBIENTE).equals(AMBIENTE_PROD);
	}
	
	public static boolean isAmbienteDesenvolvimento() {
		return SATAPropertyLoader.getProperty(PROP_SATA_AMBIENTE).equals(AMBIENTE_DESENV);
	}
	
	public static String getDataAtualFormatada() {
		return formataCalendar(getDataAtual());
	}
	
	public static String getSomenteDataAtualFormatada() {
		return formataCalendarData(getDataAtual());
	}
	
	public static String formataCalendar(Calendar calendar) {
		String string = "";
		string += getStrDoisDigitos(calendar.get(Calendar.DAY_OF_MONTH)) + "/";
		string += getStrDoisDigitos(calendar.get(Calendar.MONTH)+1) + "/";
		string += calendar.get(Calendar.YEAR) + " ";
		string += getStrDoisDigitos(calendar.get(Calendar.HOUR_OF_DAY)) + ":";
		string += getStrDoisDigitos(calendar.get(Calendar.MINUTE)) + ":";
		string += getStrDoisDigitos(calendar.get(Calendar.SECOND));
		return string;
	}
	
	public static String formataCalendarData(Calendar calendar) {
		String string = "";
		string += getStrDoisDigitos(calendar.get(Calendar.DAY_OF_MONTH)) + "/";
		string += getStrDoisDigitos(calendar.get(Calendar.MONTH)+1) + "/";
		string += calendar.get(Calendar.YEAR);
		return string;
	}
	
	public static String removeExcessoEspacos(String str) {
		String padrao = "\\s{2,}";
	    Pattern regPat = Pattern.compile(padrao);
	    Matcher matcher = regPat.matcher(str);
	    String res = matcher.replaceAll(" ").trim();
	    return res;
	}
	
	// pega as cotacoes do site
	// (http://br.finance.yahoo.com/q/hp?s=PETR4.SA) no intervalo
	public static List<CotacaoAtivoTO> getCotacoesFromYahooFinances(String acao, String dataInicial, String dataFinal)
	{
		List<CotacaoAtivoTO> listaCotacoesAtivo = new ArrayList<CotacaoAtivoTO>();
		Hashtable<String, String> h = getYahooFinancesURLParameters(acao, dataInicial, dataFinal);
		
//		String html = CotacaoLopesFilho.POST("http://br.finance.yahoo.com/q/hp", h);
		String html = HTTPSata.POST("http://br.finance.yahoo.com/q/hp", h);
//		System.out.println(html);
//		String dataInicioLista = getDataToYahooFinances(dataFinal); // Ex: "10 de mai de 2011"
		String dataFimDaLista = getDataToYahooFinances(dataInicial); // Ex: "2 de mai de 2011"
//		System.out.println(dataInicioLista);
//		System.out.println(dataFimDaLista);
		
//		String pedacoHtml = html.substring(html.indexOf(dataInicioLista));
//		System.out.println(pedacoHtml);
		String tagTDInicialDia = "<td class=\"yfnc_tabledata1\" nowrap align=\"right\">";
		if(html.indexOf(tagTDInicialDia) == -1){
			System.out.println("nao encontrou");
			return listaCotacoesAtivo;
		}
			
		String pedacoHtml = html.substring(html.indexOf(tagTDInicialDia));
//		System.out.println(pedacoHtml);
		String tagTD = "<td class=\"yfnc_tabledata1\" align=\"right\">";
		String fimTD = "</td>";
		
		//Ordem de leitura dos valores:
		// 0 - Dia  1 - Ano  2 - Abertura  3 - Alta   4 - Baixa  5 - Fechar
		String valores[] = new String[6];
		
		int maximoVezesBusca = 300;
		int contador = 0;
		boolean buscar = true;
		int posicaoFimTD = 0;
		
		int i;
		//para todos os dias que faltam
		while(buscar)
		{
			//evita que fique em loop infinito
			contador++;
			if (contador == maximoVezesBusca)
				break;
			
			//Le os valores do HTML
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTDInicialDia));
			posicaoFimTD = pedacoHtml.indexOf(fimTD);
			//Pega a data
			valores[0] = pedacoHtml.substring(tagTDInicialDia.length(), posicaoFimTD);
			
			//se vier os dividendos na tabela pula pra proxima linha dela
			if(valores[0].contains("/")){
				pedacoHtml = pedacoHtml.substring(posicaoFimTD);
				continue;
			}
			
			//verifica se ja chegou ao fim para sair do loop, senao continua a extrair
			if(valores[0].equalsIgnoreCase(dataFimDaLista))
				break;
			
			//Formata para a data padrao dd/MM/yyyy
			valores[0] = formataDataFromYahooFinances(valores[0]);
			
			//Pega o ano
			valores[1] = valores[0].substring(valores[0].length() - 4);
			
			//Pega os valores da cotacao
			i = 2;
			while (i < 6) 
			{
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTD));
//				System.out.println(pedacoHtml);
				posicaoFimTD = pedacoHtml.indexOf(fimTD);
//				System.out.println(posicaoFimTD);
//				System.out.println(tagTD.length());
				valores[i] = pedacoHtml.substring(tagTD.length(), posicaoFimTD);
//				System.out.println(valores[i]);
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(posicaoFimTD));
				i++;
			}
			CotacaoAtivoTO caTO = new CotacaoAtivoTO();
			caTO.setCodigo(acao);
			caTO.setPeriodo(valores[0]); //data corrente de insercao
			caTO.setAno(valores[1]);
			caTO.setTipoPeriodo("D");
			caTO.setAbertura(valores[2].replace(",", ""));
			caTO.setMaxima(valores[3].replace(",", ""));
			caTO.setMinima(valores[4].replace(",", ""));
			caTO.setFechamento(valores[5].replace(",", ""));
			listaCotacoesAtivo.add(caTO);
		}
		return listaCotacoesAtivo;
	}
	
	public static String getDataToYahooFinances(String data){
		
		String valores[] = data.split("/");
		int dia = Integer.parseInt(valores[0]);
		int indiceMes = Integer.parseInt(valores[1]) - 1;
		int ano = Integer.parseInt(valores[2]);
		return String.valueOf(dia) + " de " + String.valueOf(mesesYahooFinances[indiceMes]) + " de " + String.valueOf(ano);
	}
	
	public static Hashtable<String, String> getYahooFinancesURLParameters(String acao, 
				String dataInicial, String dataFinal){
		
		Hashtable<String, String> h = new Hashtable<String, String>();
		String valoresDataInicial[] = dataInicial.split("/");
		String valoresDataFinal[] = dataFinal.split("/");
		int indiceMesInicial = (Integer.parseInt(valoresDataInicial[1]) - 1);
		int indiceMesFinal = (Integer.parseInt(valoresDataFinal[1]) - 1);
		h.put("s", acao + ".SA");
		h.put("a", String.valueOf(indiceMesInicial) ); //indice mes inicial
		h.put("b", valoresDataInicial[0]); //dia inicial
		h.put("c", valoresDataInicial[2]); //ano inicial
		h.put("d", String.valueOf(indiceMesFinal)); //indice mes final
		h.put("e", valoresDataFinal[0]); //dia final
		h.put("f", valoresDataFinal[2]); //ano final
		h.put("g", "d"); //diario
		
		return h;
	}
	
	//A data do Yahoo Finances vem no formato Ex: 10 de mai de 2011
	public static String formataDataFromYahooFinances(String dataYahooFinances)
	{
		String valoresSplit[] = dataYahooFinances.split(" ");
		
		System.out.println("EI: " + dataYahooFinances);
		String dia = valoresSplit[0];
		if(Integer.parseInt(dia) < 10)
			dia = "0" + dia;
		
		int mes = getIndiceMesYahooFinances(valoresSplit[2]) + 1;
		String mesStr = String.valueOf(mes);
		if(mes < 10)
			mesStr = "0" + mesStr;
		
		String ano = valoresSplit[4];
		
		//return dia + "/" + mesStr + "/" + ano;
		return ano + mesStr + dia;
	}
	
	//retorna a posicao do mes no array de meses
	public static int getIndiceMesYahooFinances(String mesYahooFinanc)
	{
		for(int i=0; i < mesesYahooFinances.length; i++){
			if(mesYahooFinanc.equalsIgnoreCase(mesesYahooFinances[i])){
				return i;
			}
		}
		return -1; //Se nao encontrar
	}
	
	//formata a data para yyyyMMdd
	public static String formataDataFromInfoMoney(String dataInfoMoney)
	{
		String valoresSplit[] = dataInfoMoney.split("/");
		
		return valoresSplit[2] + valoresSplit[1] + valoresSplit[0];
	}
	
	//retorna data para o site InfoMoney
	public static String getDataToInfoMoney(String dataSemFormato){
		String valoresSplit[] = dataSemFormato.split("/");
		String dia;
		String mes;
		
		if (Integer.parseInt(valoresSplit[0]) < 10)
			dia = "0" + Integer.parseInt(valoresSplit[0]);
		else
			dia = valoresSplit[0];
		
		if (Integer.parseInt(valoresSplit[1]) < 10)
			mes = "0" + Integer.parseInt(valoresSplit[1]);
		else
			mes = valoresSplit[1];
		
		return dia + "/" + mes + "/" + valoresSplit[2];
	}
	
	// pega as cotacoes do site
	// (http://br.finance.yahoo.com/q/hp?s=PETR4.SA) no intervalo
	public static List<CotacaoAtivoTO> getCotacoesFromInfoMoney(String acao, String dataInicial, String htmlSource)
	{
		List<CotacaoAtivoTO> listaCotacoesAtivo = new ArrayList<CotacaoAtivoTO>();
		
		String html = htmlSource;
//		System.out.println(html);
//		String dataInicioLista = getDataToYahooFinances(dataFinal); // Ex: "10 de mai de 2011"
		String dataFimDaLista = dataInicial;
//		String dataFimDaLista = "09/05/2011";
//		System.out.println(dataInicioLista);
//		System.out.println(dataFimDaLista);
		
//		String pedacoHtml = html.substring(html.indexOf(dataInicioLista));
//		System.out.println(pedacoHtml);
		String tagTDInicialDia = "<TD>";
		
		if(html.indexOf(dataFimDaLista) == -1){
			System.out.println("Nao encontrou data do ultimo cadastro do ativo " + acao);
			return listaCotacoesAtivo;
		}
			
		String pedacoHtml = html.substring(html.indexOf(tagTDInicialDia));
//		System.out.println(pedacoHtml);
		String tagTD = "<TD class=\"numbers\">";
		String fimTD = "</TD>";
		
		//Ordem de leitura dos valores:
		// 0 - Dia  1 - Ano  2 - Abertura  3 - Alta   4 - Baixa  5 - Fechar
		String valores[] = new String[8];
		
		int maximoVezesBusca = 300;
		int contador = 0;
		boolean buscar = true;
		int posicaoFimTD = 0;
		
		int i;
		//para todos os dias que faltam
		while(buscar)
		{
			//evita que fique em loop infinito
			contador++;
			if (contador == maximoVezesBusca)
				break;
			
			//Le os valores do HTML
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTDInicialDia));
			posicaoFimTD = pedacoHtml.indexOf(fimTD);
			//Pega a data
			valores[0] = pedacoHtml.substring(tagTDInicialDia.length(), posicaoFimTD);
			
			//verifica se ja chegou ao fim para sair do loop, senao continua a extrair
			if(valores[0].equalsIgnoreCase(dataFimDaLista))
				break;
			
			//Pega o ano
			valores[1] = valores[0].substring(valores[0].length() - 4);
			
			
			//Pega os valores da cotacao
			i = 2;
			while (i < 8) 
			{
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTD));
//				System.out.println(pedacoHtml);
				posicaoFimTD = pedacoHtml.indexOf(fimTD);
//				System.out.println(posicaoFimTD);
//				System.out.println(tagTD.length());
				valores[i] = pedacoHtml.substring(tagTD.length(), posicaoFimTD);
//				System.out.println(valores[i]);
				pedacoHtml = pedacoHtml.substring(posicaoFimTD);
				i++;
			}
			CotacaoAtivoTO caTO = new CotacaoAtivoTO();
			caTO.setCodigo(acao);
			//Formata para a data padrao yyyyMMdd
			valores[0] = formataDataFromInfoMoney(valores[0]);
			caTO.setPeriodo(valores[0]); //data corrente de insercao
			caTO.setAno(valores[1]);
			caTO.setTipoPeriodo("D");
			caTO.setAbertura(valores[4].replace(",", ""));
			caTO.setMaxima(valores[7].replace(",", ""));
			caTO.setMinima(valores[5].replace(",", ""));
			caTO.setFechamento(valores[3].replace(",", ""));
			listaCotacoesAtivo.add(caTO);
		}
		return listaCotacoesAtivo;
	}
	
	public static boolean indicePertence(List<Integer> listaIndices, int indice){
    	
    	for(int i=0;i < listaIndices.size();i++){
    		if(listaIndices.get(i).intValue()==indice){
    			return true;
    		}
    	}
    	
    	return false;
    }
	
	private SATAUtil() {} // Não é possível instanciar classes utilitárias
}

