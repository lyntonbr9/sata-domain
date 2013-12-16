package sata.auto.enums;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public enum TipoCalculoValorInvestido {
	CUSTO_MONTAGEM("custoMontagem",true),
	DIFERENCA_STRIKES("diferencaStrikes", true),
	CUSTO_MONTAGEM_IGNORANDO_PRIMEIRO_MES("custoMontagemIgnorandoPrimeiroMes",false),
	PRECO_ACAO("precoAcao",true);
	
	private String name;
	private boolean choosable;
	
	private TipoCalculoValorInvestido(String name, boolean choosable) {
		this.name = name;
		this.choosable = choosable;
	}
	
	public String getLabel() {
		String key = IConstants.MSG_ENUM_PREFIX_TIPO_CALCULO_VALOR_INVESTIDO + name;
		return SATAUtil.getMessage(key);
	}
	
	public static TipoCalculoValorInvestido get(String name) {
		for (TipoCalculoValorInvestido value : values())
			if (value.name.equalsIgnoreCase(name))
				return value;
		return null;
	}

	public static List<SelectItem> getSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (TipoCalculoValorInvestido value : values())
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
