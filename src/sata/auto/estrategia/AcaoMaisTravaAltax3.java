package sata.auto.estrategia;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class AcaoMaisTravaAltax3 extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		anoInicial = 2000;
		anoFinal = 2011;
		tipoCalculoValorInvestido = TipoCalculoValorInvestido.CUSTO_MONTAGEM_IGNORANDO_PRIMEIRO_MES;
		Acao acao = AcaoConteiner.get("PETR4");
		
//		simulacoes.add(new Simulacao(new Compra(1, acao, 2, 2)));
//		simulacoes.add(new Simulacao(new Venda (3, new Call(acao, -2), 2, 2), 
//									 new Compra(3, new Call(acao, 0), 2, 2)));
		simulacoes.add(new Simulacao(new Compra(1, acao, 2)));
		simulacoes.add(new Simulacao(new Venda (3, new Call(acao, -2), 2), 
									 new Compra(3, new Call(acao, 0), 2)));
	}
	
	public static void main(String[] args) {
		new AcaoMaisTravaAltax3().executa(TipoRelatorio.OPERACOES);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return null;
	}
}
