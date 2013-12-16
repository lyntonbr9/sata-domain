package sata.metastock.teste;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestaConexaoDB2 {

 public static void main(String[] args) {

  Connection connection = null;

  try  {
	   // Carregando o Driver JDBC
	 
	   String driverName = "COM.ibm.db2.jdbc.net.DB2Driver";
	                     
	   Class.forName(driverName);
	
	   // Criando uma nova conexão com o banco  DB2 7x	
	   // jdbc:db2://172.31.9.142:6789/DB2D (url de conexão)
	                                                
	   String serverName = "172.31.9.142";  //DB2Connect  (alias)
	   String portNumber = "6789";         //50000  (porta default)
	   String sid = "DB2P";               //DB2P (banco de produção)
	   String url = "jdbc:db2://" + serverName + ":" + portNumber + "/"
	   + sid;
	   String username = "n529";  
	   String password = "ben529";
	
	   connection = DriverManager.getConnection(url, username, password);
	
	   DatabaseMetaData dmd = connection.getMetaData();
	   System.out.println("Conectado a base DB2 em Desenvolvimento.\n");
	   System.out.println("Url : " + dmd.getURL());
	   System.out.println("Driver :  " + dmd.getDriverName());
	   System.out.println("Versão :  " + dmd.getDriverVersion());
	   
	   PreparedStatement preparedStatement = connection.prepareStatement("select EMBR_NOM_EMPREGADO from B140.EMPREGADO_BR_R3");   // sql de teste comentada
	   ResultSet rs = preparedStatement.executeQuery();
	   while(rs.next()) {
	    System.out.println(rs.getString("EMBR_NOM_EMPREGADO"));
	   }
	   rs.close();
	   preparedStatement.close(); 
	  } catch (ClassNotFoundException cnfe){
		  cnfe.printStackTrace();
		  System.err.println(" Não foi possível encontrar o Driver JDBC");
	  } catch (SQLException sql){
		  sql.printStackTrace();
		  System.err.println("Não foi possível executar a query");
	  } finally {
		  try{
			  connection.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
 }
}
