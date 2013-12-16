package sata.auto.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public enum Posicao {
	COMPRADO('C'),
	VENDIDO('V');
	
	private char key;
	
	private Posicao(char key) {
		this.key = key;
	}
	
	public String getLabel() {
		String prop = IConstants.MSG_ENUM_PREFIX_POSICAO + key;
		return SATAUtil.getMessage(prop);
	}

	public static Posicao get(char key) {
		for (Posicao value : values())
			if (value.getKey() == key)
				return value;
		return null;
	}
	
	public static Posicao get(String name) {
		for (Posicao value : values())
			if (value.name().equals(name))
				return value;
		return null;
	}

	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Posicao value : values())
			items.add(new SelectItem(value, value.getLabel()));
		return items;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}
}
