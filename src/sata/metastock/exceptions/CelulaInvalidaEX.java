/*
 * Sistema de Formacao de Precos - FORPREC
 * Empresa: PETROBRAS DISTRIBUIDORA
 * Gerencia: GTI/GESUN/GESWEB
 * Analistas: 
 * 		Andre Serodio 		- Chave: ZFM1
 * 		Flavio Guimarães 	- Chave: ZFOO
 * 		Marcos L. L. Filho	- Chave: ZFO8
 * 
 * Criado em Feb 23, 2005
 * Hora: 2:06:58 PM
 */
package sata.metastock.exceptions;

/**
 * @author Flávio Guimarães Chaves
 * GTI/GESUN/GESWEB
 * Chave ZFOO
 *
 */
public class CelulaInvalidaEX extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 22189157619632178L;

	public CelulaInvalidaEX(){
		
	}
	
	public CelulaInvalidaEX(String msg){
		super(msg);
	}
}
