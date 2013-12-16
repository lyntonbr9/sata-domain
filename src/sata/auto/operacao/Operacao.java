package sata.auto.operacao;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.simulacao.Simulacao;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public abstract class Operacao implements IConstants {
	
	int qtdLotes = 1;
	int momento = ABERTURA;
	Ativo ativo;
	int mesesParaVencimento = 1;
	Condicao condicao;
	Operacao reversa;
	boolean reversivel = true;
	boolean executada = false;
	//quando a operação de reversao foi realizada
	Dia diaAbertura;
	//quando a operação de reversao foi realizada
	Dia diaReversao;
	//quando eh o vencimento da operação
	Dia diaVencimento;
	//quantidade de dias de duração da operação
	int duracao;
	//quantidade de dias para o vencimento da operação
	int diasParaVencimento = 1;

	abstract Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int diasParaVencimento);
	public abstract String getBundleMessage();
	
	public Preco getPreco(Dia dia) throws SATAEX {
		return ativo.getPreco(dia, this);
	}
	
	public boolean condicaoVerdadeira(Preco preco) throws SATAEX {
		if (condicao == null) return true;
		return condicao.verdadeira(preco);
	}
	
	private BigDecimal precoExercicioOpcao;
	
	public BigDecimal getPrecoExercicioOpcao(Dia dia, Opcao opcao) throws SATAEX {
		if (ativo instanceof Opcao) {
			if (momento == ABERTURA)
				return calculaPrecoExercicioOpcao(dia, opcao);

			else {
				precoExercicioOpcao = reversa.precoExercicioOpcao;
				return precoExercicioOpcao;
			}
		}
		return null;
	}
	
	private BigDecimal calculaPrecoExercicioOpcao(Dia dia, Opcao opcao) throws SATAEX {
		BigDecimal precoAcaoNoDiaDaOpcao = opcao.getAcao().getPreco(dia);
		BigDecimal volatilidade = opcao.getAcao().getVolatilidade(dia);
		precoExercicioOpcao = calculaPrecoExercicioOpcao(opcao, precoAcaoNoDiaDaOpcao, volatilidade);
		return precoExercicioOpcao;
	}
	
	public static BigDecimal calculaPrecoExercicioOpcao(Opcao opcao, BigDecimal precoAcao, BigDecimal volatilidade) {
		BigDecimal spread = new BigDecimal(SPREAD/100);
		BigDecimal multiplicador = new BigDecimal(opcao.getOrdem(volatilidade)).multiply(spread);
		return precoAcao.add(precoAcao.multiply(multiplicador));
	}
	
	public Operacao getReversa() {
		int momentoReverso = FECHAMENTO;
		reversa = criaOperacaoReversa(mesesParaVencimento, momentoReverso, diasParaVencimento);
		reversa.setAtivo(ativo);
		return reversa;
	}
	
	public int getDiasParaVencimento(Dia dia) {

		if (momento == ABERTURA) {
			//se for na abertura retorna a duração da operação
			return duracao;
		}
		else if (momento == FECHAMENTO) {
			//se for no fechamento retorna os dias que faltam para o vencimento
			return diasParaVencimento;
		}
		return 0;
	}
	
	public int getDiferenca(Dia dia1, Dia dia2) {
		int dif = SATAUtil.getDiferencaDias(dia1.getCalendar(), dia2.getCalendar());
		if (dif < 0)
			dif = SATAUtil.getDiferencaDias(dia1.getCalendar(), Simulacao.getDiaFechamento(dia1.getMes().getMesPosterior()).getCalendar());
		return dif;
		
//		long longDia = dia1.getCalendar().getTimeInMillis();
//		long longFechamento = dia2.getCalendar().getTimeInMillis();
//		if (longDia > longFechamento) {
//			dia2 = Simulacao.getDiaFechamento(dia1.getMes().getMesPosterior());
//			longFechamento = dia2.getCalendar().getTimeInMillis();
//		}
//		return (int) (((longFechamento - longDia) / (24*60*60*1000)) + 1);
	}
	
	public Operacao() {}
	
	public Operacao(Ativo ativo) {
		this.ativo = ativo;
	}
	
	public Operacao(Ativo ativo, boolean reversivel) {
		this.ativo = ativo;
		this.reversivel = reversivel;
	}
	
	public Operacao(Ativo ativo, int mesesParaVencimento) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, Condicao condicao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.condicao = condicao;
	}
	
	public Operacao(Ativo ativo, Condicao condicao) {
		this.ativo = ativo;
		this.condicao = condicao;
	}
	
	public Operacao(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
	}
	
	public Operacao(int qtdLotes, Ativo ativo) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
	}

	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, int diasParaVencimento) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.diasParaVencimento = diasParaVencimento;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
	}
	
	public Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, Condicao condicao, int diasParaVencimento) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.condicao = condicao;
		this.diasParaVencimento = diasParaVencimento;
	}
	
	protected Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, int momento, Condicao condicao, Operacao reversa) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
		this.condicao = condicao;
		this.reversa = reversa;
	}
	
	protected Operacao(int qtdLotes, Ativo ativo, int mesesParaVencimento, int momento, Condicao condicao, Operacao reversa, int diasParaVencimento) {
		this.qtdLotes = qtdLotes;
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
		this.condicao = condicao;
		this.reversa = reversa;
		this.diasParaVencimento = diasParaVencimento;
	}

	public String getString() {
		String strCondicao = "";
		String strMeses = "";
		String strDias = "";
		if (mesesParaVencimento > 1) 
			strMeses = SATAUtil.getMessage(MSG_PATTERN_OPERACAO_MESES, String.valueOf(mesesParaVencimento));
		if (condicao != null) strCondicao = " ["+condicao+"]";
		if (diasParaVencimento > 1) 
			strDias = SATAUtil.getMessage(MSG_PATTERN_OPERACAO_DIAS, String.valueOf(diasParaVencimento));
		return SATAUtil.getMessage(MSG_PATTERN_OPERACAO, toString(), strMeses, strDias, strCondicao);
	}
	
	@Override
	public String toString() {
		String strQtd = "";
		if (qtdLotes > 1) strQtd = qtdLotes + "x";
		return SATAUtil.getMessage(MSG_PATTERN_OPERACAO, strQtd, this.getBundleMessage(), ativo.toString(), "");
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,27).
	       append(qtdLotes).
	       append(momento).
	       append(ativo).
	       append(mesesParaVencimento).
	       append(condicao).
	       append(reversa).
	       append(diasParaVencimento).
	       append(reversivel).
	       append(executada).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int getQtdLotes() {
		return qtdLotes;
	}
	public void setQtdLotes(int qtdLotes) {
		this.qtdLotes = qtdLotes;
	}
	public void setAtivo(Ativo ativo) {
		this.ativo = ativo;
	}
	public Ativo getAtivo() {
		return ativo;
	}
	public int getMomento() {
		return momento;
	}
	public void setMomento(int momento) {
		this.momento = momento;
	}
	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}
	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}
	public Condicao getCondicao() {
		return condicao;
	}
	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}
	public void setReversa(Operacao reversa) {
		this.reversa = reversa;
	}
	public boolean isReversivel() {
		return reversivel;
	}
	public void setReversivel(boolean reversivel) {
		this.reversivel = reversivel;
	}
	public boolean isExecutada() {
		return executada;
	}
	public void setExecutada(boolean executada) {
		this.executada = executada;
	}
	public int getDiasParaVencimento() {
		return diasParaVencimento;
	}
	public void setDiasParaVencimento(int diasParaVencimento) {
		this.diasParaVencimento = diasParaVencimento;
	}
	public Dia getDiaVencimento() {
		return diaVencimento;
	}
	public void setDiaVencimento(Dia diaVencimento) {
		this.diaVencimento = diaVencimento;
	}
	public Dia getDiaAbertura() {
		return diaAbertura;
	}
	public void setDiaAbertura(Dia diaAbertura) {
		this.diaAbertura = diaAbertura;
		//calcula o dia de vencimento da operacao
		Dia diaVencimento = Simulacao.getDiaFechamento(diaAbertura.getMes().getMesPosterior());
		for (int i = 1; i < getMesesParaVencimento(); i++)
			diaVencimento = Simulacao.getDiaFechamento(diaVencimento.getMes().getMesPosterior());
		this.diaVencimento = diaVencimento;
		//calcula a duracao da operacao
		this.duracao = getDiferenca(diaAbertura, this.diaVencimento);
	}
	public int getDuracao() {
		//caso a duracao já tenha sido calculada atraves do dia da abertura e do vencimento
		if(duracao != 0)
			return duracao;
		else //caso não tenha sido calculada ela retorna uma estimativa de duracao
			return getMesesParaVencimento() * QTD_DIAS_MES - getDiasParaVencimento();
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}
	public Dia getDiaReversao() {
		return diaReversao;
	}
	public void setDiaReversao(Dia diaReversao) {
		this.diaReversao = diaReversao;
		//atualiza a quantidade de dias para fechamento pq depende do dia de reversão
		this.diasParaVencimento = getDiferenca(diaReversao, this.diaVencimento);
	}
}
