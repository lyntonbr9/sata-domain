package sata.auto.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import sata.domain.util.IConstants;

public class LocaleUtil implements IConstants {
	
	private static Locale locale;
	private static ResourceBundle rb;

	public static Locale getCurrentLocale() {
		if (locale != null)
			return locale;
		else
			return LOCALE_BRASIL;
	}

	public static void setLocale(Locale locale) {
		LocaleUtil.locale = locale;
	}

	public static void setRb(ResourceBundle rb) {
		LocaleUtil.rb = rb;
	}
	
	public static ResourceBundle getBundle() {
		if (rb != null)
			return rb;
		else 
			return ResourceBundle.getBundle(MSG_BUNDLE, getCurrentLocale());
	}
	
	public static String formataData(java.util.Date data) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM,getCurrentLocale());
		
		return format.format(data);
	}

	public static java.util.Date formataData(String data) throws ParseException {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, getCurrentLocale());
		return format.parse(data);
	}
	
	private LocaleUtil() {} // Não é possível instanciar classes utilitárias

}
