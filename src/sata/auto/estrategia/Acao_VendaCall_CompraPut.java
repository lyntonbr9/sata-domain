package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class Acao_VendaCall_CompraPut extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer qtdLotesCall = parametros[param++];
		Integer ordemCall = parametros[param++];
		Integer qtdMesesCall = parametros[param++];
		Integer ordemPut = parametros[param++];
		
		Simulacao simulacao = new Simulacao(new Compra(acao),
											new Venda (qtdLotesCall, new Call(acao, ordemCall), qtdMesesCall),
											new Compra(new Put (acao, ordemPut)));
		
		simulacoes.add(simulacao);	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraPut estrategia = new Acao_VendaCall_CompraPut();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 1, 4, 1, 2);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Acao("+acao.getNome()+")" + separador +
		+parametros[0]+"xVendCall("+parametros[1]+","+parametros[2]+")" + separador + 
		"CompPut("+parametros[3]+")"+ separador +
		anoInicial + separador + anoFinal;
	}
}
