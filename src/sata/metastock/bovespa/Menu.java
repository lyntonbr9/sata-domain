/*
 * Created on 12/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.bovespa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.metastock.data.Merge2;


/**
 * @author Flavio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Menu extends JPanel{
	
	JComboBox cmbAcoes;
	
	JButton inicio = new JButton("Inicio");
    JButton diaAtras = new JButton("<");    
    JButton diaFrente = new JButton(">");
    JButton paginaAtras = new JButton("<<");    
    JButton paginaFrente = new JButton(">>");
    JButton fim = new JButton("Fim"); 
	
    JButton zoomMais = new JButton("Zoom +");
    JButton zoomMenos = new JButton("Zoom -");
	
    JLabel labelDiario = new JLabel("D");
    JRadioButton bDiario = new JRadioButton();
	
    JLabel labelSemanal = new JLabel("S");
    JRadioButton bSemanal = new JRadioButton();

    JLabel labelMensal = new JLabel("M");
    JRadioButton bMensal = new JRadioButton();
   	
    JLabel lblOpacidade = new JLabel("Opacidade: ");
   	JTextField txtOpacidade = new JTextField("",4);

	private int cont = 0;
	
	private String acoes[] = null;
	
    public Menu() {
    	
		IAtivoDAO ativoDAO = SATAFactoryFacade.getAtivoDAO();
		List<String> listaAtivos = ativoDAO.getCodigosAtivos();
		acoes = new String[listaAtivos.size()];
		for(int i=0; i < listaAtivos.size(); i++)
			acoes[i] = listaAtivos.get(i);
		
		cmbAcoes = new JComboBox(acoes);
		
		paginaAtras.addActionListener(new AtrasPaginaIntervaloListener());      
		paginaFrente.addActionListener(new FrentePaginaIntervaloListener());   
		diaAtras.addActionListener(new AtrasDiaIntervaloListener());
		diaFrente.addActionListener(new FrenteDiaIntervaloListener());
		inicio.addActionListener(new primeiroCandleListener());
		fim.addActionListener(new ultimoCandleListener());
		zoomMais.addActionListener(new ZoomMaisListener());
		zoomMenos.addActionListener(new ZoomMenosListener());
//        bDiario.addActionListener(new DiaCandleListener());       
//        bSemanal.addActionListener(new SemanaCandleListener());       
//        bMensal.addActionListener(new MesCandleListener());
        
        // Associate the two buttons with a button group
        bDiario.setSelected(true);
        ButtonGroup periodicidadegroup = new ButtonGroup();
        periodicidadegroup.add(bDiario);
        periodicidadegroup.add(bSemanal);
        periodicidadegroup.add(bMensal);

        
        cmbAcoes.addActionListener(new ComboSelecionaAcoesListener());
        txtOpacidade.addActionListener(new OpacidadeListener());
        
        
        /*layout sequencial para esquerda*/
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

    	this.add(cmbAcoes);
    	//this.add(diasAtras);
    	
    	
    	this.add(inicio);
    	this.add(diaAtras);
    	this.add(diaAtras);
    	this.add(diaFrente);
    	this.add(paginaAtras);
    	this.add(paginaFrente);
    	this.add(fim);
    	this.add(zoomMais);
    	this.add(zoomMenos);
    	this.add(labelDiario);
    	this.add(bDiario);
    	this.add(labelSemanal);
    	this.add(bSemanal);
    	this.add(labelMensal);
    	this.add(bMensal);
    	this.add(lblOpacidade);
    	this.add(txtOpacidade);

        setPreferredSize(new Dimension(200, 50));
        setBackground(new Color(210,210,210));
    }//end constructor
    

    public String getAcao(){
    	//return textfield.getText();
    	return (String) cmbAcoes.getSelectedItem();
    }

    class OpacidadeListener implements ActionListener{
    	public void actionPerformed(ActionEvent evt){
    		JTextField txtSource = (JTextField)evt.getSource();
    		String valorOpacidade = txtSource.getText();
    		if (valorOpacidade.equalsIgnoreCase("") == false){
    			valorOpacidade = valorOpacidade.replace(",", ".");
    			if(Float.parseFloat(valorOpacidade) > 1)
    				valorOpacidade = "1.0";
    			MainFrame.setOpacidade(Float.parseFloat(valorOpacidade));
    		}
    			
    	}
    }
    
    class ComboSelecionaAcoesListener implements ActionListener{
    	public void actionPerformed(ActionEvent evt){
    		JComboBox cmbSource = (JComboBox)evt.getSource();
    		String acao = (String) cmbSource.getSelectedItem();
    		MainFrame.setAcao(acao);   			
    	}
    }
    
    class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JTextField textfield = (JTextField)evt.getSource();
            MainFrame.setAcao(textfield.getText());
        }

    }
    
    class ultimoCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
         
        	MainFrame.setUltimoCandle();
        	
        }
    }
    
    class primeiroCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
         
        	MainFrame.setPrimeiroCandle();
        }	
    }
    
    class DiasListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JTextField diasAtras = (JTextField)evt.getSource();
            MainFrame.setDias(diasAtras.getText());
        }

	
    }
    
    class FrentePaginaIntervaloListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	MainFrame.nextPage();
        	
        }	
    }

    class AtrasPaginaIntervaloListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	MainFrame.lastPage();
        }	
    }
           
    class FrenteDiaIntervaloListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	MainFrame.nextDia();
           	   	
           	   }
           }

    class AtrasDiaIntervaloListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	MainFrame.lastDia();
        }
    }
    
    
    class ZoomMaisListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	MainFrame.zoomMais();
           	   	
        }
    }

    class ZoomMenosListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
        	
        	MainFrame.zoomMenos();
        }
    }
//    class NextListener implements ActionListener {
//        public void actionPerformed(ActionEvent evt) {
//           String acao = (String) cmbAcoes.getSelectedItem();
//           
//           for(int i=0;i<acoes.length;i++){
//           	   if(acoes[i].equalsIgnoreCase(acao)){
//           	   	
//           	   		if(i==acoes.length-1){
//	           	   		MainFrame.setAcao(acoes[0]);     
//	           	   		cmbAcoes.setSelectedItem(acoes[0]);
//	           	   		return;
//           	   		}else{
//	           	   		MainFrame.setAcao(acoes[i+1]);
//	           	   		cmbAcoes.setSelectedItem(acoes[i+1]);
//	           	   		return;
//           	   		}	
//           	   }
//           }
//        }
//    }

//    class NextListener implements ActionListener {
//        public void actionPerformed(ActionEvent evt) {
//           String acao = textfield.getText();
//           
//           for(int i=0;i<acoes.length;i++){
//           	   if(acoes[i].equalsIgnoreCase(acao)){
//           	   	
//           	   		if(i==acoes.length-1){
//	           	   		MainFrame.setAcao(acoes[0]);     
//	           	   		textfield.setText(acoes[0]);
//	           	   		return;
//           	   		}else{
//	           	   		MainFrame.setAcao(acoes[i+1]);     
//	           	   		textfield.setText(acoes[i+1]);
//	           	   		return;
//           	   		}
//           	   	
//           	   		
//           	   }
//           }
//            
//        }
//    }
    
           
 
    
    class DiaCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(1);
 
        }
    }
    
    class SemanaCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(5);
 
        }	
    }
    class MesCandleListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
           
        	MainFrame.setDiasCandle(22);
 
        }
    }
}
