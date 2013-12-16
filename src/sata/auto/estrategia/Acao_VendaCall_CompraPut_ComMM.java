package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.auto.simulacao.Simulacao;

public class Acao_VendaCall_CompraPut_ComMM extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer ordemCallMercBaixa = parametros[param++];
		Integer ordemPutMercBaixa = parametros[param++];
		Integer ordemCallMercAlta = parametros[param++];
		Integer ordemPutMercAlta = parametros[param++];
		Integer posicaoMediaMovel = parametros[param++];

		Condicao mercadoEmBaixa = new Condicao(Atributo.MEDIA_MOVEL, Operador.MENOR, posicaoMediaMovel);
		Condicao mercadoEmAlta =  new Condicao(Atributo.MEDIA_MOVEL, Operador.MAIOR_IGUAL, posicaoMediaMovel);
		
		simulacoes.add(new Simulacao(new Compra(acao),
									 new Venda (new Call(acao, ordemCallMercBaixa), mercadoEmBaixa),
				         			 new Compra(new Put (acao, ordemPutMercBaixa),  mercadoEmBaixa),
				         		 	 new Venda (new Call(acao, ordemCallMercAlta),  mercadoEmAlta),
				         			 new Compra(new Put (acao, ordemPutMercAlta),   mercadoEmAlta)));
	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraPut_ComMM estrategia = new Acao_VendaCall_CompraPut_ComMM();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, -4, 0, 4, -1, 50);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Acao("+acao.getNome()+")" + separador +
			"CompPut("+parametros[0]+")" + separador + 
			"VendCall("+parametros[1]+")" + separador + 
			"CompPut("+parametros[2]+")" + separador +
			"VendCall("+parametros[3]+")" + separador +
			"MédiaMóvel("+parametros[4]+")" + separador +
			anoInicial + separador + anoFinal;
	}
}
