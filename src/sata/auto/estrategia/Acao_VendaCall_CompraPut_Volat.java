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

public class Acao_VendaCall_CompraPut_Volat extends Estrategia {
	
	@Override
	public void prepara(Integer... parametros) {
		int param = 0;
		Integer qtdLotesCallVolBaixa = parametros[param++];
		Integer ordemCallVolBaixa = parametros[param++];
		Integer qtdMesesCallVolBaixa = parametros[param++];
		Integer ordemPutVolBaixa = parametros[param++];
		Integer qtdLotesCallVolAlta = parametros[param++];
		Integer ordemCallVolAlta = parametros[param++];
		Integer ordemPutVolAlta = parametros[param++];
		Double volatilidade = (double) parametros[param++]/100;
		
		Condicao volBaixa = new Condicao(Atributo.VOLATILIDADE, Operador.MENOR, volatilidade);
		Condicao volAlta  = new Condicao(Atributo.VOLATILIDADE, Operador.MAIOR_IGUAL, volatilidade);
		
		Simulacao simulacao = new Simulacao(new Compra(acao),
											new Venda (qtdLotesCallVolBaixa, new Call(acao, ordemCallVolBaixa), qtdMesesCallVolBaixa, volBaixa),
											new Compra(new Put (acao, ordemPutVolBaixa), volBaixa),
											new Venda (qtdLotesCallVolAlta, new Call(acao, ordemCallVolAlta), volAlta),
											new Compra(new Put (acao, ordemPutVolAlta), volAlta));
		
		simulacoes.add(simulacao);	}
	
	public static void main(String[] args) {
		Acao_VendaCall_CompraPut_Volat estrategia = new Acao_VendaCall_CompraPut_Volat();
		estrategia.acao = AcaoConteiner.get("OGXP3");
		estrategia.anoInicial = 2000;
		estrategia.anoFinal = 2011;
		estrategia.executa(TipoRelatorio.COMPLETO, 1, 4, 2, 0, 2, 4, -1, 30);
	}
	
	@Override
	public String getTextoEstrategia(String separador, Integer... parametros) {
		return "Acao("+acao.getNome()+")" + separador +
		"Vend"+parametros[0]+"Call("+parametros[1]+","+parametros[2]+")" + separador +
		"CompPut("+parametros[3]+")" + separador +
		"Vend"+parametros[4]+"Call("+parametros[5]+")" + separador +
		"CompPut("+parametros[6]+")" + separador + 
		"Volat(" + parametros[7] + "%)" + separador +
		anoInicial + separador + anoFinal;
	}
}
