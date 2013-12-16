package sata.auto.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public enum Operador {
	IGUAL("="),
	DIFERENTE("<>"),
	MAIOR(">"),
	MENOR("<"),
	MAIOR_IGUAL(">="),
	MENOR_IGUAL("<=");
	
	private String simbol;
	
	private Operador(String simbol) {
		this.simbol = simbol;
	}

	public static Operador get(String simbol) {
		for (Operador value : values())
			if (value.getSimbol().equals(simbol))
				return value;
		return null;
	}

	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Operador value : values())
			items.add(new SelectItem(value, value.getSimbol()));
		return items;
	}
	
	@Override
	public String toString() {
		return simbol;
	}

	public String getSimbol() {
		return simbol;
	}

	public void setSimbol(String simbol) {
		this.simbol = simbol;
	}
}
