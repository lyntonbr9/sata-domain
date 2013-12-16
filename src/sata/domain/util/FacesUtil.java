package sata.domain.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import sata.auto.web.util.LocaleUtil;

public final class FacesUtil implements IConstants {
	
	public static List<SelectItem> convertToSelectItems(List<Date> datas) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Date data : datas)
			items.add(new SelectItem(data, LocaleUtil.formataData(data)));
		return items;
	}
	
	public static String formataTexto(String texto) {
		texto = texto.replace("\n", "<br/>");
		return texto;
	}
	
	public static void putInSession(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
	}
	
	public static void putInRequest(String key, Object value) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);
	}
	
	public static Object getFromSession(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
	}
	
	public static void removeFromSession(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
	}
	
	public static String getPaginaAtual() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewId().replace("/", "").replace("xhtml", "jsf");
	}
	
	public static void navigateTo(String stringNavegacao) {
		NavigationHandler nh = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
        nh.handleNavigation(FacesContext.getCurrentInstance(), null, stringNavegacao);	
	}
	
	public static void redirect(String page) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(page);
	}
	
	protected static ResourceBundle getContextBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null)
			return context.getApplication().getResourceBundle(context, "msgs");
		else return null;
	}
	
	public static void addException(Exception e) {
		addBundleMsg(FacesMessage.SEVERITY_FATAL, MSG_ERRO_GENERICO, SATAUtil.getStackTrace(e));
		e.printStackTrace();
	}

	public static void addInfo(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_INFO, key, arguments);  
	}  

	public static void addWarn(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_WARN, key, arguments);  
	}  
	
	public static void addError(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_ERROR, key, arguments);  
	}  

	public static void addFatal(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_FATAL, key, arguments);  
	}
	
	private static void addBundleMsg(Severity severity, String key, String[] arguments) {
		addMsg(severity, key, true, null, arguments);
	}
	
	private static void addBundleMsg(Severity severity, String key, String details) {
		addMsg(severity, key, true, details);
	}
	
	/*private static void addBundleMsg(Severity severity, String key, String details, String[] arguments) {
		addMsg(severity, key, true, details, arguments);
	}
	
	private static void addNonBundleMsg(Severity severity, String key, String[] arguments) {
		addMsg(severity, key, false, null, arguments);
	}*/

	private static void addMsg(Severity severity, String msgKey, boolean bundle, String detail, String... arguments) {
		String msg;
		if (bundle) msg = SATAUtil.getMessage(msgKey, arguments);
		else msg = msgKey;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, detail));
	}

	/*private static String getMessage(Exception e) {
		String msg = e.getLocalizedMessage();
		if (StringUtils.isEmpty(msg)) {
			msg = e.getClass().getSimpleName();
		}
		return msg;
	}*/
	
//	public static Locale getCurrentLocale() {
//		LocaleMB localeMB = getMB(LocaleMB.class);
//		if (localeMB!= null) return localeMB.getCurrentLocale();
//		else return LOCALE_BRASIL;
//	}
	
	@SuppressWarnings("unchecked")
	public static <MB> MB getMB(Class<MB> clazz) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			String mbName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
			MB mb = (MB) context.getExternalContext().getSessionMap().get(mbName);
			if (mb == null) {
				try {
					mb = clazz.newInstance();
					context.getExternalContext().getSessionMap().put(mbName, mb);
				} catch (Exception e) {
					return null;
				}
			}
			return mb;
		}
		else return null;
	}
	
	private FacesUtil() {} // Não é possível instanciar classes utilitárias
}
