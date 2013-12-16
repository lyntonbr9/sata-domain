package sata.domain.alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.web.util.LocaleUtil;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AlertaTO;
import sata.domain.to.OpcaoTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class AlertaOperacao implements IConstants {
	
	public static void verificarAlertasOperacoesAtivos() throws Exception {
		IAlertaDAO dao = SATAFactoryFacade.getAlertaDAO();
		List<AlertaTO> alertasAtivos = dao.listaAlertasAtivos();
		for (AlertaTO alerta: alertasAtivos) {
			for (SerieOperacoesTO serie: alerta.getSeries()) {
				String msg = getMensagemSerie(serie);
				BigDecimal percentual = getPercentualSerie(serie);
				System.out.println("---");
				System.out.println(msg);
				if (alerta.alertaPorcentagemGanho(percentual)) {
					String assunto = SATAUtil.getMessage(MSG_ALERTA_EMAIL_ASSUNTO_GANHO,
							alerta.getPorcentagemGanho().toString(),SATAUtil.getSomenteDataAtualFormatada()); 
					SATAUtil.sendMail(assunto, msg, serie.getInvestidor().getEmail());
				} else if (alerta.alertaPorcentagemPerda(percentual)) {
					String assunto = SATAUtil.getMessage(MSG_ALERTA_EMAIL_ASSUNTO_PERDA,
							alerta.getPorcentagemPerda().toString(),SATAUtil.getSomenteDataAtualFormatada());
					SATAUtil.sendMail(assunto, msg, serie.getInvestidor().getEmail());
				}
			}
		}
	}
	
	public static String getMensagemSerie(SerieOperacoesTO serie) {
		BigDecimal valorAbertura = BigDecimal.ZERO;
		BigDecimal valorFechamento = BigDecimal.ZERO;
		String msg = "";
		msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_ALERTA) + " " + serie.getAlerta().getNome() + "\n";
		msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_SERIE) + " " 
			+ SATAUtil.getMessage(MSG_GENERAL_LABEL_OF) + " " 
			+ serie.getInvestidor().getNome() + " "
			+ SATAUtil.getMessage(MSG_GENERAL_LABEL_OF) + " "
			+ LocaleUtil.formataData(serie.getDataExecucao()) + "\n";
		BigDecimal volatilidade = calculaVolatilidade(serie);
		for (OperacaoRealizadaTO op: serie.getOperacoes()) {
			valorAbertura = valorAbertura.add(op.getValorReal().multiply(new BigDecimal(op.getQtdLotes())));
			BigDecimal valorAtualTotal = op.getValorAtual().multiply(new BigDecimal(op.getQtdLotes()));
			valorFechamento = valorFechamento.add(valorAtualTotal);
			msg += SATAUtil.getMessage(MSG_GENERAL_LABEL_OPERACAO) + " " + op.getPosicao() 
				+ " " + op.getAtivo() + " = " 
				+ SATAUtil.formataNumero(op.getValorReal()) + " --> " 
				+ SATAUtil.formataNumero(op.getValorAtual()) + " (B&S = "
				+ SATAUtil.formataNumero(getBlackScholes(op, volatilidade)) + ")\n";
		}
		BigDecimal valorInvestido = getValorInvestido(serie);
		BigDecimal valorSerie = valorAbertura.add(valorFechamento);
		BigDecimal percentual = valorSerie.divide(valorInvestido, RoundingMode.HALF_EVEN).multiply(CEM);
		msg += SATAUtil.getMessage(MSG_GENERAL_LABEL_PRECO_ACAO) + " = " 
			+  SATAUtil.formataNumero(serie.getPrecoAcao()) + " --> "
			+  SATAUtil.formataNumero(serie.getPrecoAcaoAtual()) + "\n";
		msg += SATAUtil.getMessage(MSG_GENERAL_LABEL_VOLATILIDADE) + " = " 
			+ SATAUtil.formataNumero(volatilidade.multiply(CEM)) + "%\n";
		msg += SATAUtil.getMessage(MSG_GENERAL_LABEL_VALOR_INVESTIDO) + " = " 
			+ SATAUtil.formataNumero(valorInvestido) + "\n";
		msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_VALOR_SERIE) + " = " 
			+ SATAUtil.formataNumero(valorSerie) + "\n";
		msg += SATAUtil.getMessage(MSG_ALERTA_LABEL_PREC_SERIE) + " = " 
			+ SATAUtil.formataNumero(percentual)+ "%";
		return msg;
	}
	
	public static BigDecimal getPercentualSerie(SerieOperacoesTO serie) {
		BigDecimal valorAbertura = BigDecimal.ZERO;
		BigDecimal valorFechamento = BigDecimal.ZERO;
		for (OperacaoRealizadaTO op: serie.getOperacoes()) {
			valorAbertura = valorAbertura.add(op.getValorReal().multiply(new BigDecimal(op.getQtdLotes())));
			BigDecimal valorAtualTotal = op.getValorAtual().multiply(new BigDecimal(op.getQtdLotes()));
			valorFechamento = valorFechamento.add(valorAtualTotal);
		}
		BigDecimal valorInvestido = getValorInvestido(serie);
		BigDecimal valorSerie = valorAbertura.add(valorFechamento);
		BigDecimal percentual = valorSerie.divide(valorInvestido, RoundingMode.HALF_EVEN).multiply(CEM);
		return percentual;
	}
	
	private static BigDecimal getValorInvestido(SerieOperacoesTO serie) {
		switch (serie.getAlerta().getTipoCalculoVI()) {
		case PRECO_ACAO:
			BigDecimal precoTotalAcao = serie.getPrecoAcao().multiply(new BigDecimal(serie.getQtdLotesAcao()));
			BigDecimal percentual = (new BigDecimal(serie.getAlerta().getPercCalculoVI())).divide(CEM);
			return precoTotalAcao.multiply(percentual);
			
		case CUSTO_MONTAGEM:
			BigDecimal valorAbertura = BigDecimal.ZERO;
			for (OperacaoRealizadaTO op: serie.getOperacoes()) 
				valorAbertura = valorAbertura.add(op.getValorReal().multiply(new BigDecimal(op.getQtdLotes())));
			return valorAbertura;

		default:
			return BigDecimal.ZERO;
		}
	}
	
	private static BigDecimal getBlackScholes(OperacaoRealizadaTO operacao, BigDecimal volatilidade) {
		BigDecimal precoAcao = operacao.getSerie().getPrecoAcaoAtual();
		BigDecimal precoExercicioOpcao = operacao.getOpcao().getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), operacao.getOpcao().getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.blackScholes(true, precoAcao, precoExercicioOpcao, diasParaVencimento, volatilidade, taxaJuros);
	}
	
	private static BigDecimal calculaVolatilidade(SerieOperacoesTO serie) {
		OpcaoTO opcaoMaisATM = getOpcaoMaisATM(serie.getOperacoes());
		BigDecimal precoAcao = serie.getPrecoAcaoAtual();
		BigDecimal precoOpcao = opcaoMaisATM.getPrecoAtual();
		BigDecimal precoExercicioOpcao = opcaoMaisATM.getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), opcaoMaisATM.getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.calculaVolatilidade(true, precoAcao, precoExercicioOpcao, diasParaVencimento, precoOpcao, taxaJuros);
	}
	
	private static OpcaoTO getOpcaoMaisATM(List<OperacaoRealizadaTO> operacoes) {
		double menorDiferenca = Double.POSITIVE_INFINITY;
		OpcaoTO opcaoMaisATM = null;
		for (OperacaoRealizadaTO op : operacoes) {
			double diferenca = op.getOpcao().getPrecoExercicio().subtract(op.getSerie().getPrecoAcaoAtual()).abs().doubleValue();
			if (diferenca < menorDiferenca) {
				menorDiferenca = diferenca;
				opcaoMaisATM = op.getOpcao();
			}
		}
		return opcaoMaisATM;
	}
	
	public static void main(String[] args) throws Exception {
		verificarAlertasOperacoesAtivos();
	}
}
