package sata.auto.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public enum Atributo {
	PRECO("preco",true),
	VOLATILIDADE("volatilidade",true),
	PERCENTUAL_OPERACAO("percentualOperacao",false),
	PERCENTUAL_ACAO("percentualAcao",false),
	MEDIA_MOVEL("mediaMovel",false);
	
	private String name;
	private boolean choosable;
	
	private Atributo(String name, boolean choosable) {
		this.name = name;
		this.choosable = choosable;
	}
	
	public String getLabel() {
		String key = IConstants.MSG_ENUM_PREFIX_ATRIBUTO + name;
		return SATAUtil.getMessage(key);
	}
	
	public static Atributo get(String name) {
		for (Atributo value : values())
			if (value.name.equalsIgnoreCase(name))
				return value;
		return null;
	}

	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Atributo value : values())
			if (value.isChoosable()) {
				items.add(new SelectItem(value, value.getLabel()));
			}
		return items;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChoosable() {
		return choosable;
	}

	public void setChoosable(boolean choosable) {
		this.choosable = choosable;
	}
}
