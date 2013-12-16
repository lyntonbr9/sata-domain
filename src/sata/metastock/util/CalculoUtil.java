/*
 * Created on 24/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.IConstants;
import sata.metastock.indices.MediaMovel;
import sata.metastock.indices.MediaMovelSimples;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CalculoUtil implements IConstants {
	
	public static double[] calculaMediaMovelSimples(double[] listaDeFechamentos, int periodo){
		MediaMovelSimples mms = new MediaMovelSimples(listaDeFechamentos, periodo);
		return mms.calculaMediaMovelSimples();
	}
	
	public static double[] calculaMediaMovel(double[] listaDeFechamentos, int periodo){
		MediaMovel mms = new MediaMovel(listaDeFechamentos, periodo);
		return mms.getMediaMovel();
	}
	
	public static double getPerdaMedia(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double perda = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					perda += new BigDecimal(closes[i] - closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		return new BigDecimal(perda).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static double getGanhoMedio(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		double ganho = 0.0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					ganho += new BigDecimal(closes[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).add(new BigDecimal(-1)).doubleValue();
					comprasPositivas++;
				}
				
			}
		}
		
		if(comprasPositivas==0){
			return 0.0;
		}
		
		return new BigDecimal(ganho).divide(new BigDecimal(comprasPositivas),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();

	}
	
	public static int getNumeroNegativas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasNegativas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] < closes[i]){
					comprasNegativas++;
				}
				
			}
		}
				
		return comprasNegativas;
	} 
	
	public static int getNumeroPositivas(double closes[],boolean[] ptosCompra,int days){
		
		
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
				
		return comprasPositivas;
	} 
	
	
	
	
	public static double getEfecienciaUsandoMaximaAcimaDe(double ganhoMinimo,double high[],double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && high[i+days] > closes[i]){
					double alta = new BigDecimal(high[i+days]).divide(new BigDecimal(closes[i]),BigDecimal.ROUND_HALF_EVEN,6).doubleValue() -1;
					if(alta>=ganhoMinimo){
						comprasPositivas++;
					}
					
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	
	public static double getEfecienciaUsandoMaxima(double high[],double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && high[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getEfeciencia12(double closes[],boolean[] ptosCompra){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+2) < (ptosCompra.length-1) && closes[i+1] > closes[i]){
					if(closes[i+2] > closes[i]){
						comprasPositivas++;
					}
					
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getEfeciencia(double closes[],boolean[] ptosCompra,int days){
		
		int compras = 0;
		int comprasPositivas = 0;
		for(int i=0;i<ptosCompra.length;i++){
			if(ptosCompra[i]){
				compras++;
				if((i+days) < (ptosCompra.length-1) && closes[i+days] > closes[i]){
					comprasPositivas++;
				}
				
			}
		}
		
		double result = new BigDecimal(comprasPositivas).divide(new BigDecimal(compras),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	} 
	
	public static double getTaxaAltasAcoes(double closes[],int days){
		
		int altas = 0;
		for(int i=0;i<closes.length;i++){
			    
				if((i+days) < (closes.length-1) && closes[i+days] > closes[i]){
					altas++;
				}
				
			
		}
		
		double result = new BigDecimal(altas).divide(new BigDecimal(closes.length),BigDecimal.ROUND_HALF_EVEN,6).doubleValue();
		
		return result;
	}
	
	/**
	 * Calcula a volatilidade anual e mensal das cotacoes do ativo
	 * @param codigoAtivo Codigo do Ativo.
	 * @param ano Ano do Ativo.
	 * @return Retorna a lista com a volatilidade atualizada.
	 * @throws SQLException 
	 */
	public static List<CotacaoAtivoTO> calculaVolatilidade(String codigoAtivo, String ano) throws SQLException
	{
		PropertyConfigurator.configure("log4j.properties");
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		//pega as cotacoes do ano passado e retrasado do ativo
		int anoPassado = Integer.parseInt(ano) - 1;
		int anoRetrasado = Integer.parseInt(ano) - 2;
		List<CotacaoAtivoTO> listaDasCotacoesDoAno = caDAO.getCotacoesDoAtivo(codigoAtivo, Integer.parseInt(ano));
		List<CotacaoAtivoTO> listaDasCotacoesAnoPassado = caDAO.getCotacoesDoAtivo(codigoAtivo, anoPassado);
		List<CotacaoAtivoTO> listaDasCotacoesAnoRetrasado = caDAO.getCotacoesDoAtivo(codigoAtivo, anoRetrasado);
		
		//cria uma lista com todas as cotacoes para calculo
		List<CotacaoAtivoTO> listaDasCotacoesParaCalculo = new ArrayList<CotacaoAtivoTO>();
		if(listaDasCotacoesAnoPassado.size() > 0 && listaDasCotacoesAnoRetrasado.size() > 0)
		{
			//se a lista das cotacoes do ano passado for menor que o necessario
			//tem que preencher com as cotacoes do ano retrasado
			if (listaDasCotacoesAnoPassado.size() < QTD_DIAS_UTEIS_ANO)
			{
				if(listaDasCotacoesAnoRetrasado.size() > 0)
				{
					int qtdDiasQueFalta = QTD_DIAS_UTEIS_ANO - listaDasCotacoesAnoPassado.size();
					for(int i=listaDasCotacoesAnoRetrasado.size() - qtdDiasQueFalta; i < listaDasCotacoesAnoRetrasado.size(); i++)
						listaDasCotacoesParaCalculo.add(listaDasCotacoesAnoRetrasado.get(i));
				}
			}
			//termina de preencher com as cotacoes do ano passado
			for(int i=0; i<listaDasCotacoesAnoPassado.size();i++)
				listaDasCotacoesParaCalculo.add(listaDasCotacoesAnoPassado.get(i));
			
			//termina de preencher com as cotacoes do ano corrente
			for(int i=0; i<listaDasCotacoesDoAno.size();i++)
				listaDasCotacoesParaCalculo.add(listaDasCotacoesDoAno.get(i));
		}
		
		double volatilidadeAnual=0.0;
		double volatilidadeMensal=0.0;
		//calcula a volatilidade anual e mensal historica
		if(listaDasCotacoesParaCalculo.size() > 0) //se tiver cotacoes para calculo
		{
			//calcula a volatilidade anual
			for(int i=0; i<listaDasCotacoesDoAno.size(); i++)
			{
				List<CotacaoAtivoTO> listaParaCalculoVolatilidadeAnual = new ArrayList<CotacaoAtivoTO>();
				for(int j = i; j < (QTD_DIAS_UTEIS_ANO + i); j++)
					listaParaCalculoVolatilidadeAnual.add(listaDasCotacoesParaCalculo.get(j));
				volatilidadeAnual = getVolatilidade(listaParaCalculoVolatilidadeAnual);
				listaDasCotacoesDoAno.get(i).setVolatilidadeAnual(volatilidadeAnual);	
			}
			
			//calcula a volatilidade mensal
			for(int i=0; i<listaDasCotacoesDoAno.size(); i++)
			{
				List<CotacaoAtivoTO> listaParaCalculoVolatilidadeMensal = new ArrayList<CotacaoAtivoTO>();
				for(int j = (listaDasCotacoesParaCalculo.size() - listaDasCotacoesDoAno.size() - QTD_DIAS_UTEIS_MES + i); j < (listaDasCotacoesParaCalculo.size() - listaDasCotacoesDoAno.size() + i); j++)
					listaParaCalculoVolatilidadeMensal.add(listaDasCotacoesParaCalculo.get(j));
				
				volatilidadeMensal = getVolatilidade(listaParaCalculoVolatilidadeMensal);
				listaDasCotacoesDoAno.get(i).setVolatilidadeMensal(volatilidadeMensal);	
			}
		}
		else{ //se não tiver cotacoes para calculo
			
			//entao seta a volatilidade anual como 0.0 para todos
			for(CotacaoAtivoTO caTO : listaDasCotacoesDoAno)
				caTO.setVolatilidadeAnual(0.0);
			
			//as primeiras 21 cotacoes ficam com a volatilidade mensal 0.0
			for (int i=0; i < QTD_DIAS_UTEIS_MES ; i++)
				listaDasCotacoesDoAno.get(i).setVolatilidadeMensal(0.0);
			
			//para as demais a volatilidade mensal tem que ser calculada
			for (int i = QTD_DIAS_UTEIS_MES; i < listaDasCotacoesDoAno.size(); i++)
			{
				List<CotacaoAtivoTO> listaParaCalculoVolatilidadeMensal = new ArrayList<CotacaoAtivoTO>();
				//pega as 21 cotacoes anteriores ao dia
				for(int j=(i - QTD_DIAS_UTEIS_MES); j < i; j++)
				{
					listaParaCalculoVolatilidadeMensal.add(listaDasCotacoesDoAno.get(j));
				}
				volatilidadeMensal = getVolatilidade(listaParaCalculoVolatilidadeMensal);
				listaDasCotacoesDoAno.get(i).setVolatilidadeMensal(volatilidadeMensal);
			}
		}
		
		return listaDasCotacoesDoAno;

	}
	
	   
    /**
     * Retorna a volatilidade das cotacoes da acao.
     * @param listaCotacoesAtivo Lista com as cotacoes para calculo da volatilidade.
     * @return Retorna a volatilidade da acao a partir da lista de cotacoes.
     */
    public static double getVolatilidade(List<CotacaoAtivoTO> listaCotacoesAtivo)
    {
    	
    	int numTotalDaAmostra = listaCotacoesAtivo.size();
    	double mediaVariacoesPrecos = 0;
    	double desvioPadrao = 0;
    	double variacoesPrecos[] = new double[numTotalDaAmostra];
    	double precoFechamentoAnterior = 0;
    	double precoFechamento = 0;
    	double somatorioVariacaoDePrecos = 0;
    	double somatorioVariacaoDePrecosDesvioPadrao = 0;
    	for(int i=1; i < numTotalDaAmostra; i++)
    	{	
    		precoFechamentoAnterior = Double.parseDouble(listaCotacoesAtivo.get(i-1).getFechamento());
    		precoFechamento = Double.parseDouble(listaCotacoesAtivo.get(i).getFechamento());
    		variacoesPrecos[i] = Math.log(precoFechamento/precoFechamentoAnterior);
    	}
    	
    	for(int i=1; i < numTotalDaAmostra; i++)
    	{	
    		somatorioVariacaoDePrecos = somatorioVariacaoDePrecos + variacoesPrecos[i];
    	}
    	
    	mediaVariacoesPrecos = somatorioVariacaoDePrecos / numTotalDaAmostra;
    	
    	for(int i=1; i < numTotalDaAmostra; i++)
    	{	
    		somatorioVariacaoDePrecosDesvioPadrao = somatorioVariacaoDePrecosDesvioPadrao + Math.pow((mediaVariacoesPrecos - variacoesPrecos[i]), 2);
    	}
    
    	desvioPadrao = Math.sqrt(somatorioVariacaoDePrecosDesvioPadrao / (numTotalDaAmostra - 1));
    	//System.out.println("desvioPadrao = " + desvioPadrao);
    	
    	//multiplica pela raiz quadrada da quantidade de dias
    	desvioPadrao = desvioPadrao * Math.sqrt(listaCotacoesAtivo.size());
    	
    	return desvioPadrao;    	
    }
    
    /**
     * 
     * @param listaDasCotacoesAnoPassado Lista com as cotacoes do ano passado.
     * @param listaDasCotacoesAnoRetrasado Lista com as cotacoes do ano retrasado.
     * @return Retorna a volatilidade anual da acao (considerando o ano passado e utilizando o ano retrasado para completar 252 cotacoes).
     */
    @Deprecated
    public static double getVolatilidadeAnualAcao(List<CotacaoAtivoTO> listaDasCotacoesAnoPassado, List<CotacaoAtivoTO> listaDasCotacoesAnoRetrasado)
    {
		List<CotacaoAtivoTO> listaDasCotacoesParaCalculoDaVolatilidade = new ArrayList<CotacaoAtivoTO>(); 

		int qtdDiasFaltamParaCalculoVolatilidade = QTD_DIAS_UTEIS_ANO - listaDasCotacoesAnoPassado.size();
		
		//pega do ano RETRASADO as cotacoes que faltam para o calculo da volatilidade
		//no ano tem-se 252 dias uteis entao deve-se conseguir 252 cotacoes
		if (listaDasCotacoesAnoRetrasado.size() > 0)
		{
			for(int i = listaDasCotacoesAnoRetrasado.size() - qtdDiasFaltamParaCalculoVolatilidade; i < listaDasCotacoesAnoRetrasado.size(); i++)
			{
				CotacaoAtivoTO caTO = listaDasCotacoesAnoRetrasado.get(i);
				listaDasCotacoesParaCalculoDaVolatilidade.add(caTO);
			}			
		}
		
		//pega o restante das cotacoes do ano passado
		for(int i = 0; i < listaDasCotacoesAnoPassado.size(); i++)
		{
			CotacaoAtivoTO caTO = listaDasCotacoesAnoPassado.get(i);
			listaDasCotacoesParaCalculoDaVolatilidade.add(caTO);
		}
        return CalculoUtil.getVolatilidade(listaDasCotacoesParaCalculoDaVolatilidade);
    }
    
    public static double getPctgem(double valor, double valorDeReferencia)
    {
    	return (valor*100)/valorDeReferencia;
    }	
	
    @Deprecated
    public static double getVolatilidade()
    {
    	return 0.27; //27%
    }
    
    public static double getProbabilidadeHistorica(List<CotacaoAtivoTO> cotacoes, int qtdDias, double valorInicio, double valorFim, boolean ehMaximo){
    	
    	double diferenca = valorFim - valorInicio;
    	double pctDiferenca = Math.abs(diferenca / valorInicio);
    	if(ehMaximo){
    		System.out.println("pctDifMax: " + BigDecimal.valueOf(pctDiferenca * 100) + " %");
    	}else{
    		System.out.println("pctDifMin: " + BigDecimal.valueOf(pctDiferenca * 100) + " %");
    	}
    	
    	double max = 0.0;
    	double min = 0.0;
    	double pctDiferencaMaximo = 0.0;
    	int qtdMaximosUltrapassaram = 0;
    	double pctDiferencaMinimo = 0.0;
    	int qtdMinimosUltrapassaram = 0;
    	double valorAcao = 0.0;
    	
    	for(int i = 0; i < cotacoes.size() - qtdDias; i++){
    		valorAcao = cotacoes.get(i).getValorFechamento().doubleValue();
    		max = getMaximo(cotacoes, i, i + qtdDias);
    		min = getMinimo(cotacoes, i, i + qtdDias);
    		pctDiferencaMaximo = Math.abs(max - valorAcao) / valorAcao;
    		pctDiferencaMinimo = Math.abs(min - valorAcao) / valorAcao;
    		if(pctDiferencaMaximo >= pctDiferenca){
    			qtdMaximosUltrapassaram++;
    		}
    		if(pctDiferencaMinimo >= pctDiferenca){
    			qtdMinimosUltrapassaram++;
    		}
    	}
    	
    	if(ehMaximo){
    		return ((double)qtdMaximosUltrapassaram / cotacoes.size()) * 100;
    	}else{
    		return ((double)qtdMinimosUltrapassaram / cotacoes.size()) * 100;
    	}
    }
    
    private static double getMaximo(List<CotacaoAtivoTO> cotacoes, int indiceInicio, int indiceFim){
    	double valorAcaoMax = 0.0;
    	double valorAcaoCorrente = 0.0;
    	for(int i = indiceInicio; i <= indiceFim; i++){
    		valorAcaoCorrente = cotacoes.get(i).getValorFechamento().doubleValue();
    		if(valorAcaoCorrente >= valorAcaoMax){
    			valorAcaoMax = valorAcaoCorrente;
    		}
    	}
    	return valorAcaoMax;
    }
    
    private static double getMinimo(List<CotacaoAtivoTO> cotacoes, int indiceInicio, int indiceFim){
    	double valorAcaoMin = Double.MAX_VALUE;
    	double valorAcaoCorrente = 0.0;
    	for(int i = indiceInicio; i <= indiceFim; i++){
    		valorAcaoCorrente = cotacoes.get(i).getValorFechamento().doubleValue();
    		if(valorAcaoCorrente <= valorAcaoMin){
    			valorAcaoMin = valorAcaoCorrente;
    		}
    	}
    	return valorAcaoMin;
    }
    
	public static void main(String[] args) throws SQLException {
		
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		String codigoAtivo = "PETR4";
		String dataInicial="2000-01-01";
		String dataFinal = "2011-01-01";
		int qtdDias = 25;
		double valorFimMin = 20.05;
		double valorInicio = 21.59;
		double valorFimMax = 22.66;
		List<CotacaoAtivoTO> cotacoesDoAno = caDAO.getCotacoesDoAtivo(codigoAtivo, dataInicial, dataFinal);
		double resultadoProbMax = getProbabilidadeHistorica(cotacoesDoAno, qtdDias, valorInicio, valorFimMax, true);
		double resultadoProbMin = getProbabilidadeHistorica(cotacoesDoAno, qtdDias, valorInicio, valorFimMin, false);
		System.out.println("resultadoProbMax: " + BigDecimal.valueOf(resultadoProbMax) + " %");
		System.out.println("resultadoProbMin: " + BigDecimal.valueOf(resultadoProbMin) + " %");
	}
	
}
