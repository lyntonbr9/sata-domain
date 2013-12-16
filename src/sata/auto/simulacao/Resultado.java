package sata.auto.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.exception.SATAEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.Mes;
import sata.auto.to.ValorOperacao;
import sata.auto.to.ValorResultado;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class Resultado implements IConstants {
	private List<ValorOperacao> resultados = new ArrayList<ValorOperacao>();
	
	private Integer anoInicial;
	private Integer anoFinal;
	private TipoCalculoValorInvestido tipoCalculoValorInvestido;
	private int percValorInvestido = 100;
	
	public void addResultado(Resultado resultado) {
		if (anoInicial == null || anoInicial.intValue() > resultado.anoInicial.intValue())
			this.anoInicial = resultado.anoInicial;
		if (anoFinal == null || anoFinal.intValue() < resultado.anoFinal.intValue())
			this.anoFinal = resultado.anoFinal;
		this.tipoCalculoValorInvestido = resultado.tipoCalculoValorInvestido;
		this.resultados.addAll(resultado.resultados);
	}
	
	public void addValorOperacao(ValorOperacao valorOperacao) {
		resultados.add(valorOperacao);
	}
	
	public void remove(Mes mes) {
		List<ValorOperacao> remover = new ArrayList<ValorOperacao>();
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes)) {
				remover.add(valorOperacao);
			}
		}
		resultados.removeAll(remover);
	}
	
	public boolean possui(Mes mes) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes))
				return true;
		}
		return false;
	}
	
	public boolean possui(Operacao operacao, Mes mes) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes) &&
					valorOperacao.getOperacao().equals(operacao))
				return true;
		}
		return false;
	}
	
	public void limpa() {
		resultados.clear();
	}
	
	public void setResultadoMensal(Operacao operacao, Mes mes, Preco preco) {
		setResultadoMensal(operacao, mes.getMes(), mes.getAno(), preco);
	}
	
	public void setResultadoMensal(Operacao operacao, Integer mes, Integer ano, Preco preco) {
		ValorOperacao valorOperacao = getValorOperacao(operacao, mes, ano);
		if (valorOperacao != null)
			valorOperacao.setPreco(preco);
		else addValorOperacao(new ValorOperacao(operacao, new Mes(mes,ano), preco));
	}
	
	public ValorOperacao getValorOperacao(Operacao operacao, Mes mes) {
		return getValorOperacao(operacao, mes.getMes(), mes.getAno());
	}
	
	public ValorOperacao getValorOperacao(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				return valorOperacao;
			}
		}
		return null;
	}
	
	public BigDecimal getResultadoMensal(Operacao operacao, Mes mes) {
		return getResultadoMensal(operacao, mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoMensal(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				return valorOperacao.getValor();
			}
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getResultadoMensal(Mes mes) {
		return getResultadoMensal(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoMensalAcao(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano) &&
				valorOperacao.getOperacao().getAtivo() instanceof Acao) {
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoMensal(Integer mes, Integer ano) {
		if (tipoCalculoValorInvestido == TipoCalculoValorInvestido.CUSTO_MONTAGEM_IGNORANDO_PRIMEIRO_MES
				&& mes.equals(1) && ano.equals(anoInicial)) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getValorInvestido(Mes mes) {
		return getValorInvestido(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getValorInvestidoAcao(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
					valorOperacao.getMes().getAno().equals(ano) &&
					valorOperacao.getOperacao() instanceof Compra &&
					valorOperacao.getOperacao().getAtivo() instanceof Acao) {
				valor = valor.add(valorOperacao.getValor().negate());
			}
		}
		return valor;
	}
	
	public BigDecimal getValorInvestido(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
					valorOperacao.getMes().getAno().equals(ano)) {
				
				try {
				switch (tipoCalculoValorInvestido) {
				case PRECO_ACAO:
						return valorOperacao.getPrecoAcao().multiply(new BigDecimal(percValorInvestido).divide(CEM));
				case CUSTO_MONTAGEM:
				case CUSTO_MONTAGEM_IGNORANDO_PRIMEIRO_MES:
					if (valorOperacao.getOperacao().getMomento() == ABERTURA)
						if (valorOperacao.getOperacao().getAtivo() instanceof RendaFixa)
							valor = valor.add(valorOperacao.getPrecoAcao());
						else valor = valor.add(valorOperacao.getValor().negate());
					break;
				case DIFERENCA_STRIKES:
					if (valorOperacao.getOperacao().getAtivo() instanceof Opcao &&
							valorOperacao.getOperacao().getMomento() == ABERTURA) {
						BigDecimal precoOpcao = ((PrecoOpcao)valorOperacao.getPreco()).getPrecoExercicioOpcao();
						if (valorOperacao.getOperacao() instanceof Venda)
							precoOpcao = precoOpcao.negate();
						valor = valor.add(precoOpcao);
					}
					break;
				}
				} catch (SATAEX e) {}
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualMensal(Mes mes) {
		return getResultadoPercentualMensal(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoPercentualMensal(Integer mes, Integer ano) {
		BigDecimal valorInicial = getValorInvestido(mes, ano);
		BigDecimal resultadoNominal = getResultadoMensal(mes,ano);
		if (valorInicial.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(CEM);
	}
	
	public BigDecimal getResultadoPercentualMensalAcao(Mes mes) {
		return getResultadoPercentualMensalAcao(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoPercentualMensalAcao(Integer mes, Integer ano) {
		BigDecimal valorInicial = getValorInvestidoAcao(mes, ano);
		BigDecimal resultadoNominal = getResultadoMensalAcao(mes, ano);
		if (valorInicial.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(CEM);
	}
	
	public BigDecimal getResultadoAnual(Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (int mes=1; mes<=12; mes++) {
			valor = valor.add(getResultadoMensal(mes, ano));
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualAnual(Integer ano) {
		BigDecimal valorInicial = getValorInvestido(1, ano);
		BigDecimal resultadoNominal = getResultadoAnual(ano);
		if (valorInicial.equals(BigDecimal.ZERO)) 
			return BigDecimal.ZERO;
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(CEM);
	}
	
	public BigDecimal getResultadoComReivestimento(BigDecimal valorInicial) {
		BigDecimal caixa = new BigDecimal(valorInicial.doubleValue());
		Collections.sort(resultados);
		for (int ano=anoInicial; ano<=anoFinal; ano++) 
			for (int mes=1; mes<=12; mes++) {
				caixa = getResultadoMensalComReivestimento(caixa, ano, mes);
			}
		return caixa;
	}
	
	public BigDecimal getResultadoMensalComReivestimento(BigDecimal caixa, Integer ano, Integer mes) {
		BigDecimal valor1Lote = getValorInvestido(mes, ano);
		if (valor1Lote.equals(BigDecimal.ZERO))
			return caixa;
		BigDecimal numLotes = new BigDecimal(Math.round(caixa.divide(valor1Lote,RoundingMode.HALF_EVEN).doubleValue()));
		BigDecimal valorGanho = getResultadoMensal(mes, ano).multiply(numLotes);
		caixa = caixa.add(valorGanho);
		return caixa;
	}
	
	public BigDecimal getMediaAnual() {
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal qtdAnos = BigDecimal.ZERO;
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			soma = soma.add(getResultadoPercentualAnual(ano));
			qtdAnos = qtdAnos.add(BigDecimal.ONE);
		}
		BigDecimal mediaAnual = soma.divide(qtdAnos, RoundingMode.HALF_EVEN);
		return mediaAnual;
	}
	
	public BigDecimal getMediaMensal() {
		return getMediaAnual().divide(new BigDecimal(12), RoundingMode.HALF_EVEN);
	}
	
	public List<ValorResultado> listaResultadosAnuais() {
		List<ValorResultado> resultados = new ArrayList<ValorResultado>();
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			resultados.add(new ValorResultado(String.valueOf(ano), getResultadoPercentualAnual(ano), 
					getResultadoAnual(ano), getValorInvestido(1,ano)));
		}
		return resultados;
	}
	
	public List<ValorResultado> listaResultadosMensais() {
		List<ValorResultado> resultados = new ArrayList<ValorResultado>();
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			if (ano != anoInicial.intValue())
				resultados.add(new ValorResultado());
			for (int iMes=1; iMes <= 12; iMes++) {
				Mes mes = new Mes(iMes,ano);
				if (possui(mes)) {
					resultados.add(new ValorResultado(mes.toString(), getResultadoPercentualMensal(mes),
							getResultadoMensal(mes), getValorInvestido(mes)));
				}
			}
		}
		return resultados;
	}
	
	public List<ValorResultado> listaResultadoComReinvestimento() {
		BigDecimal valorInicial = CEM;
		BigDecimal valorFinal = getResultadoComReivestimento(valorInicial);
		BigDecimal caixa = valorInicial;
		List<ValorResultado> resultados = new ArrayList<ValorResultado>();
		resultados.add(new ValorResultado(SATAUtil.getMessage(MSG_SIMULACAO_LABEL_VALOR_INICIAL),valorInicial));
		for (int ano=anoInicial; ano<=anoFinal; ano++) 
			for (int mes=1; mes<=12; mes++) {
				BigDecimal valor = getResultadoMensalComReivestimento(caixa, ano, mes);
				resultados.add(new ValorResultado(new Mes(mes,ano).toString(),valor));
				caixa = valor;
			}
		resultados.add(new ValorResultado(SATAUtil.getMessage(MSG_SIMULACAO_LABEL_VALOR_FINAL),valorFinal));
		return resultados;
	}
	
	public String imprime(TipoRelatorio tipoRelatorio) throws CotacaoInexistenteEX {
		String string = "";
		
		if (tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += imprimeOperacoes()+"\n";
		}
		
		if (tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += imprimeResultadosMensais()+"\n";
		}
		
		if (tipoRelatorio == TipoRelatorio.ANUAL ||
			tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += imprimeResultadosAnuais()+"\n";
		}
		
		if (tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.ANUAL ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += imprimeResultadoConsolidado();
		}
		
		if (tipoRelatorio == TipoRelatorio.REINVESTIMENTO) {
			string += imprimeResultadoComReinvestimento();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV) {
			string += imprimeCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV_MENSAL) {
			string += imprimeResultadosMensaisCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV_MENSAL) {
			string += imprimeResultadosMensaisCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV_REINVESTIMENTO) {
			string += imprimeResultadoComReinvestimentoCSV();
		}
		
		return string+"\n";
	}
	
	public static boolean imprimeInicioFim(TipoRelatorio tipoRelatorio) {
		return (tipoRelatorio != TipoRelatorio.NENHUM) &&
			(tipoRelatorio != TipoRelatorio.CSV) && 
			(tipoRelatorio != TipoRelatorio.CSV_MENSAL) &&
			(tipoRelatorio != TipoRelatorio.CSV_REINVESTIMENTO);
	}
	
	public static String getExtensaoArquivoSaida(TipoRelatorio tipoRelatorio) {
		switch (tipoRelatorio) {
		case CSV:
		case CSV_MENSAL:
		case CSV_REINVESTIMENTO:
			return "csv";
		default:
			return "txt";
		}
	}
	
	private String imprimeValoresResultado(List<ValorResultado> valoresResultados, String... separador) {
		String string = "";
		for (ValorResultado valorResultado : valoresResultados) {
			if (valorResultado.isEmpty())
				string += "\n";
			else {
				int i=0;
				string += "\n" + valorResultado.getLabel() + separador[i++];
				for (BigDecimal valor: valorResultado.getValores()) {
					string += SATAUtil.formataNumero(valor);
					if (i < separador.length)
						string += separador[i++];
				}
			}
		}
		return string;
	}
	
	private String imprimeOperacoes() {
		String string = "";
		Collections.sort(resultados);
		Mes mesAnterior = null;
		for (ValorOperacao valorOperacao : resultados) {
			if (!resultados.get(0).equals(valorOperacao) &&
					!valorOperacao.getMes().equals(mesAnterior)){
				string += getResultadoMensal(mesAnterior);
				string += "\n";
			}
			string += "\n" + valorOperacao;
			mesAnterior = valorOperacao.getMes();
		}
		return string;
	}
	
	private String imprimeResultadosAnuais(String... separador) {
		return imprimeValoresResultado(listaResultadosAnuais(), separador);
	}
	
	private String imprimeResultadosAnuais() {
		return imprimeResultadosAnuais(" = ", "% (", "/", ")");
	}
	
	private String imprimeResultadosMensais(String... separador) {
		return imprimeValoresResultado(listaResultadosMensais(), separador);
	}
	
	private String imprimeResultadosMensaisCSV() {
		return imprimeResultadosMensais(";", ";", ";", ";");
	}
	
	private String imprimeResultadosMensais() {
		return imprimeResultadosMensais(" = ", "% (", "/", ")");
	}
	
	private String imprimeResultadoComReinvestimento(String... separador) {
		return imprimeValoresResultado(listaResultadoComReinvestimento(), separador);
	}
	
	private String imprimeResultadoComReinvestimentoCSV() {
		return imprimeResultadoComReinvestimento(";");
	}
	
	private String imprimeResultadoComReinvestimento() {
		return imprimeResultadoComReinvestimento(": ");
	}
	
	private String imprimeResultadoConsolidado() {
		String string = "\n" + SATAUtil.getMessage(MSG_SIMULACAO_LABEL_MEDIA_ANUAL) + ": " + SATAUtil.formataNumero(getMediaAnual()) + "%";
		string += "\n" + SATAUtil.getMessage(MSG_SIMULACAO_LABEL_MEDIA_MENSAL) + ": " + SATAUtil.formataNumero(getMediaMensal()) + "%";
		return string;
	}
	
	private String imprimeCSV() {
		String string = "Operação;Dia;Valor;Preço de Exercício;Preço da Ação;Volatilidade\n";
		Collections.sort(resultados);
		for (ValorOperacao valorOperacao : resultados) {
			string += imprimeCSV(valorOperacao.getOperacao(), valorOperacao.getMes()) + "\n";
		}
		return string;
	}
	
	private String imprimeCSV(Operacao operacao, Mes mes) {
		String string = "";
		ValorOperacao valorOperacao = getValorOperacao(operacao, mes);
		string += valorOperacao.getOperacao()+";";
		string += valorOperacao.getPreco().getDia()+";";
		string += SATAUtil.formataNumero(valorOperacao.getValor())+";";
		if (valorOperacao.getPreco() instanceof PrecoOpcao) {
			string += SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoExercicioOpcao())+";";
			string += SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoAcao())+";";
		}
		else string += ";;";
		string += SATAUtil.formataNumero(valorOperacao.getPreco().getVolatilidade());
		return string;
	}
	
	public void setAnoInicial(Integer anoInicial) {
		this.anoInicial = anoInicial;
	}
	public Integer getAnoInicial() {
		return anoInicial;
	}
	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}
	public Integer getAnoFinal() {
		return anoFinal;
	}
	public List<ValorOperacao> getResultados() {
		return resultados;
	}
	public void setResultados(List<ValorOperacao> resultados) {
		this.resultados = resultados;
	}
	public TipoCalculoValorInvestido getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}
	public void setTipoCalculoValorInvestido(
			TipoCalculoValorInvestido tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}
	public int getPercValorInvestido() {
		return percValorInvestido;
	}
	public void setPercValorInvestido(int percValorInvestido) {
		this.percValorInvestido = percValorInvestido;
	}
}
