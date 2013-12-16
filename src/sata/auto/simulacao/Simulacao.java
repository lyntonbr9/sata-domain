package sata.auto.simulacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Stop;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.Dia;
import sata.auto.to.Mes;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class Simulacao implements IConstants {
	
	private List<Operacao> operacoes = new ArrayList<Operacao>();
	private Integer anoInicial;
	private Integer anoFinal;
	private TipoCalculoValorInvestido tipoCalculoValorInvestido;
	private int percValorInvestido = 100;
	private Stop stop;
	private int diasParaVencimento;
	
	public Simulacao(Operacao... operacoes) {
		this.operacoes = Arrays.asList(operacoes);
	}
	
	public Resultado getResultado() throws SATAEX {
		System.gc();
		Resultado resultado = new Resultado();
		resultado.setAnoInicial(anoInicial);
		resultado.setAnoFinal(anoFinal);
		resultado.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
		resultado.setPercValorInvestido(percValorInvestido);
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			if (possuiCotacaoNoAno(ano)) {
				for (int iMes=1; iMes <= 12; iMes++) {
					Mes mes = new Mes(iMes,ano);
					try {
						//executa a abertura das operações
						executaOperacoes(resultado, operacoes, mes, getDiaAbertura(mes));
						
						//tem que atualizar os dias para fechamento escolhido a cada mes
						//para que possa fazer novo calculo de qual sera o dia de reversao
						for(Operacao op: operacoes) {
							op.setDiasParaVencimento(diasParaVencimento);
						}
							
						Dia diaReversao;
						if (stop != null) //se houver stop 
							diaReversao = getDiaAbertura(mes).getProximoDia();
						else //se nao houver stop, calcula o dia de reversao
							diaReversao = getDiaReversao(mes, operacoes);
						do  {
							//executa a reversão das operações
							executaOperacoesReversas(resultado, operacoes, mes, diaReversao);
							if(stop == null) //se não tiver stop
								break;
							if (diaReversao.equals(getDiaFechamento(mes))) //se tiver stop
								break;
							diaReversao = diaReversao.getProximoDia();
						} while (!stop(resultado, mes));

					} catch (CotacaoInexistenteEX e) {
						resultado.remove(mes);
					}
				}
			}
		}
		limpaCaches();
		return resultado;
	}
	
	private void executaOperacoes(Resultado resultado, List<Operacao> operacoes, Mes mes, Dia dia) throws SATAEX {
		for (Operacao operacao : operacoes) {
			Dia diaOperacao = dia;
			//atualiza o dia da operacao que sera executada
			operacao.setDiaAbertura(diaOperacao);
			//executa a operacao de abertura
			executaOperacao(resultado, operacao, mes, diaOperacao, false);
		}
	}
	
	private void executaOperacoesReversas(Resultado resultado, List<Operacao> operacoes, Mes mesReversao, Dia diaReversao) throws SATAEX {
		for (Operacao operacao : operacoes) {
			if (operacao.isReversivel() && operacao.isExecutada()) {
				if (resultado.possui(operacao, mesReversao)) {
					//executa a operacao de reversão
					executaOperacao(resultado, operacao.getReversa(), mesReversao, diaReversao, true);
				}
			}
		}
	}
	
	private void executaOperacao(Resultado resultado, Operacao operacao, Mes mes, Dia dia, boolean reversa) throws SATAEX {
		Preco preco = operacao.getPreco(dia);
		boolean condicaoVerdadeira = operacao.condicaoVerdadeira(preco);
		if (reversa) condicaoVerdadeira = true;
		if (condicaoVerdadeira) {
			resultado.setResultadoMensal(operacao, mes, preco);
			operacao.setExecutada(true);
		}
	}
	
	private boolean stop(Resultado resultado, Mes mes) {
		if (stop == null) return false;
		return stop.stop(mes, resultado);
	}
	
	public static Dia getDiaAbertura(Mes mes) {
		Mes mesCotacao = mes.getMesAnterior();
		return new Dia(SATAUtil.getDiaMes(mesCotacao.getAno(), mesCotacao.getMes(), Calendar.MONDAY, 3), mesCotacao);
	}
	
	public static Dia getDiaFechamento(Mes mes) {
//		return new Dia(SATAUtil.getDiaMes(mes.getAno(), mes.getMes(), Calendar.MONDAY, 3)-3, mes);
		return new Dia(SATAUtil.getDiaMes(mes.getAno(), mes.getMes(), Calendar.MONDAY, 3), mes);
	}
	
	public static Dia getDiaReversao(Mes mes, List<Operacao> operacoes) {
		Dia diaReversao;
		Operacao operacao = null;
		int qtdDias = Integer.MAX_VALUE;
		//calcula qual é a operacao com menor duração para operacao
		for(Operacao op: operacoes) {
			if(op.isReversivel() && qtdDias >= op.getDuracao()){
				qtdDias = op.getDuracao();
				operacao = op;
			}
		}
		//calcula o dia de reversao a partir da operacao com menor duração
		diaReversao = getDiaFechamento(mes);
		for (int i = 1; i < operacao.getMesesParaVencimento(); i++) {
			diaReversao = getDiaFechamento(diaReversao.getMes().getMesPosterior());
		}
		//calcula o dia de reversao no mes de analise
		if (operacao.getDiasParaVencimento() > 1) {
			diaReversao = diaReversao.getDiaAnterior(operacao.getDiasParaVencimento());
		}
		//tem que atualizar a quantidade de dias para vencimento das operações
		for(Operacao op: operacoes) {
			//atualiza a quantidade de dias que restam para o vencimento
			op.setDiaReversao(diaReversao);
		}
		
		return diaReversao;
	}
	
	private boolean possuiCotacaoNoAno(int ano) {
		try {
			String codigoAcao = operacoes.get(0).getAtivo().getAcao().getNome();
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			return caDAO.possuiCotacaoNoAno(codigoAcao, ano);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void limpaCaches() {
		for (Operacao operacao: operacoes) {
			operacao.getAtivo().limpaPrecos();
		}
	}
	
	public List<Operacao> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
	}
	public Integer getAnoInicial() {
		return anoInicial;
	}
	public void setAnoInicial(Integer anoInicial) {
		this.anoInicial = anoInicial;
	}
	public Integer getAnoFinal() {
		return anoFinal;
	}
	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}
	public Stop getStop() {
		return stop;
	}
	public void setStop(Stop stop) {
		this.stop = stop;
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

	public int getDiasParaVencimento() {
		return diasParaVencimento;
	}

	public void setDiasParaVencimento(int diasParaVencimento) {
		this.diasParaVencimento = diasParaVencimento;
	}
}
