package sata.auto.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public enum TipoRelatorio {
	NENHUM("nenhum",false),
	OPERACOES("operacoes",false),
	ANUAL("anual",true),
	MENSAL("mensal",true),
	COMPLETO("completo",true),
	REINVESTIMENTO("reinvestimento",true),
	CSV("csv",false),
	CSV_MENSAL("csvMensal",false),
	CSV_REINVESTIMENTO("csvReinvestimento",false);
	
	private String name;
	private boolean choosable;
	
	private TipoRelatorio(String name, boolean choosable) {
		this.name = name;
		this.choosable = choosable;
	}

	public String getLabel() {
		String key = IConstants.MSG_ENUM_PREFIX_TIPO_RELATORIO + name;
		return SATAUtil.getMessage(key);
	}
	
	public static TipoRelatorio get(String name) {
		for (TipoRelatorio value : values())
			if (value.name.equalsIgnoreCase(name))
				return value;
		return null;
	}

	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (TipoRelatorio value : values())
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
