package sata.domain.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class HibernateUtil implements IConstants {
	private static final SessionFactory sessionFactory;  
    
    private static Session session;  
  
    static {  
    	String ambiente = SATAPropertyLoader.getProperty(PROP_SATA_AMBIENTE);
        sessionFactory = new Configuration().configure("conf/hibernate.cfg."+ambiente+".xml").buildSessionFactory();  
    }  
  
    public static Session getSession() {  
        if (session == null) {
        	System.out.println("Criando nova sessao Hibernate...");
        	session = sessionFactory.openSession();
        }
        return session;
    }  
  
    public static void closeCurrentSession() {
    	if (session != null) {
    		System.out.println("Fechando sessao Hibernate...");
    		session.close();
    		session = null;
    	}
    }  
  
    public static Session currentSession() {  
        return session;  
    }  
      
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    }  
}
