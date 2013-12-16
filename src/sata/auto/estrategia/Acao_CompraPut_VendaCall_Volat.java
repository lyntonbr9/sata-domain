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

public class Acao_CompraPut_VendaCall_Volat extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer ordemPut = parametros[param++];
		Integer ordemCall = parametros[param++];
		Double volatilidade = (double) parametros[param++].intValue() / 100;
		
		Put put = new Put(acao, ordemPut);
		Call call = new Call(acao, ordemCall);
		
		Condicao volatilidadeBaixa = new Condicao(Atributo.VOLATILIDADE, Operador.MENOR, volatilidade);
		Condicao volatilidadeAlta = new Condicao(Atributo.VOLATILIDADE, Operador.MAIOR_IGUAL, volatilidade);
		
		simulacoes.add(new Simulacao(
				new Compra(acao, 1),
				new Compra(put, 1),
				new Venda(call, 2, volatilidadeBaixa),
				new Venda(call, 1, volatilidadeAlta)));
	}
	
	public static void main(String[] args) {
		Acao_CompraPut_VendaCall_Volat estrategia = new Acao_CompraPut_VendaCall_Volat();
		estrategia.acao = AcaoConteiner.get("PETR4");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 0, 4, 30);
	}

	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Acao("+acao.getNome()+")" + separador +
			"CompraPut("+parametros[0]+")"+separador+
			"VendaCall("+parametros[1]+")"+separador+
			"2MesesVolat("+parametros[2]+"%)" + separador +
			anoInicial + separador + anoFinal;
	}
}
