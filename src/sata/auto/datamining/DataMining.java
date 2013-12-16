package sata.auto.datamining;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sata.auto.enums.TipoRelatorio;
import sata.auto.estrategia.Estrategia;
import sata.auto.estrategia.Lhama2;
import sata.auto.estrategia.SomenteAcao;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.to.Variacao;
import sata.domain.util.IConstants;
import sata.domain.util.LoggerUtil;
import sata.domain.util.SATAUtil;

public class DataMining implements IConstants {
	
	public static void main(String[] args) throws Exception {
		Variacao stop = new Variacao(1,7);
		Variacao diasParaFechamento = new Variacao(7,7);
		dataMine(Lhama2.class, "PETR4", 2000, 2011, stop, diasParaFechamento);
	}
	
	public static void dataMine(Class<? extends Estrategia> clazz, String acao, int anoInicial, int anoFinal, Variacao... variacoes) throws Exception {
		try {
			LoggerUtil.setup("DM_"+clazz.getSimpleName()+"_"+acao+"_"+anoInicial+"_"+anoFinal);
			LoggerUtil.log("Início: " + SATAUtil.getDataAtualFormatada());
			BigDecimal valorInicial = CEM;
			BigDecimal melhorResultado = BigDecimal.ZERO;
			String melhorSimulacao = "";

			variacoes = SATAUtil.reverte(variacoes);
			List<Integer[]> listaParametros = criaListaParametros(variacoes, variacoes.length, new ArrayList<Integer>());

			for (Integer[] parametros: listaParametros) {
				Estrategia estrategia = (Estrategia) clazz.getDeclaredConstructor().newInstance();
				estrategia.setAcao(AcaoConteiner.get(acao));
				estrategia.setAnoInicial(anoInicial);
				estrategia.setAnoFinal(anoFinal);
				estrategia.executa(TipoRelatorio.NENHUM, parametros);
				String simulacao = estrategia.getTextoEstrategia("; ",parametros);
				BigDecimal resultado = estrategia.getResultado().getResultadoComReivestimento(valorInicial).subtract(valorInicial);
				LoggerUtil.log(simulacao + " = " + SATAUtil.formataNumero(resultado));

				if (resultado.doubleValue() > melhorResultado.doubleValue()) {
					melhorResultado = resultado;
					melhorSimulacao = simulacao;
				}
			}
			LoggerUtil.log("\nMelhor Simulação: " + melhorSimulacao + " = " + SATAUtil.formataNumero(melhorResultado));
			LoggerUtil.log("\nSomente Ação = " + SATAUtil.formataNumero(getResultadoSomenteAcao(acao, anoInicial, anoFinal, valorInicial)));
			LoggerUtil.log("\nFim: " + SATAUtil.getDataAtualFormatada());
		}catch (Exception e) {
			LoggerUtil.log(e.getMessage());
		}
	}
	
	private static List<Integer[]> criaListaParametros(Variacao[] variacao, int nivel, List<Integer> params) {
		List<Integer[]> parametros = new ArrayList<Integer[]>();
		if (nivel>0) {
			for (int i=variacao[nivel-1].getValorInicial(); i<=variacao[nivel-1].getValorFinal(); i+=variacao[nivel-1].getIncremento()) {
				params.add(i);
				parametros.addAll(criaListaParametros(variacao, nivel-1, params));
			}
			if (!params.isEmpty()) 
				params.remove(params.size()-1);
		}
		else {
			Integer[] array = new Integer[params.size()];
			parametros.add(SATAUtil.copyToArray(params, array));
			params.remove(params.size()-1);
		}
		return parametros;
	}
	
	private static BigDecimal getResultadoSomenteAcao(String acao, int anoInicial, int anoFinal, BigDecimal valorInicial) {
		Estrategia estrategia = new SomenteAcao();
		estrategia.setAcao(AcaoConteiner.get(acao));
		estrategia.setAnoInicial(anoInicial);
		estrategia.setAnoFinal(anoFinal);
		estrategia.executa(TipoRelatorio.NENHUM);
		return estrategia.getResultado().getResultadoComReivestimento(valorInicial).subtract(valorInicial);
	}
}
