/*
 * Created on 23/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.data;

import java.sql.SQLException;
import java.util.List;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.SATAUtil;

/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValuesMeta {

	private double[] closes = null;
	private double[] high = null;
	private double[] low = null;
	private double[] volume = null;
	private double[] open = null;
	private String[] data = null;
	
	private List<CotacaoAtivoTO> listaCotacoes;
	
	public ValuesMeta(String acao) {
		try {
			acao = acao.replace("D.txt", "");
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
			List<CotacaoAtivoTO> listaCotacoes;
			listaCotacoes = caDAO.getCotacoesDoAtivo(acao);

			closes = new double[listaCotacoes.size()];
			high = new double[listaCotacoes.size()];
			low = new double[listaCotacoes.size()];
			open = new double[listaCotacoes.size()];
			volume = new double[listaCotacoes.size()];
			data = new String[listaCotacoes.size()];

			CotacaoAtivoTO caTO;
			int i;
			for (i=0; i < listaCotacoes.size(); i++)
			{
				caTO = listaCotacoes.get(i);
				closes[i] = Double.parseDouble(caTO.getFechamento());
				high[i] = Double.parseDouble(caTO.getMaxima());
				low[i] = Double.parseDouble(caTO.getMinima());
				open[i] = Double.parseDouble(caTO.getAbertura());
				data[i] = SATAUtil.getDataSemFormato(caTO.getPeriodo()) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ValuesMeta(String acao, String dois) {
		try {
			ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
	//		listaCotacoes = caDAO.getCotacoesDoAtivo(acao, "2011");
			listaCotacoes = caDAO.getCotacoesDoAtivo(acao);
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
	}

	
	public List<CotacaoAtivoTO> getValores(){
		return listaCotacoes;
	}
	
//	public ValuesMeta(String acao){
//		
//		FileInputStream file;
//
//		try {
//			System.out.println();
//			file = new FileInputStream("C:\\PRIVADO\\flavio\\bolsa\\BaseBovespa3\\" + acao);
//			//file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\atual\\" + acao);
//			//file = new FileInputStream(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao);
//			
//			DataInputStream myInput = new DataInputStream(file);
//			
//	        String thisLine = "";
//	        int i=0;
//	        myInput.readLine(); // retira primeira linha
//	        while ((thisLine = myInput.readLine()) != null){         	
//	        	i++;
//	        }
//	        myInput.close();
//	        file.close();
//	        
//	        closes = new double[i];
//	        high = new double[i];
//	        low = new double[i];
//	        open = new double[i];
//	        volume = new double[i];
//	        data = new String[i];
//	        
//	        
//	        file = new FileInputStream("C:\\privado\\flavio\\bolsa\\BaseBovespa3\\" + acao);
//	       // file = new FileInputStream("H:\\flavio\\bolsa\\bovespa\\historico\\atual\\" + acao);
//	        //file = new FileInputStream(System.getProperty("user.dir") + "\\BaseBovespa3\\" + acao);
//	        DataInputStream myInput2 = new DataInputStream(file );
//	       
//	        
//	        myInput2.readLine();
//	        i=0;
//	        while ((thisLine = myInput2.readLine()) != null){ 
//	        	
//	        	/*Acão*/
//	        	thisLine.substring(0,thisLine.indexOf(","));
//	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//	        	
//	        	/*periodo*/
//	        	thisLine.substring(0,thisLine.indexOf(","));
//	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//	        	
//	        	/*data*/	        	
//	        	data[i] = thisLine.substring(0,thisLine.indexOf(","));
//	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//	        	
//	        	/*time*/
//	        	thisLine.substring(0,thisLine.indexOf(","));
//	        	thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//	        	
//	        	/*abertura*/
//			    open[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
//			    thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//			    
//			    /*maxima*/
//				high[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
//				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//				
//				/*minima*/
//				low[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
//				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//				
//				/*fechamento*/
//				closes[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
//				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//				
//				/*volume*/
//				//[i] = Double.parseDouble(thisLine.substring(0,thisLine.indexOf(",")));
//				thisLine = thisLine.substring(thisLine.indexOf(",") + 1);
//				
//				//System.out.println(closes[i-1]);
//				i++;
//	        }
//        
//	        myInput2.close();
//	        file.close();
//
//	            
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
	
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
	
	/**
	 * @return Returns the volume.
	 */
	public double[] getVolume() {
		return volume;
	}
	/**
	 * @param volume The volume to set.
	 */
	public void setVolume(double[] volume) {
		this.volume = volume;
	}
	/**
	 * @param closes The closes to set.
	 */
	public void setCloses(double[] closes) {
		this.closes = closes;
	}
	/**
	 * @param data The data to set.
	 */
	public void setData(String[] data) {
		this.data = data;
	}
	/**
	 * @param high The high to set.
	 */
	public void setHigh(double[] high) {
		this.high = high;
	}
	/**
	 * @param low The low to set.
	 */
	public void setLow(double[] low) {
		this.low = low;
	}
	/**
	 * @return Returns the high.
	 */
	public double[] getHigh() {
		return high;
	}
	/**
	 * @return Returns the closes.
	 */
	public double[] getCloses() {
		return closes;
	}
	/**
	 * @return Returns the data.
	 */
	public String[] getData() {
		return data;
	}
	/**
	 * @return Returns the low.
	 */
	public double[] getLow() {
		return low;
	}
	
	
	
}
