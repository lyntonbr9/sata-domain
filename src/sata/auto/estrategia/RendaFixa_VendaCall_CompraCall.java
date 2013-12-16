package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class RendaFixa_VendaCall_CompraCall extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer ordemCallVenda = parametros[param++];
		Integer ordemCallCompra = parametros[param++];
		
		simulacoes.add(new Simulacao(
				new Venda (new RendaFixa(acao), false),
				new Venda (new Call(acao, ordemCallVenda)),
				new Compra(new Call(acao, ordemCallCompra))));
	}
	
	public static void main(String[] args) {
		RendaFixa_VendaCall_CompraCall estrategia = new RendaFixa_VendaCall_CompraCall();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 4, -1);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "RendaFixa" + separador +
			"VendCall("+parametros[0]+")" + separador +
			"CompCall("+parametros[1]+")" + separador +
			acao.getNome() + separador + anoInicial + separador + anoFinal;
	}
}
