package sata.auto.to;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.web.util.LocaleUtil;
import sata.domain.util.IConstants;

public class Dia implements Comparable<Dia>, IConstants {
	
	private static final Map<Locale,String> FORMATOS_DATA_PADRAO = new HashMap<Locale, String>();
	static {
		FORMATOS_DATA_PADRAO.put(LOCALE_BRASIL, "dd/MM/yyyy");
		FORMATOS_DATA_PADRAO.put(LOCALE_EUA, "MM/dd/yyyy");
    }
	private static final String FORMATO_DATA_BANCO = "yyyy-MM-dd";
	
	Integer dia;
	Mes mes;
	
	public Dia() {}
	
	public Dia(Integer dia, Mes mes) {
		this.dia = dia;
		this.mes = mes;
	}
	
	public Dia(Integer dia, Integer mes, Integer ano) {
		this.dia = dia;
		this.mes = new Mes(mes, ano);
	}
	
	//data no formato yyyyMMdd
	public Dia(String data) {
		//remove o timestamp e o '-' da data
		data = data.replace(" 00:00:00.0", "").replace("-", "");
		//recupera o dia a partir da posicao 6 na string
		this.dia = Integer.valueOf(data.substring(6));
		//recupera o mes e ano
		this.mes = new Mes(Integer.valueOf(data.substring(4,6)), Integer.valueOf(data.substring(0,4)));
	}
	
	public static Dia converte(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(getFormatoDataPadrao()); 
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(sdf.parse(data)); 
		return converte(calendar);
	}
	
	public static Dia converte(Calendar calendar) {
		return new Dia(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
	}
	
	public String format(String pattern) {
		SimpleDateFormat formato = new SimpleDateFormat(pattern);  
		return formato.format(new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia).getTime()); 
	}
	
	public Dia getDiaPosterior(int qtd) {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, qtd);
		return converte(calendar);
	}
	
	public Dia getDiaMesPosterior(int qtdMeses) {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, qtdMeses);
		return converte(calendar);
	}
	
	public Dia getProximoDia() {
		return getDiaPosterior(1);
	}
	
	public Dia getDiaAnterior(int qtd) {
		return getDiaPosterior(-qtd);
	}
	
	public Dia getDiaAnterior() {
		return getDiaPosterior(-1);
	}
	
	public Dia getMesPosterior() {
		return new Dia(dia, mes.getMesPosterior());
	}
	
	public String formatoBanco() {
		return format(FORMATO_DATA_BANCO);
	}
	
	public String formatoPadrao() {
		return format(getFormatoDataPadrao());
	}
	
	public String formatoBrasileiro() {
		return format(FORMATOS_DATA_PADRAO.get(LOCALE_BRASIL));
	}
	
	public Calendar getCalendar() {
		return new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia);
	}
	
	public Integer getAno() {
		return mes.getAno();
	}
	
	@Override
	public int compareTo(Dia other) {
		return new CompareToBuilder()
	    	       .append(mes, other.mes)
	    	       .append(dia, other.dia)
	    	       .toComparison();
	}
	
	public boolean lessThan(Dia dia) {
		return this.compareTo(dia) == -1;
	}
	
	public boolean lessEqualsThan(Dia dia) {
		return this.compareTo(dia) == -1
			|| this.equals(dia);
	}
	
	public boolean greaterThan(Dia dia) {
		return this.compareTo(dia) == 1;
	}
	
	public boolean greaterEqualsThan(Dia dia) {
		return this.compareTo(dia) == 1
			|| this.equals(dia);
	}
	
	private static String getFormatoDataPadrao() {
		return FORMATOS_DATA_PADRAO.get(LocaleUtil.getCurrentLocale());
	}
	
	@Override
	public String toString() {
		return formatoPadrao();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(dia).
	       append(mes).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) { return false; }
		if (obj == this) { return true; }
		Dia dia = (Dia)obj;
		return new EqualsBuilder()
		.append(this.dia, dia.dia)
		.append(this.mes, dia.mes)
		.isEquals();
	}

	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
	/**
	 * Retorna a data com o tipo java.util.Date.
	 * @return a data com o tipo java.util.Date.
	 */
	public Date getDate(){
		return new Date(this.getAno().intValue(), this.getMes().getMes().intValue(), this.getDia().intValue());
	}
}
