package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int diasParaVencimento) {
		return new Compra(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this, diasParaVencimento);
	}
	
	@Override
	public String getBundleMessage() {
		return MSG_ENUM_LABEL_VENDA;
	}

	public Venda() {
		super();
	}

	public Venda(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		super(ativo, mesesParaVencimento, condicao);
	}

	public Venda(Ativo ativo, int mesesParaVencimento) {
		super(ativo, mesesParaVencimento);
	}

	public Venda(Ativo ativo) {
		super(ativo);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao, int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao, diasParaVencimento);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa);
	}
	
	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa,
			int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa,
				diasParaVencimento);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
	}

	public Venda(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, diasParaVencimento);
	}

	public Venda(Ativo ativo, boolean reversivel) {
		super(ativo, reversivel);
	}

	public Venda(Ativo ativo, Condicao condicao) {
		super(ativo, condicao);
	}

	public Venda(int qtdLotes, Ativo ativo, Condicao condicao) {
		super(qtdLotes, ativo, condicao);
	}
}
