package sata.auto.operacao.ativo;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoRendaFixa;
import sata.auto.to.Dia;

public class RendaFixa extends Derivado {
	
	public RendaFixa() {}
	
	public RendaFixa(Acao acao) {
		this.acao = acao;
	}
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		return new PrecoRendaFixa(this, dia);
	}
	
	@Override
	public String getBundleMessage() {
		return "list.ativo.rendaFixa";
	}
}
