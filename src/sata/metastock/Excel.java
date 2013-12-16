/*
 * Sistema de Formacao de Precos - FORPREC
 * Empresa: PETROBRAS DISTRIBUIDORA
 * Gerencia: GTI/GESUN/GESWEB
 * Analistas: 
 * 		Andre Serodio 		- Chave: ZFM1
 * 		Flavio Guimarães 	- Chave: ZFOO
 * 		Marcos L. L. Filho	- Chave: ZFO8
 * 
 * Criado em Feb 23, 2005
 * Hora: 10:13:39 AM
 */
package sata.metastock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import sata.metastock.exceptions.CelulaInvalidaEX;


/**
 * @author Flávio Guimarães Chaves
 * GTI/GESUN/GESWEB
 * Chave ZFOO
 *
 */
public class Excel {

	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private FileOutputStream fileOut;
	private int numberOfSheets=0;
	
	
	
	/**
	 * Indica o caminho do arquivo excel a ser lido
	 * @param path
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Excel(String path) throws FileNotFoundException, IOException{
		
		fs = new POIFSFileSystem(new FileInputStream(path));
		wb = new HSSFWorkbook(fs);
		numberOfSheets = wb.getNumberOfSheets();		
	/*	HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(path);
			wb.write(fileOut);
			fileOut.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Construtor para quem quer criar um arquivo do excel
	 * @param path
	 * @param write
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Excel(boolean write) throws FileNotFoundException {
		wb = new HSSFWorkbook();
		
		
		
		
	}
	
	/**
	 * Classe que retorna uma matriz de inteiros lendo da planilha 
	 * @param path
	 * @return
	 * @throws CelulaInvalidaEX
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Object[][] readFileToMatriz(int numSheet) throws CelulaInvalidaEX, FileNotFoundException, IOException{
		Object[][] matriz = null;			//matriz de retorno que representa a planilha do excel
		
		HSSFSheet sheet = wb.getSheetAt(numSheet);				// recupera a primeira planilha
		int numRows = sheet.getPhysicalNumberOfRows(); // número de linhas da planilha
		ArrayList[] rows = new ArrayList[numRows];	// linhas da planilha
		int maxColumn = 0;					// número de colunas da planilha obs: a calcular
			
			
		/*Extrai os valores da planilha e armazena em vetor de arrayList (uma matriz)*/
		for(int i=0;i<numRows;i++){
			
			HSSFRow row = sheet.getRow(i);
			
			HSSFCell cell = row.getCell((short)0);
			
			rows[i] = new ArrayList();
			int j =1;
			while(cell!=null){				
				if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
					rows[i].add(new String("" + cell.getNumericCellValue()));
				}else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
					rows[i].add(new String("" + cell.getNumericCellValue()));
				}else if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
					rows[i].add(new String(cell.getStringCellValue()));
				}else{
					rows[i].add("");
					//throw new CelulaInvalidaEX("Célula da linha " + (i +1)+ " e coluna " + j + " está em um formato inválido");
				}
				cell = row.getCell((short)j);
				if(j>maxColumn){
					maxColumn=j;
				}
				j++;
			}
		}	
			
		matriz = new Object[rows.length][maxColumn];
			
		for(int i=0;i<rows.length;i++){
			for(int j=0;j<rows[i].size();j++){
				matriz[i][j] = rows[i].get(j);
			}
		}
			
		return matriz;
		
	}
	
	/**
	 * Cria uma planilha do excel, escrevendo os valores passados na matriz
	 * de objetos e colocando os títulos passados comom parametros
	 * 
	 * @param titulosColunas Se o tituloColunas for nulo, ele preencherá somente com os valores da matriz
	 * @param matriz conteúdo do arquivo
	 * @throws IOException
	 */
	public void createExcelFileSheet(String[] titulosColunas, Vector matriz){
		

			 HSSFSheet sheet = wb.createSheet();
			   		
			/*Cria a ilnha de títulos da planilha se houver uma*/
			if(titulosColunas!=null){
				HSSFRow row = sheet.createRow((short)0);
				for(int i=0;i<titulosColunas.length;i++){
					HSSFCell cell = row.createCell((short)i); 
					System.out.println("titulo: " + titulosColunas[i]);
					cell.setCellValue(titulosColunas[i]); 
				}
			}
			
			/*Copia os dados para a planilha*/
			for(int i=0;i<matriz.size();i++){
				HSSFRow row = sheet.createRow((short)(i));
				for(int j=0;j<((Vector)matriz.get(i)).size();j++){
					HSSFCell cell = row.createCell((short)j); 		
					cell.setCellValue((String)((Vector)matriz.elementAt(i)).elementAt(j));
				}
			}
	
	}
	
	/**
	 * Save o arquivo do excel criado
	 * 
	 * @throws IOException
	 */
	public void writeFile(String path) throws IOException{
		FileOutputStream fileOut = new FileOutputStream(path);
		wb.write(fileOut);
		fileOut.close();	
	}
	
	
	/**
	 * Retorna o número de planilhas do arquivo
	 * @return
	 */
	public int getNumPlanilha(){
		return this.numberOfSheets;
	}
	
	
	public static void main(String args[]){
		
		Excel e;
		try {
			e = new Excel("H:\\flavio\\bolsa\\table.csv");
			Object[][]plan = e.readFileToMatriz(0);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CelulaInvalidaEX e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		

		
		/*
		 HSSFWorkbook workBook = new HSSFWorkbook();
		  
		 HSSFSheet sheet = workBook.createSheet();
		   
		    //**************report data rows and cols*********/
		    // create 5 rows of data
/*		    HSSFRow row1 = sheet.createRow((short) 0);
		    HSSFRow row2 = sheet.createRow((short) 1);
		    HSSFRow row3 = sheet.createRow((short) 2);
		    HSSFRow row4 = sheet.createRow((short) 3);
		    HSSFRow row5 = sheet.createRow((short) 4);
		    
		    // create the 2 cells for each row
		    HSSFCell c11 = row1.createCell((short) 0);
		    HSSFCell c12 = row1.createCell((short) 4);
		    HSSFCell c21 = row2.createCell((short) 0);
		    HSSFCell c22 = row2.createCell((short) 4);
		    HSSFCell c31 = row3.createCell((short) 0);
		    HSSFCell c32 = row3.createCell((short) 4);
		    HSSFCell c41 = row4.createCell((short) 0);
		    HSSFCell c42 = row4.createCell((short) 4);
		    
		    // writing data to the cells
		    c11.setCellValue("Sam");
		    c12.setCellValue(100);
		    
		    c21.setCellValue("John");
		    c22.setCellValue(50);
		    
		    c31.setCellValue("Paul");
		    c32.setCellValue(25);
		    
		    c41.setCellValue("Richard");
		    c42.setCellValue(20);

	
		    FileOutputStream stream;
			try {
				stream = new FileOutputStream("c:/temp/Book5.xls");
		
				workBook.write(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}

		*/
	}
	
}
