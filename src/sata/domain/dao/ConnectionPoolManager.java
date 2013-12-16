package sata.domain.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class ConnectionPoolManager implements IConstants{

//	private static Properties SATAProps;
//	private String databaseUrl = SATAProps.getProperty(PROP_SATA_DB_URL);
//	private String userName = SATAProps.getProperty(PROP_SATA_DB_USERNAME);
//	private String password = SATAProps.getProperty(PROP_SATA_DB_PASSWORD);
	private String databaseUrl = SATAPropertyLoader.getProperty(PROP_SATA_DB_URL);
	private String userName = SATAPropertyLoader.getProperty(PROP_SATA_DB_USERNAME);
	private String password = SATAPropertyLoader.getProperty(PROP_SATA_DB_PASSWORD);
	
	List<Connection> connectionPool = new ArrayList<Connection>();

	static {
		try {
			//vai carregar os properties de configuracao do SATA
//			SATAProps = SATAPropertyLoader.loadProperties(ARQ_SATA_CONF);
			
//			Class.forName(SATAProps.getProperty(PROP_SATA_DB_JDBC_DRIVER));
			Class.forName(SATAPropertyLoader.getProperty(PROP_SATA_DB_JDBC_DRIVER));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public ConnectionPoolManager()
	{
		initialize();
	}

	public ConnectionPoolManager(String databaseUrl, String userName, String password)
	{
		this.databaseUrl = databaseUrl;
		this.userName = userName;
		this.password = password;
		initialize();
	}

	private void initialize()
	{
		//Here we can initialize all the information that we need
		initializeConnectionPool();
	}

	private void initializeConnectionPool()
	{
		while(!checkIfConnectionPoolIsFull())
		{
			System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");
			//Adding new connection instance until the pool is full
			connectionPool.add(createNewConnectionForPool());
		}
		System.out.println("Connection Pool is full.");
	}

	private synchronized boolean checkIfConnectionPoolIsFull()
	{
//		int maxPoolSize = Integer.parseInt(SATAProps.getProperty(PROP_SATA_DB_MAXPOOLSIZE));
		int maxPoolSize = Integer.parseInt(SATAPropertyLoader.getProperty(PROP_SATA_DB_MAXPOOLSIZE));
		
		//Check if the pool size
		if(connectionPool.size() < maxPoolSize){
			return false;
		}

		return true;
	}

	//Creating a connection
	public Connection createNewConnectionForPool()
	{
		Connection connection = null;

		try
		{
			connection = DriverManager.getConnection(databaseUrl, userName, password);
			System.out.println("Connection: "+connection);
		}
		catch(SQLException sqle)
		{
			System.err.println("SQLException: "+sqle);
			return null;
		}

		return connection;
	}

	public synchronized Connection getConnectionFromPool()
	{
		Connection connection = null;

		//Check if there is a connection available. There are times when all the connections in the pool may be used up
		if(connectionPool.size() > 0)
		{
			connection = (Connection) connectionPool.get(0);
			connectionPool.remove(0);
		}
		//Giving away the connection from the connection pool
		return connection;
	}

	public synchronized void returnConnectionToPool(Connection connection)
	{
		//Adding the connection from the client back to the connection pool
		connectionPool.add(connection);
	}

//	public static void main(String args[])
//	{
//		ConnectionPoolManager ConnectionPoolManager = new ConnectionPoolManager();
//	}

}