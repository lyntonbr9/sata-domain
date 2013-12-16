package sata.metastock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import sata.metastock.exceptions.CelulaInvalidaEX;


public class Read {
	
	public static Vector converteVector(Object[][] matrix){
		
		Vector v = new Vector();
		
		for(int i=0;i<matrix.length;i++){
			
			Vector vLinha = new Vector();
			for(int j=0;j<matrix[i].length;j++){
				
				vLinha.add(matrix[i][j]);
				
			}
			
			v.add(vLinha);
			
		}
	
		return v;
	}
	
	
	public static void main(String[] args){
		
		try {
		//	Excel e = new Excel("d:/forprec/custoGasC.xls");
			Excel e = new Excel("H:\\flavio\\bolsa\\bovespa\\folha\\cotacao10052006.xls");
			Object[][]plan = e.readFileToMatriz(0);
			
			
			
			for(int i=0;i<plan.length;i++){
				File out = new File("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[i][0] + ".xls");
				
				if(!out.exists()){
					
					Vector meta1 = new Vector();
					meta1.add("MetaStock(tm) data file \\\\" + plan[i][0] + "\\");
					
					Vector meta2 = new Vector();
					meta2.add("TICKER");
					meta2.add("PER");
					meta2.add("DATE");
					meta2.add("TIME");
					meta2.add("OPEN");
					meta2.add("HIGH");
					meta2.add("LOW");
					meta2.add("CLOSE");
					meta2.add("VOLUME");
					meta2.add("O/I");
					
					Vector newAcao = new Vector();
					newAcao.add(meta1);
					newAcao.add(meta2);
					
					
					Excel ex = new Excel(true);
					ex.createExcelFileSheet(null, newAcao);
					ex.writeFile("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[i][0] + ".xls");
				}
				
				Excel acao = new Excel("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[i][0] + ".xls");
				Vector dadosAcao = Read.converteVector(acao.readFileToMatriz(0));
				
				Vector novaLinha = new Vector();
								
				novaLinha.add(plan[i][0]);
				novaLinha.add("D");
				novaLinha.add("10/05/2006");
				novaLinha.add("00:00:00");
				novaLinha.add(plan[i][5]);
				novaLinha.add(plan[i][7]);
				novaLinha.add(plan[i][8]);
				novaLinha.add(plan[i][2]);
				novaLinha.add(plan[i][9]);
				novaLinha.add("0");
					
				dadosAcao.add(novaLinha);
				
				Excel update = new Excel(true);
				update.createExcelFileSheet(null, dadosAcao);
				update.writeFile("H:\\flavio\\bolsa\\bovespa\\acoes\\" + plan[i][0] + ".xls");
				System.out.println(plan[i][0] + " atualizado");
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CelulaInvalidaEX e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
