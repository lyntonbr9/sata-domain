package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class SomenteAcao extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		simulacoes.add(new Simulacao(new Compra(acao)));
	}
	
	public static void main(String[] args) {
		SomenteAcao estrategia = new SomenteAcao();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Acao("+acao.getNome()+")" + separador +
		anoInicial + separador + anoFinal;
	}
}
