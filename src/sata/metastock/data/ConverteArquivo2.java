/*
 * Created on 09/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package sata.metastock.data;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import sata.metastock.util.AcaoBovespa;

/**
 * @author Flavio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConverteArquivo2 {

	public static void main(String args[]) {

		//acao: PETR4 opcao: PETR
		
//		String[] acoes = {"PETR4","TNLP4","ABCB4", "BBDC4", "BBRK3","CMIG4","GGBR4","OGXP3","USIM5"};
//		
//		for (int i = 0; i < acoes.length; i++)
//		{
//			geraArqCotacao(acoes[i], "2008");
//		}
		//geraArqCotacao("VALE", "2009");
		//geraArqCotacao("VALE5", "2009");

		geraArqCotacao("PETR4", "2011");
		//geraArqCotacao("VALE5", "2010");
	}

	/**
	 * @return
	 */
	public static ArrayList geraArqCotacao(String codAcaoOpcao,
			String ano) {

		ArrayList acoes = new ArrayList();
		FileInputStream file;
		int tamStrNomeAcaoOpcao;
		int tamStrNomeParametro;
		try {

			file = new FileInputStream("COTAHIST_A" + ano + ".TXT");
			DataInputStream myInput = new DataInputStream(file);

			String thisLine = "";

			DateFormat df = DateFormat.getDateInstance();

			Date agora = new Date();
			long t1 = agora.getTime();
			System.out.println("Processando...");

			if (codAcaoOpcao.length() == 5) {
				tamStrNomeAcaoOpcao = 5;
				tamStrNomeParametro = 5;
			} else {
				tamStrNomeAcaoOpcao = 7;
				tamStrNomeParametro = 4;
			}

			while ((thisLine = myInput.readLine()) != null) {

				if (thisLine.substring(0, 2).equals("01")) {
					
					
					if (thisLine.substring(12, 24).trim().length() == tamStrNomeAcaoOpcao
							&& thisLine.substring(12, 24).trim().substring(0,
									tamStrNomeParametro).equalsIgnoreCase(
									codAcaoOpcao)) {

						// System.out.println("ok");
						/*
						 * String novaLinha = acao.getCodigo() + acao.getData() +
						 * "," + acao.getAbertura().replace(',','.') + "," +
						 * acao.getMaxima().replace(',','.') + "," +
						 * acao.getMinima().replace(',','.') + "," +
						 * acao.getFechamento().replace(',','.') + "," +
						 * acao.getQtd() + "," + acao.getFechamento();
						 * 
						 * System.out.println(novaLinha);
						 */

						AcaoBovespa acao = new AcaoBovespa();

						acao.setData(thisLine.substring(2, 10).trim()); // data
						acao.setCodigo(thisLine.substring(12, 24).trim()); // código
						acao.setEmpresa(thisLine.substring(27, 39).trim()); // nome
						acao.setTipo(thisLine.substring(39, 49).trim()); // tipo
						acao.setAbertura(""
								+ Integer.parseInt(thisLine.substring(56, 69)
										.trim())); // abertura
						acao.setMaxima(""
								+ Integer.parseInt(thisLine.substring(69, 82)
										.trim())); // máximo
						acao.setMinima(""
								+ Integer.parseInt(thisLine.substring(82, 95)
										.trim())); // minimo
						acao.setMedia(""
								+ Integer.parseInt(thisLine.substring(95, 108)
										.trim()));// medio
						acao.setFechamento(""
								+ Integer.parseInt(thisLine.substring(108, 121)
										.trim()));// fechamento
						acao.setOfertaCompra(""
								+ Integer.parseInt(thisLine.substring(121, 134)
										.trim()));// melhor oferta compra
						acao.setOfertaVenda(""
								+ Integer.parseInt(thisLine.substring(134, 147)
										.trim()));// melhor oferta venda
						acao.setNegs(""
								+ Integer.parseInt(thisLine.substring(147, 152)
										.trim()));// numero de negociações
						acao.setQtd(""
								+ Long.parseLong(thisLine.substring(170, 188)
										.trim()));// volume
						acoes.add(acao);
					}

					//		     	
					// AcaoBovespa acao = new AcaoBovespa();
					//			     			     	
					// acao.setData(thisLine.substring(2,10).trim()); //data
					// acao.setCodigo(thisLine.substring(12,24).trim());
					// //código
					// acao.setEmpresa(thisLine.substring(27,39).trim()); //nome
					// acao.setTipo(thisLine.substring(39,49).trim()); //tipo
					// acao.setAbertura("" +
					// Integer.parseInt(thisLine.substring(56,69).trim()));
					// //abertura
					// acao.setMaxima("" +
					// Integer.parseInt(thisLine.substring(69,82).trim()));
					// //máximo
					// acao.setMinima("" +
					// Integer.parseInt(thisLine.substring(82,95).trim()));
					// //minimo
					// acao.setMedia("" +
					// Integer.parseInt(thisLine.substring(95,108).trim()));//medio
					// acao.setFechamento("" +
					// Integer.parseInt(thisLine.substring(108,121).trim()));//fechamento
					// acao.setOfertaCompra("" +
					// Integer.parseInt(thisLine.substring(121,134).trim()));//melhor
					// oferta compra
					// acao.setOfertaVenda("" +
					// Integer.parseInt(thisLine.substring(134,147).trim()));//melhor
					// oferta venda
					// acao.setNegs("" +
					// Integer.parseInt(thisLine.substring(147,152).trim()));//numero
					// de negociações
					// acao.setQtd("" +
					// Long.parseLong(thisLine.substring(170,188).trim()));//volume
					//			     				     	
					// if(acao.getCodigo().length()==7 &&
					// acao.getCodigo().substring(0,5).equalsIgnoreCase("PETRE")){
					// //System.out.println("ok");
					// /* String novaLinha = acao.getCodigo() + acao.getData() +
					// "," + acao.getAbertura().replace(',','.') + "," +
					// acao.getMaxima().replace(',','.') + "," +
					// acao.getMinima().replace(',','.') +
					// "," + acao.getFechamento().replace(',','.') + "," +
					// acao.getQtd() + "," + acao.getFechamento();
					//
					// System.out.println(novaLinha);*/
					// acoes.add(acao);
					// }

					/*
					 * String novaLinha = acao.getCodigo() + acao.getData() +
					 * "," + acao.getAbertura().replace(',','.') + "," +
					 * acao.getMaxima().replace(',','.') + "," +
					 * acao.getMinima().replace(',','.') + "," +
					 * acao.getFechamento().replace(',','.') + "," +
					 * acao.getQtd() + "," + acao.getFechamento();
					 * 
					 * System.out.println(novaLinha);
					 */
				}
			}
			agora = new Date();
			long tempo = agora.getTime() - t1;
			System.out.println("Tempo de processamento: " + tempo);

			ArrayList acoesOrdenada = ordena(acoes);

			try {
				FileWriter writer = new FileWriter("saida_" + codAcaoOpcao + "_" + ano + " .txt");
				PrintWriter saida = new PrintWriter(writer);

				for (int i = 0; i < acoesOrdenada.size(); i++) {

					AcaoBovespa acao = (AcaoBovespa) acoesOrdenada.get(i);
					/*
					 * String novaLinha = acao.getCodigo() + " " +
					 * acao.getData() + "," +
					 * acao.getAbertura().replace(',','.') + "," +
					 * acao.getMaxima().replace(',','.') + "," +
					 * acao.getMinima().replace(',','.') + "," +
					 * acao.getFechamento().replace(',','.') + "," +
					 * acao.getQtd() + "," + acao.getFechamento();
					 * 
					 */

					/*
					 * if(acao.getCodigo().equals("PETRK62")){ valor1 =
					 * Integer.parseInt(acao.getFechamento()); data1 =
					 * acao.getData(); } if(acao.getCodigo().equals("PETRK64")){
					 * 
					 * valor2 = Integer.parseInt(acao.getFechamento()); data2 =
					 * acao.getData(); }
					 * 
					 * if(valor2 != 0 && valor1 != 0 && data1.equals(data2)){
					 * 
					 * int resultado = (18*valor2 - 10*valor1);
					 * 
					 * System.out.println(data2 + " " + resultado);
					 * 
					 * valor1 = 0; valor2 = 0; }
					 */

					String novaLinha;
					
					novaLinha = acao.getCodigo() + " " + acao.getAbertura() + " " 
					+ acao.getMaxima() + " " + acao.getMinima() + " "
					+ acao.getFechamento() + " " + acao.getData() + " "
					+ acao.getQtd();
				

					// System.out.println(novaLinha);
					saida.println(novaLinha);

				}

				saida.close();
				writer.close();
			} catch (Exception e) {
			}

			file.close();
			myInput.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return acoes;
	}

	public static ArrayList converte(String ac, int tamanho) {

		ArrayList acoes = new ArrayList();
		FileInputStream file;
		ArrayList acoesOrdenada = null;

		try {
			file = new FileInputStream(
					"C:\\Documents and Settings\\zfoo\\Meus documentos\\Dados historicos\\COTAHIST_A2007.TXT");
			DataInputStream myInput = new DataInputStream(file);

			String thisLine = "";

			DateFormat df = DateFormat.getDateInstance();

			Date agora = new Date();
			long t1 = agora.getTime();
			// System.out.println(t1);

			while ((thisLine = myInput.readLine()) != null) {

				if (thisLine.substring(0, 2).equals("01")) {

					AcaoBovespa acao = new AcaoBovespa();

					acao.setData(thisLine.substring(2, 10).trim()); // data
					acao.setCodigo(thisLine.substring(12, 24).trim()); // código
					acao.setEmpresa(thisLine.substring(27, 39).trim()); // nome
					acao.setTipo(thisLine.substring(39, 49).trim()); // tipo
					acao.setAbertura(""
							+ Integer.parseInt(thisLine.substring(56, 69)
									.trim())); // abertura
					acao.setMaxima(""
							+ Integer.parseInt(thisLine.substring(69, 82)
									.trim())); // máximo
					acao.setMinima(""
							+ Integer.parseInt(thisLine.substring(82, 95)
									.trim())); // minimo
					acao.setMedia(""
							+ Integer.parseInt(thisLine.substring(95, 108)
									.trim()));// medio
					acao.setFechamento(""
							+ Integer.parseInt(thisLine.substring(108, 121)
									.trim()));// fechamento
					acao.setOfertaCompra(""
							+ Integer.parseInt(thisLine.substring(121, 134)
									.trim()));// melhor oferta compra
					acao.setOfertaVenda(""
							+ Integer.parseInt(thisLine.substring(134, 147)
									.trim()));// melhor oferta venda
					acao.setNegs(""
							+ Integer.parseInt(thisLine.substring(147, 152)
									.trim()));// numero de negociações
					acao.setQtd(""
							+ Long.parseLong(thisLine.substring(170, 188)
									.trim()));// volume

					if (acao.getCodigo().length() == tamanho
							&& acao.getCodigo().substring(0, tamanho)
									.equalsIgnoreCase(ac)) {
						// System.out.println("ok");
						acoes.add(acao);
					}

					/*
					 * String novaLinha = acao.getCodigo() + acao.getData() +
					 * "," + acao.getAbertura().replace(',','.') + "," +
					 * acao.getMaxima().replace(',','.') + "," +
					 * acao.getMinima().replace(',','.') + "," +
					 * acao.getFechamento().replace(',','.') + "," +
					 * acao.getQtd() + "," + acao.getFechamento();
					 * 
					 * System.out.println(novaLinha);
					 */
				}
			}
			agora = new Date();
			long tempo = agora.getTime() - t1;
			System.out.println("Tempo de processamento: " + tempo);

			acoesOrdenada = ordena(acoes);

			file.close();
			myInput.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return acoesOrdenada;
	}

	public static ArrayList ordena(ArrayList acoes) {

		ArrayList ordenada = new ArrayList();

		int x = acoes.size();

		for (int j = 0; j < x; j++) {
			long menor = 99999999;
			AcaoBovespa acaoMenor = null;
			int indice = 0;
			for (int i = 0; i < acoes.size(); i++) {

				AcaoBovespa acao = (AcaoBovespa) acoes.get(i);
				if (Long.parseLong(acao.getData()) < menor) {

					menor = Long.parseLong(acao.getData());
					acaoMenor = acao;
					indice = i;
				}

			}
			ordenada.add(acaoMenor);
			acoes.remove(indice);
		}

		return ordenada;
	}
}
