/*
 * Created on 23/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.simulacao;

import java.math.BigDecimal;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Capital {
	
	private double dinheiro = 10000.0;
	
	private double corretagem = 40.0;
	
	private double precoCompra = 0.0;
	
	private double precoVenda = 0.0;
	
	
	
	/**
	 * @return Returns the corretagem.
	 */
	public double getCorretagem() {
		return corretagem;
	}
	/**
	 * @param corretagem The corretagem to set.
	 */
	public void setCorretagem(double corretagem) {
		this.corretagem = corretagem;
	}
	/**
	 * @return Returns the dinheiro.
	 */
	public double getDinheiro() {
		return dinheiro;
	}
	/**
	 * @param dinheiro The dinheiro to set.
	 */
	public void setDinheiro(double dinheiro) {
		this.dinheiro = dinheiro;
	}
	/**
	 * @return Returns the precoCompra.
	 */
	public double getPrecoCompra() {
		return precoCompra;
	}
	/**
	 * @param precoCompra The precoCompra to set.
	 */
	public void setPrecoCompra(double precoCompra) {
		this.precoCompra = precoCompra;
	}
	/**
	 * @return Returns the precoVenda.
	 */
	public double getPrecoVenda() {
		return precoVenda;
	}
	/**
	 * @param precoVenda The precoVenda to set.
	 */
	public void setPrecoVenda(double precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	public double calculaNovoValor(){
		
		dinheiro = dinheiro*(new BigDecimal(precoVenda).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue());
		dinheiro = dinheiro - corretagem;
		return dinheiro;	
	}
	
	public double getPercentual(){
		
		if(precoVenda>precoCompra){
			return 100*(new BigDecimal(precoVenda).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1);
		}else{
			return 100*(new BigDecimal(precoCompra - precoVenda).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue());
		}
		
	}
	
	public double getPercentualMomento(double fechamento){
		
		if(fechamento>precoCompra){
			return 100*(new BigDecimal(fechamento).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue()-1);
		}else{
			return 100*(new BigDecimal(fechamento - precoVenda).divide(new BigDecimal(precoCompra),BigDecimal.ROUND_HALF_EVEN,6).doubleValue());
		}
		
		
	}
	
}
