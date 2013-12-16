/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;

import java.math.BigDecimal;

import sata.metastock.data.Values;
import sata.metastock.data.ValuesMeta;
import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.IFR;
import sata.metastock.indices.MediaMovel;
import sata.metastock.indices.Stochastic;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Calculo {

	private  double[] close = null;
	private  double[] high = null;
	private  double[] low = null;
	private  double[] open = null;
	private String[] datas = null;

	
	
	
	public double[][] getAcoesMM(int mediaMovel,String acao, int agrupa){
	
	  int mmMen = 4;
	  int mmMai = 9;
	  

	  double[] mmMenor = null;
	  double[] mmMaior = null; 
	  
	
	  
	  	  
	  //LoadValues values = new LoadValues(acao + ".txt");
	  ValuesMeta values = new ValuesMeta(acao + "D.txt");
	  //System.out.println("\n\n\n" + acao);
	  close = values.getCloses();
	  datas = values.getData();
	  high = values.getHigh();
	  low = values.getLow();
	  open = values.getOpen();
		  
	  if(agrupa!=1){
	  	agrupa(agrupa);
	  }
	  
	  
	  MediaMovel mm = new MediaMovel(close,mediaMovel);
	  mmMenor = mm.getMediaMovel();
	  
	  double[][] retorno = new double[2][close.length];
		  
	  retorno[0] = close;
	  retorno[1] = mmMenor;
	
	  return retorno;
	
	}  
	
	public  void agrupa(int x){
		
		double[] closeAgrupado = new double[close.length/x + 1];
		double[] highAgrupado = new double[close.length/x + 1];
		double[] lowAgrupado = new double[close.length/x + 1];
		double[] openAgrupado = new double[close.length/x + 1];
		String[] datasAgrupado = new String[close.length/x + 1];

		int k = 0;
		for(int i=0;i<close.length;){
			
			highAgrupado[k] = 0;
			lowAgrupado[k] = Double.MAX_VALUE;
			
			for(int j=i;j<i+x && j<close.length;j++){
							
				datasAgrupado[k] = datas[i];
				openAgrupado[k] = open[i];
				closeAgrupado[k] = close[j];
				if(high[j]>highAgrupado[k]){
					highAgrupado[k] = high[j];
				}
				 
				if(low[j]<lowAgrupado[k]){
					lowAgrupado[k] = low[j];
				}
				
			}
			//System.out.println(openAgrupado[k] + " " + lowAgrupado[k] + " " + highAgrupado[k] + " " + closeAgrupado[k] + " " + datasAgrupado[k]);
			k++;
			i=i+x;
		}
		
		close = closeAgrupado;
		high = highAgrupado ;
		low = lowAgrupado ;
		open = openAgrupado ;
		datas = datasAgrupado ;

	
	}
	
	public static int getDiasAbaixoMM(int mediaMovel,String acao){
		  int mmMen = 4;
		  
		  
		  double[] close = null;
		  double[] mmMenor = null;
		  
		  ValuesMeta values = new ValuesMeta(acao + "D.txt");		  
		  close = values.getCloses();
	
		  MediaMovel mm = new MediaMovel(close,mediaMovel);
		  mmMenor = mm.getMediaMovel();
		  		  
		  int i = 1;
		  while(mmMenor[close.length-i]>=close[close.length-i]){	  	
		  	i++;
		  }
		  
		  
		  
		  return i-1;
		
	}
	
	public static double getValorDiaSeguinte(int mediaMovel,String acao){
		  int mmMen = 4;
		  int mmMai = 9;
		  
		  double[] close = null;
		  double[] high = null;
		  double[] low = null;
		  double[] mmMenor = null;
		  double[] mmMaior = null; 
		  
		  String[] datas = null;
		  	  
		  //LoadValues values = new LoadValues(acao + ".txt");
		  ValuesMeta values = new ValuesMeta(acao + "D.txt");
		  //System.out.println("\n\n\n" + acao);
		  close = values.getCloses();
		  datas = values.getData();
		  high = values.getHigh();
		  low = values.getLow();
			  

		  
		  MediaMovel mm = new MediaMovel(close,mediaMovel);
		  mmMenor = mm.getMediaMovel();
		  
		  
		  if(mmMenor[close.length-1]<=close[close.length-1]){
		  	return -1;
		  }
		
		  double soma = 0.0;
		  for(int i=close.length-1;i>close.length-mediaMovel;i--){
		  	soma += close[i];
		  }

		  
		  return ((new BigDecimal(soma).divide(new BigDecimal(mediaMovel-1),4,BigDecimal.ROUND_HALF_UP)).doubleValue());
		
	}
	
	
	
	/**
	 * @return Returns the close.
	 */
	public double[] getClose() {
		return close;
	}
	/**
	 * @param close The close to set.
	 */
	public void setClose(double[] close) {
		this.close = close;
	}
	/**
	 * @return Returns the high.
	 */
	public double[] getHigh() {
		return high;
	}
	/**
	 * @param high The high to set.
	 */
	public void setHigh(double[] high) {
		this.high = high;
	}
	/**
	 * @return Returns the low.
	 */
	public double[] getLow() {
		return low;
	}
	/**
	 * @param low The low to set.
	 */
	public void setLow(double[] low) {
		this.low = low;
	}
	/**
	 * @return Returns the open.
	 */
	public double[] getOpen() {
		return open;
	}
	/**
	 * @param open The open to set.
	 */
	public void setOpen(double[] open) {
		this.open = open;
	}
	public static void main(String args[]){
		
		Calculo c = new Calculo();
		c.getAcoesMM(4, "PETR4",1);
		
	}
	
	
	/**
	 * @return Returns the datas.
	 */
	public String[] getDatas() {
		return datas;
	}
	/**
	 * @param datas The datas to set.
	 */
	public void setDatas(String[] datas) {
		this.datas = datas;
	}
	/**
	 * Faz um relatorio de todas as ações que estão com ifr e estocástico 
	 * abaixo de 30 e com MM4 abaixo
	 * @param acoes
	 * @return
	 */
	public static String getRelatorio1(String[] acoes){
		
		String relatorio = "Estocástico abaixo de de 30:\n";
		String relatorio2 = "Estocástico e IFR abaixo de 30:\n";
		String relatorio3 = "IFR entre 30 e 50\n";
		String relatorio4 = "Media Móvel 4 Dias acima do preço de fechamento\n";
		
		for(int i=0;i<acoes.length;i++){
			Calculo c =new Calculo();
			
			System.out.println(acoes[i]);
	        double valores[][] = c.getAcoesMM(4,acoes[i],1);
	        
	        double mm4[] = valores[1]; 
	        double close[] = valores[0];
	        double open[] = c.getOpen();
	        double high[] = c.getHigh();
	        double low[] = c.getLow();
	         
	        IFR ifr = new IFR(valores[0],9,0);
	        double[] ifrs = ifr.getIFR();
      
	        Stochastic stc = new Stochastic(close, 10,3);
	        double[] stcs = null;
	       
	        try {
				stcs = stc.getStochastic();
			} catch (VetorMenor e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(stcs[stcs.length-1] < 30 /*&& ifrs[ifrs.length-1] < 30*/){
				double precoAlvo = c.getValorDiaSeguinte(4,acoes[i]);
				if(precoAlvo>0){
			        double variacaoAlvo = new BigDecimal(precoAlvo).divide(new BigDecimal(valores[0][valores[0].length-1]),BigDecimal.ROUND_HALF_DOWN,4).add(new BigDecimal(-1)).multiply(new BigDecimal(100)).doubleValue();
			        relatorio += acoes[i] + " IFR=" +  ifrs[ifrs.length-1] + " Estocástico=" +  stcs[stcs.length-1] +
						" Preço Alvo=" + precoAlvo + " variação Alvo=" + variacaoAlvo + "\n";
				}
			
			}
			
			if(stcs[stcs.length-1] < 30 && ifrs[ifrs.length-1] < 30){
				double precoAlvo = c.getValorDiaSeguinte(4,acoes[i]);
				if(precoAlvo>0){
			        double variacaoAlvo = new BigDecimal(precoAlvo).divide(new BigDecimal(valores[0][valores[0].length-1]),BigDecimal.ROUND_HALF_DOWN,4).add(new BigDecimal(-1)).multiply(new BigDecimal(100)).doubleValue();
			        relatorio2 += acoes[i] + " IFR=" +  ifrs[ifrs.length-1] + " Estocástico=" +  stcs[stcs.length-1] +
						" Preço Alvo=" + precoAlvo + " variação Alvo=" + variacaoAlvo + "\n";
				}
			
			}
			
			if(ifrs[ifrs.length-1] <= 50 && ifrs[ifrs.length-1] >= 30){
				double precoAlvo = c.getValorDiaSeguinte(4,acoes[i]);
				if(precoAlvo>0){
			        double variacaoAlvo = new BigDecimal(precoAlvo).divide(new BigDecimal(valores[0][valores[0].length-1]),BigDecimal.ROUND_HALF_DOWN,4).add(new BigDecimal(-1)).multiply(new BigDecimal(100)).doubleValue();
			        relatorio3 += acoes[i] + " IFR=" +  ifrs[ifrs.length-1] + " Estocástico=" +  stcs[stcs.length-1] +
						" Preço Alvo=" + precoAlvo + " variação Alvo=" + variacaoAlvo + "\n";
				}
			
			}
			
			double precoAlvo = c.getValorDiaSeguinte(4,acoes[i]);
			if(precoAlvo>0){
				int diasAbaixoMM4 = getDiasAbaixoMM(4,acoes[i]);
		        double variacaoAlvo = new BigDecimal(precoAlvo).divide(new BigDecimal(valores[0][valores[0].length-1]),BigDecimal.ROUND_HALF_DOWN,4).add(new BigDecimal(-1)).multiply(new BigDecimal(100)).doubleValue();
		        relatorio4 += acoes[i] + " Dias abaixo MM4: " + diasAbaixoMM4 +" IFR=" +  ifrs[ifrs.length-1] + " Estocástico=" +  stcs[stcs.length-1] +
					" Preço Alvo=" + precoAlvo + " variação Alvo=" + variacaoAlvo + "\n";
			}
			
			
		}     
		 relatorio += "\n\n" + relatorio2 + "\n\n" +relatorio3 + "\n\n" + relatorio4;
		 return relatorio;
	}
	
}
