package sata.domain.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe que implementa um Cache LRU
 * @author Leonardo Jardim
 * @param <Key> chave da cache
 * @param <Type> o tipo do objeto armazenado na cache
 */
public class Cache<Key, Type> extends LinkedHashMap<Key, Type> {

	private static final long serialVersionUID = -2196105308884533037L;

	protected int maxsize;
	
	public Cache(int maxsize) {
		super(maxsize, 0.75f, true);
		this.maxsize = maxsize;
	}

	/**
	 * Faz com que o elemento mais velho
	 * seja removido no caso do tamanho ser atingido
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<Key, Type> entry) {
		return (size() > maxsize);
	}
}