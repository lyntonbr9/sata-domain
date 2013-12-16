package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Stop;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class Lhama2 extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		Venda rendaFixa = new Venda(new RendaFixa(acao));
		rendaFixa.setReversivel(false);
		
		Condicao volatilidadeBaixa = new Condicao(Atributo.VOLATILIDADE, Operador.MENOR, 0.3);
		Condicao volatilidadeAlta = new Condicao(Atributo.VOLATILIDADE, Operador.MAIOR_IGUAL, 0.3);
		
		Stop stop = new Stop(Atributo.PERCENTUAL_OPERACAO, Operador.MAIOR_IGUAL, parametros[0]);
		
		Compra compra2xCallATM = new Compra(2, new Call(acao, 0), volatilidadeBaixa);
		Venda vendaCallITM4 = new Venda(new Call(acao, -4), volatilidadeBaixa);
		
		Compra compraCallATM = new Compra(new Call(acao, 0), volatilidadeAlta);
		Venda vendaCallOTM4 = new Venda(2, new Call(acao, 4), volatilidadeAlta);
		Compra compraCallOTM8 = new Compra(new Call(acao, 8), volatilidadeAlta);
		
		compra2xCallATM.setDiasParaVencimento(parametros[1]);
		vendaCallITM4.setDiasParaVencimento(parametros[1]);
		
		Simulacao simulacao = new Simulacao(
				compra2xCallATM ,
				vendaCallITM4,
				compraCallATM,
				vendaCallOTM4,
				compraCallOTM8
				);
		if (parametros[0] < 100)
			simulacao.setStop(stop);
		simulacao.setTipoCalculoValorInvestido(TipoCalculoValorInvestido.PRECO_ACAO);
		simulacao.setPercValorInvestido(30);
		simulacoes.add(simulacao);
	}
	
	public static void main(String[] args) {
		Lhama2 estrategia = new Lhama2();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 100, 7);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Lhama2("+acao.getNome()+")" + separador +
		anoInicial + separador + anoFinal + separador + "Stop(" + parametros[0] + "%)"
		+ separador + parametros[1] + "diasPFechamento";
	}
}
