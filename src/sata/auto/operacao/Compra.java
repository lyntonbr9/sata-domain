package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Compra extends Operacao {
		
	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int diasParaVencimento) {
		return new Venda(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this, diasParaVencimento);
	}

	@Override
	public String getBundleMessage() {
		return MSG_ENUM_LABEL_COMPRA;
	}

	public Compra() {
		super();
	}

	public Compra(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		super(ativo, mesesParaVencimento, condicao);
	}

	public Compra(Ativo ativo, int mesesParaVencimento) {
		super(ativo, mesesParaVencimento);
	}

	public Compra(Ativo ativo) {
		super(ativo);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao, int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao, diasParaVencimento);
	}
	
	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa);
	}
	
	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa,
			int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa,
				diasParaVencimento);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int diasParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento, diasParaVencimento);
	}
	
	public Compra(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
	}

	public Compra(Ativo ativo, boolean reversivel) {
		super(ativo, reversivel);
	}

	public Compra(Ativo ativo, Condicao condicao) {
		super(ativo, condicao);
	}

	public Compra(int qtdLotes, Ativo ativo, Condicao condicao) {
		super(qtdLotes, ativo, condicao);
	}
}
