package sata.domain.alert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AcompOpcaoTO;
import sata.domain.to.AcompanhamentoTO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.CotacaoOpcaoTO;
import sata.domain.to.OpcaoTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class AcompOpcoes implements IConstants {
	
	public static void verificarOpcoes() throws Exception {
		for (AcompanhamentoTO acompanhamento : SATAFactoryFacade.getAcompanhamentoDAO().listar()) {
			System.out.println("---");
			System.out.println(getMensagemAcompanhamento(acompanhamento, true));
		}
	}
	
	public static String getMensagemAcompanhamento(AcompanhamentoTO acompanhamento, boolean sendMail) throws SQLException, MessagingException {
		BigDecimal precoAcaoAtual = acompanhamento.getPrecoAcaoAtual();
		String msg = SATAUtil.getMessage(MSG_ACOMPANHAMENTO_LABEL_ACOMPANHAMENTO) + " " + acompanhamento.getNome() + "\n";
		msg += "- " + SATAUtil.getMessage(MSG_GENERAL_LABEL_PRECO_ACAO) + " = "   + SATAUtil.formataNumero(precoAcaoAtual) + "\n";
		msg += "- " + SATAUtil.getMessage(MSG_GENERAL_LABEL_VOLATILIDADE) + " = {*}% \n";
		int opcoesEncontradas = 0;
		List<OpcaoTO> opcoes = SATAFactoryFacade.getOpcaoDAO().pesquisa(acompanhamento.getAcao(), acompanhamento.getDataVencimento());
		for (AcompOpcaoTO acompOpcao : acompanhamento.getAcompanhamentos()) {
			int noPreco = 0;
			for (OpcaoTO opcao : opcoes) {
				BigDecimal precoExercicio = opcao.getPrecoExercicio();
				BigDecimal percentual = precoExercicio.subtract(precoAcaoAtual).divide(precoAcaoAtual, RoundingMode.HALF_EVEN).multiply(CEM);
				if (percentual.doubleValue() >= acompOpcao.getPercToleradoInferior()
						&& percentual.doubleValue() <= acompOpcao.getPercToleradoSuperior()) {
					//					if (true) {
					msg += "- " + SATAUtil.getMessage(MSG_GENERAL_LABEL_OPCAO) + " " + acompOpcao.getPercExercicio() + "%: "
					+ opcao.getCodigo() 
					+ "(" + SATAUtil.formataNumero(precoExercicio) + ")"
					+ " => " + SATAUtil.formataNumero(percentual) + "%"
					+ " = " + SATAUtil.formataNumero(opcao.getPrecoAtual())
					+ " (B&S = {["+ opcao.getCodigo() + "]})\n";
					noPreco++;
				}
			}
			if (noPreco > 0) {
				opcoesEncontradas++;
			}
			else {
				msg += "- " + SATAUtil.getMessage(MSG_GENERAL_LABEL_OPCAO) + " " 
					+ acompOpcao.getPercExercicio() + "%: "+ SATAUtil.getMessage(MSG_ACOMPANHAMENTO_MSG_NAO_ENCONTRADA) + "\n" ;
			}
		}
		if (opcoesEncontradas > 0) {
			msg = atualizaMsg(opcoes, acompanhamento, msg);
			if (opcoesEncontradas >= acompanhamento.getAcompanhamentos().size()) {
				String assunto = SATAUtil.getMessage(MSG_ACOMPANHAMENTO_EMAIL_ASSUNTO, acompanhamento.getNome());
				SATAUtil.sendMail(assunto, msg, acompanhamento.getInvestidor().getEmail());
			}
		}
		else {
			msg += SATAUtil.getMessage(MSG_ACOMPANHAMENTO_MSG_NENHUMA_OPCAO_ENCONTRADA);
		}
		return msg;
	}
	
	public static BigDecimal calculaVolatilidade(List<OpcaoTO> opcoes, BigDecimal precoAcaoAtual) {
		OpcaoTO opcaoMaisATM = getOpcaoMaisATM(opcoes, precoAcaoAtual);
		BigDecimal precoOpcao = opcaoMaisATM.getPrecoAtual();
		BigDecimal precoExercicioOpcao = opcaoMaisATM.getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), opcaoMaisATM.getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.calculaVolatilidade(true, precoAcaoAtual, precoExercicioOpcao, diasParaVencimento, precoOpcao, taxaJuros);
	}
	
	public static BigDecimal getBlackScholes(OpcaoTO opcao, BigDecimal precoAcaoAtual, BigDecimal volatilidade) {
		BigDecimal precoExercicioOpcao = opcao.getPrecoExercicio();
		int diasParaVencimento = SATAUtil.getDiferencaDias(new Date(), opcao.getDataVencimento());
		BigDecimal taxaJuros = new BigDecimal(SATAUtil.getTaxaDeJuros());
		return PrecoOpcao.blackScholes(true, precoAcaoAtual, precoExercicioOpcao, diasParaVencimento, volatilidade, taxaJuros);
	}
	
	public static OpcaoTO getOpcaoMaisATM(List<OpcaoTO> opcoes, BigDecimal precoAcaoAtual) {
		double menorDiferenca = Double.POSITIVE_INFINITY;
		OpcaoTO opcaoMaisATM = null;
		for (OpcaoTO opcao : opcoes) {
			double diferenca = opcao.getPrecoExercicio().subtract(precoAcaoAtual).abs().doubleValue();
			if (diferenca < menorDiferenca) {
				menorDiferenca = diferenca;
				opcaoMaisATM = opcao;
			}
		}
		return opcaoMaisATM;
	}
	
	public static CotacaoOpcaoTO getCotacaoOpcaoMaisATM(List<CotacaoOpcaoTO> cotacoesOpcoes, CotacaoAtivoTO ca) {
		double menorDiferenca = Double.POSITIVE_INFINITY;
		CotacaoOpcaoTO opcaoMaisATM = null;
		for(CotacaoOpcaoTO co : cotacoesOpcoes){
			//verifica se a cotacao da ação está dentro da série da opção
			int diferencaDias = SATAUtil.getDiferencaDias(ca.getPeriodo(), co.getDataVencimento()); 
			if ( diferencaDias > 0 && diferencaDias < 31 ) {
				double diferenca = co.getValorPrecoExercicio().subtract(ca.getValorFechamento()).abs().doubleValue();
				if (diferenca < menorDiferenca) {
					menorDiferenca = diferenca;
					opcaoMaisATM = co;
				}				
			}
		}
		return opcaoMaisATM;
	}
	
//	public static CotacaoOpcaoTO getCotacaoOpcaoMaisATM(List<CotacaoOpcaoTO> cotacoesOpcoes, BigDecimal precoAcaoAtual){
//		List<OpcaoTO> opcoes = new ArrayList<OpcaoTO>();
//		for(CotacaoOpcaoTO coTO : cotacoesOpcoes){
//			OpcaoTO oTO = new OpcaoTO();
//			oTO.setCodigo(coTO.getCodigo());
//			oTO.setPrecoExercicio(coTO.getValorPrecoExercicio());
//			opcoes.add(oTO);
//		}		
//		OpcaoTO opcaoATM = getOpcaoMaisATM(opcoes, precoAcaoAtual);
//		for(CotacaoOpcaoTO coTO : cotacoesOpcoes){
//			if(coTO.getCodigo().equalsIgnoreCase(opcaoATM.getCodigo())){
//				return coTO;
//			}
//		}
//		return null;
//	}
	
	private static String atualizaMsg(List<OpcaoTO> opcoes, AcompanhamentoTO acompanhamento, String msg) {
		BigDecimal volatilidade = calculaVolatilidade(opcoes, acompanhamento.getPrecoAcaoAtual());
		msg = msg.replace("{*}",SATAUtil.formataNumero(volatilidade.multiply(CEM)));
		for (OpcaoTO opcao : opcoes) {
			msg = msg.replace("{["+ opcao.getCodigo() + "]}", 
					SATAUtil.formataNumero(getBlackScholes(opcao, acompanhamento.getPrecoAcaoAtual(), volatilidade)));
		}
		return msg;
	}
	
	public static void main(String[] args) throws Exception {
		verificarOpcoes();
	}
}
