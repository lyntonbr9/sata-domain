package sata.metastock.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;

import sata.metastock.data.ConverteArquivo;
import sata.metastock.util.AcaoBovespa;

public class SimulacaoPercentual {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SimulacaoPercentual.simula("COTAHIST_A1998");
		SimulacaoPercentual.simula("COTAHIST_A1999");
		SimulacaoPercentual.simula("COTAHIST_A2000");
		SimulacaoPercentual.simula("COTAHIST_A2001");
		SimulacaoPercentual.simula("COTAHIST_A2002");
		SimulacaoPercentual.simula("COTAHIST_A2003");
		SimulacaoPercentual.simula("COTAHIST_A2004");
		SimulacaoPercentual.simula("COTAHIST_A2005");
		SimulacaoPercentual.simula("COTAHIST_A2006");
		SimulacaoPercentual.simula("COTAHIST_A2007");
		SimulacaoPercentual.simula("COTAHIST_A2008");
		SimulacaoPercentual.simula("COTAHIST_A2009");
	}
	
	public static void simula(String arq){
		
		ArrayList cotacoes = ConverteArquivo.convete(arq);
		
		//System.out.println(cotacoes.size());
		int perdeu = 0;
		for(int i=0;i<cotacoes.size();i++){
			
			BigDecimal valor =  new BigDecimal(Integer.parseInt(((AcaoBovespa)cotacoes.get(i)).getFechamento()));
			
			boolean maior = false;
			for(int j=i+1;j<i+21 && j<cotacoes.size();j++){
				BigDecimal valor2 =  new BigDecimal(Integer.parseInt(((AcaoBovespa)cotacoes.get(j)).getMinima()));
				
				
				if(valor.intValue()>valor2.intValue()){
					BigDecimal perc = (valor.subtract(valor2)).divide(valor,BigDecimal.ROUND_HALF_EVEN,6);
					
					if(perc.doubleValue()>0.11 && !maior){
						maior = true;
						perdeu++;
						//System.out.println(perc.doubleValue());
						//System.out.println("perdeu");
					}
				}								
			}
						
			//System.out.println(perdeu);
		}

		System.out.println(arq + " " + perdeu + " " +cotacoes.size());
//		System.out.println(arq + " " + (new BigDecimal(perdeu)).divide(new BigDecimal(cotacoes.size())).doubleValue());
	}
	
}
