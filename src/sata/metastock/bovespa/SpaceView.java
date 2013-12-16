/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.simulacao.SimulacaoAcaoAltaVarPoucoTempo;
import sata.domain.to.CotacaoAtivoTO;
import sata.metastock.data.ValuesMeta;
import sata.metastock.exceptions.VetorMenor;
import sata.metastock.indices.ATR;
import sata.metastock.indices.IFR;
import sata.metastock.indices.Stochastic;
import sata.metastock.simulacao.Capital;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class SpaceView extends JPanel {
	
	private String acao = "";
	private String acaoAnterior = "c";

	
	private int intervalo = 120;
	
	private boolean reload = false;
	private int diasCandle = 1;
	
	private int xDireitoClick = 0;
	private int yDireitoClick = 0;
	private boolean clicouDireito = false;
	
	private int x1Click = 0;
	private int y1Click = 0;
	private int x2Click = 0;
	private int y2Click = 0;
	private boolean x1Vazio = true;   
	
	//areas
	private int zoom = 1;
	private int areaY = 290/zoom;
	private int areaV= 50/zoom;
	private int areaI = 100/zoom;
	private int areaE = 100/zoom;
	private int areaM = 100/zoom;
	
	private int inicioGrafprincipal = areaY; 
	private int incioGrafVOL = areaY + areaV;
	private int inicioGrafIFR = areaY + areaV + areaI;
	private int inicioGrafEST = areaY + areaV + areaI + areaE ;
	private int inicioGrafATR = areaY + areaV + areaI + areaE + areaM;
	
	private int linhaIFR30 = areaY + areaV + areaI - new Double(areaI*0.3).intValue();
	private int linhaIFR70 = areaY + areaV + areaI - new Double(areaI*0.7).intValue();
	
	private int linhaEST30 = areaY + areaV + areaI + areaE - new Double(areaI*0.3).intValue();
	private int linhaEST70 = areaY + areaV + areaI + areaE - new Double(areaI*0.7).intValue();
	

	//tracejado
	private int compTracejado = 5;
	
	//candle
	private int espacoDia = 7;
	private int largCandle = 5;
	
	//zoom
	
	private int z = 3;
	
	//dias
	private int dias;	
	
	/*dias setados manualmente*/
	private int primeiro = 0;
	private int ultimo = intervalo-1;
	
	/*dias mostrados*/
	private int inicio = 0;
	private int fim = intervalo-1;
	

	private double fator = 1;
	private int escala = 1;
	private double menorPto  =  20000000;
	private double maiorPto  = 0;
	

    int pontoXInicial[] = null;
    int pontoXFinal[] = null;
     
    double valores[][] = null;
    double valores2[][] = null;
    double valores3[][] = null;
    
    double[] ifrs = null;
    
    double[] stcs = null;
    
    double[] atrEntry = null;
    double[] atrExit = null;
    
    //simulação
    
    private boolean comprado = false;
    private Capital capital = new Capital();
    
    private List<CotacaoAtivoTO> candles;
	
    //=================================== constructor
    public SpaceView () {
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.WHITE);
        this.addMouseListener(new MousePanel());   
        this.addMouseMotionListener(new MousePanel());
    }//end constructor
   
    //================================ paintComponent
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        System.out.println("DDDDDDDDDDDDDDDDDDDD");
        
        g.setColor(Color.BLACK);
       
        g.clearRect(0,0,1024,768);
        //setBackground(new Color(200,200,200,0));
 
        ValuesMeta values = new ValuesMeta(acao,"");
      //  ValuesMeta values = new ValuesMeta("USIM5","");
        candles = values.getValores();
        
        /*Simulação*/
		SimulacaoAcaoAltaVarPoucoTempo s = new SimulacaoAcaoAltaVarPoucoTempo();				
		s.getResultado(candles, null);
		ArrayList indicesIndicados = s.getIndicesIndicadosSimulacao();
        
        if(reload || !acao.equals(acaoAnterior)){
        	reload = false;
            acaoAnterior = acao;
            dias = candles.size();
            
         //   if(dias < intervalo) intervalo = dias;
            
        
             
        }
        
        if(yDireitoClick < inicioGrafprincipal && clicouDireito){
        	clicouDireito = false;
        	g.drawLine(0,yDireitoClick,2000,yDireitoClick);
        	g.drawLine(xDireitoClick,0,xDireitoClick,2000);
        }
        
        
        if(x1Vazio && x1Click!=0 && x2Click!=0 && y1Click!=0 && y2Click!=0){
        	//System.out.println("desenhando " + x1Click + " " + y1Click+ " " +x2Click+ " " + y2Click);
        	int y=getPontoY(2000);
        	g.drawLine(x1Click, y1Click,x2Click, y2Click);
        	g.drawLine(x1Click, y1Click,2000, y);
        	g.drawLine(2000, y,x2Click, y2Click);
        }
       
        
   //------------------------ ACERTO DO NUMERO DE CANDLES EXIBIDOS E QUAIS SERÃO EXIBIDOS -----------------------------     
        
        pontoXInicial = new int[candles.size()];
        pontoXFinal = new int[candles.size()];
        
        inicio = primeiro;
        fim = ultimo;
        
        if(inicio < 0){
        	inicio = 0;
        	fim = intervalo-1;
        	primeiro = 0;
        	ultimo = intervalo-1;
        } 
        
        if(fim > candles.size() -1 ){
        	
        	inicio = candles.size() - intervalo;
        	fim = candles.size() -1;        	
        	primeiro = candles.size() - intervalo;
        	ultimo = candles.size() -1;
        }
        
        if(candles.size()<intervalo){
        	
        	inicio = 0;
        	fim = candles.size() -1;        	
        	primeiro = 0;
        	ultimo = candles.size() -1;
            
        	
        }
      
        
      //------------------------FIM ACERTO DO NUMERO DE CANDLES EXIBIDOS E QUAIS SERÃO EXIBIDOS -----------------------------       
        
        
       //--------verifica maior e menor valor exibido no spaço dos candles -------------------- 
        menorPto  =  20000000;
        maiorPto  = 0;
        for(int i=inicio;i<fim;i++){
        	CotacaoAtivoTO candle = (CotacaoAtivoTO) candles.get(i);
        	if(i>0){
	        	if(Integer.parseInt(candle.getMinima())<menorPto){
	        		menorPto = Integer.parseInt(candle.getMinima());
	        	}
	        	if(Integer.parseInt(candle.getMaxima())>maiorPto){
	        		maiorPto = Integer.parseInt(candle.getMaxima());
	        	}
	        	
        	}	
        }
      
 //----------------------------------CALCULO DE FATOR E ESCALA----------------------------------------------------------
        fator = 1;
        if((maiorPto - menorPto)>areaY){
        	fator = new  Double(new BigDecimal(maiorPto-menorPto).divide(new BigDecimal(areaY),BigDecimal.ROUND_DOWN,6).doubleValue()).doubleValue();
        	maiorPto = new  Double(new BigDecimal(maiorPto).divide(new BigDecimal(fator),BigDecimal.ROUND_DOWN,6).doubleValue()).doubleValue();
        	menorPto = new  Double(new BigDecimal(menorPto).divide(new BigDecimal(fator),BigDecimal.ROUND_DOWN,6).doubleValue()).doubleValue();

        }
        
        escala = 1;
        if(maiorPto - menorPto>0){
            escala = new  Double(new BigDecimal(areaY).divide(new BigDecimal(maiorPto - menorPto),BigDecimal.ROUND_DOWN,6).doubleValue()).intValue();
        }
 
//-----------------------------------FIM FATOR E ESCALA---------------------------------------------------------------------        
        
        
//----------------------------------- DELIMITAÇÃO DA TELA ------------------------------------------------------------------
        
        g.drawLine(0,inicioGrafprincipal-areaY,5000,inicioGrafprincipal-areaY);
        
        g.drawLine(0,incioGrafVOL,5000,incioGrafVOL);
        
        g.drawLine(0,inicioGrafIFR,5000,inicioGrafIFR);
               
        g.drawLine(0,inicioGrafEST,5000,inicioGrafEST);
              
        g.drawLine(0,inicioGrafATR,5000,inicioGrafATR);
        
//-----------------------------------FIM DELIMITAÇÃO DA TELA ------------------------------------------------------------------
        
        boolean desenha = true;
        for(int k=0;k<300;k++){
        	if(desenha){
        		desenha = false;
                g.drawLine(k*compTracejado,linhaIFR30,k*compTracejado + compTracejado,linhaIFR30);
                
                g.drawLine(k*compTracejado,linhaIFR70,k*compTracejado + compTracejado,linhaIFR70);
                
                g.drawLine(k*compTracejado,linhaEST30,k*compTracejado + compTracejado,linhaEST30);
                
                g.drawLine(k*compTracejado,linhaEST70,k*compTracejado + compTracejado,linhaEST70);
        	}else{
        		desenha = true;
        	}
        }
                
        int x=0;
        	        	
//---------------------------DESENHA CANDLE ---------------------------------------------------------
        for(int i=inicio;i<=fim;i++){
	        	
        		CotacaoAtivoTO candle = (CotacaoAtivoTO) candles.get(i);
        	
	    		int ptoC =  new Double(escala*(Integer.parseInt(candle.getFechamento())/fator- menorPto)).intValue();
	        	int ptoO =  new Double(escala*((Integer.parseInt(candle.getAbertura()))/fator- menorPto)).intValue();
	        	int ptoL =  new Double(escala*((Integer.parseInt(candle.getMinima()))/fator- menorPto)).intValue();
	        	int ptoH =  new Double(escala*((Integer.parseInt(candle.getMaxima()))/fator- menorPto)).intValue(); 
        	
	        	if(ptoO<=ptoC){
	        		g.setColor(Color.BLUE);
	        		g.drawRect(x*espacoDia+espacoDia-largCandle/2,inicioGrafprincipal-ptoC-2,largCandle,ptoC - ptoO + 1);
	        		
	        		/*simulação*/
	        		if(isIndicado(indicesIndicados, i)){
	        			g.setColor(Color.BLACK);
		        		g.fillRect(x*espacoDia+espacoDia-largCandle/2,inicioGrafprincipal-ptoC-2,largCandle + 10,3);
		        	}

	        	}else{
	        		g.setColor(Color.RED);
	        		g.fillRect(x*espacoDia+espacoDia-largCandle/2,inicioGrafprincipal-ptoO-2,largCandle,ptoO - ptoC + 1);
	        		/*simulação*/
	        		if(isIndicado(indicesIndicados, i)){
	        			g.setColor(Color.BLACK);
		        		g.fillRect(x*espacoDia+espacoDia-largCandle/2,inicioGrafprincipal-ptoC-2,largCandle + 10,3);
		        	}
	        	}
	        		        	
	        	pontoXInicial[i] = x*espacoDia;
	        	pontoXFinal[i] = pontoXInicial[i] + espacoDia;
	        	
	        	g.drawLine(x*espacoDia+espacoDia, inicioGrafprincipal-ptoL,x*espacoDia+espacoDia, inicioGrafprincipal-ptoH);
	        	
	        	x++;

        }
        
//---------------------------FIM DESENHA CANDLE ---------------------------------------------------------

        
        if(x1Vazio && x1Click!=0 && x2Click!=0 && y1Click!=0 && y2Click!=0){
        
	        ArrayList pontosCanal = getPontos(x,espacoDia);
	    	g.setColor(Color.RED);
	    	System.out.println("Calculou pontos");
	    	for(int i=0;i<pontosCanal.size();i++){
	    		Point p = (Point)pontosCanal.get(i);
	    		g.drawString("D+" + (i+1) + "=" + getPreco(p.y,menorPto,fator,escala,inicioGrafprincipal) ,900,400 +(i*10));
	    		
	    		g.drawLine(p.x, p.y-15, p.x, p.y);
	    	}
	       	x1Click = 0;
        	y1Click = 0;
        	x2Click = 0;
        	y2Click = 0;
        }
    	
        
        x++;
        g.setColor(Color.blue);
        g.setColor(new Color(174,97,220));
	        
    }//end paintComponent
    
    
    public boolean isIndicado(ArrayList indices, int indDesenhado){
    	
    	for(int i=0;i < indices.size();i++){
    		if(((Integer)indices.get(i)).intValue()==indDesenhado){
    			return true;
    		}
    	}
    	
    	return false;
    }
	        
    public void setPaginaFrente(){
    	primeiro = primeiro + intervalo-1;
    	ultimo = ultimo + intervalo-1;
    }
        	
    public void setPaginaAtras(){
    	primeiro = primeiro - intervalo-1;
    	ultimo = ultimo - intervalo-1;
        	
    }
	        
    public void setDiaFrente(){
    	primeiro = primeiro + 1;
    	ultimo = ultimo + 1;
    }
	        
    public void setDiaAtras(){
    	primeiro = primeiro - 1;
    	ultimo = ultimo - 1;

    }
        
    public void setPrimeiroCandle(){
    	primeiro = -1;
    	ultimo = -1;
    }
       
    public void setUltimoCandle(){
    	primeiro = Integer.MAX_VALUE;
    	ultimo = Integer.MAX_VALUE;
    
    }
    
    
  public void zoomMais(){
	  
	   if(z==2){
		   intervalo = 120;
		   espacoDia = 7;
		   largCandle = 5;
		   z = 3;
		   
	   }else if(z==1){
		   intervalo = 200;
		   espacoDia = 5;
		   largCandle = 3;
		   z = 2;
	   }else{
		   intervalo = 120;
		   espacoDia = 7;
		   largCandle = 5;
		   z = 3;
	   }
	  primeiro = Integer.MAX_VALUE;
   	  ultimo = Integer.MAX_VALUE;
  }
  public void zoomMenos(){
	  
	   if(z==3){
		   intervalo = 200;
		   espacoDia = 5;
		   largCandle = 3;
		   z = 2;
	   }else if(z==2){
		   intervalo = 300;
		   espacoDia = 3;
		   largCandle = 2;
		   z = 1;
	   }else{
		   intervalo = 300;
		   espacoDia = 3;
		   largCandle = 2;
		   z = 1;
	   }
	   primeiro = Integer.MAX_VALUE;
   	   ultimo = Integer.MAX_VALUE;
 }
    
    public void setDias(int dias){
    	System.out.println("Setou dias");
    	this.dias = dias;   
    }
    
	/**
	 * @return Returns the acao.
	 */
	public String getAcao() {
		return acao;
	}
	/**
	 * @param acao The acao to set.
	 */
	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public void setDiasCandle(int d){
		this.diasCandle = d;
		reload = true;
	}
	
	public int getPontoY(int x){
		
		ArrayList pontos = new ArrayList();
		double c = (new BigDecimal(y1Click - y2Click).divide(new BigDecimal(x1Click - x2Click),4,BigDecimal.ROUND_HALF_UP)).doubleValue();   
	
		return  (new Double(c*(x-x1Click) + y1Click)).intValue();

	}
	
	public ArrayList getPontos(int i,int fator){
			
		ArrayList pontos = new ArrayList();
		double c = (new BigDecimal(y1Click - y2Click).divide(new BigDecimal(x1Click - x2Click),4,BigDecimal.ROUND_HALF_UP)).doubleValue();   
	
		for(int j=1;j<4;j++){
			
			int x = (i+j)*fator;
			
			int ptoY = (new Double(c*(x-x1Click) + y1Click)).intValue();
			Point p = new Point(x,ptoY);
			pontos.add(p);
		}
		return pontos;
	}
	
	public double getPreco(int y,double menor, double fator,int esc, int tela){
		
		y = tela - y;
		double prc = ((new BigDecimal(y).divide(new BigDecimal(esc),4,BigDecimal.ROUND_HALF_UP)).doubleValue()+menor)*fator;   

		return (new BigDecimal(prc).divide(new BigDecimal(100),4,BigDecimal.ROUND_HALF_UP)).doubleValue();
		
	}
	
	public void calcIFRsFuturos(double[]close, int dias){
		
		double ultimo = close[close.length-1];
		double[] closeTemp = new double[dias + 2];
		
		int j=0;
		
		for(int i=close.length-1;i>=close.length-1 -dias +1;i--){
			closeTemp[closeTemp.length-2-j++]=close[i];
		}
		
		for(int i=-4;i<=4;i++){
			closeTemp[closeTemp.length-1]= getValor(ultimo,(new BigDecimal(i).divide(new BigDecimal(100),4,BigDecimal.ROUND_HALF_UP)).doubleValue());
			System.out.print(closeTemp[closeTemp.length-1] + " ");
			IFR ifr = new IFR(closeTemp,dias,0);
			System.out.println("ifr " + i + "% = "+ ifr.getIFR()[closeTemp.length-1]);
		}
		
	}
	
	
	public double getValor(double preco,double var){
		System.out.println(var);
		if(var>=0){
			return preco*(1+var);
		}else{
			return (preco - (preco*(-1)*var));
		}
	}
	
	
	class MousePanel implements MouseListener, MouseMotionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			
			
			if(e.getButton() == MouseEvent.BUTTON1){
				if(x1Vazio){
					x1Vazio = false;
					x1Click = e.getPoint().x;
					y1Click = e.getPoint().y;
					System.out.println(x1Click+ " " +y1Click);
				}else{
					x1Vazio = true;
					x2Click = e.getPoint().x;
					y2Click = e.getPoint().y;
					System.out.println(x2Click+ " " +y2Click);
				}
				
				MainFrame.repaintSpaceView();
			}
			
			if(e.getButton() == MouseEvent.BUTTON3){
				clicouDireito = true;
				xDireitoClick = e.getPoint().x;
				yDireitoClick = e.getPoint().y;
				MainFrame.repaintSpaceView();
			}
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		public void mouseMoved(MouseEvent e) {
						
			int dia = -1 ;

			//procura o dia relativo que o mouse passa por cima
			for(int i=inicio; i <= fim; i++){ 
				//se o ponto x esta dentro do intervalo da candle na coordenada X
				if(e.getPoint().x >= pontoXInicial[i] && e.getPoint().x <= pontoXFinal[i])
					dia = i;
			}
			
//			System.out.println("e.getPoint().x = " + e.getPoint().x);
//			System.out.println("diaRelativo = " + diaRelativo);
			
			if(dia != -1){
				
				
				
				if(candles != null && dia < candles.size()){
					String date = candles.get(dia).getPeriodo();
					
					MainFrame.showPreco("Data: " + date + " Preço:" 
							+ getPreco(e.getPoint().y,menorPto,fator,escala,inicioGrafprincipal) 
							+ " A: " + candles.get(dia).getAbertura() 
							+ " Max: " + candles.get(dia).getMaxima()
							+ " Min: " + candles.get(dia).getMinima()
							+ " F: " + candles.get(dia).getFechamento());
				}
			}
			else{
				MainFrame.showPreco(null); //esconde o tooltip
			}
		}
	}
	
}//endclass SpaceView
