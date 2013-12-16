package sata.domain.dao.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.context.ManagedSessionContext;

import sata.domain.util.LoggerUtil;

public class HibernateSessionRequestFilter implements Filter {
 
    private SessionFactory sf;
 
    public static final String HIBERNATE_SESSION_KEY = "hibernateSession";
    public static final String END_OF_CONVERSATION_FLAG = "endOfConversation";
 
	public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
 
        org.hibernate.classic.Session currentSession;
 
        // Try to get a Hibernate Session from the HttpSession
        HttpSession httpSession =
                ((HttpServletRequest) request).getSession();
        Session disconnectedSession =
                (Session) httpSession.getAttribute(HIBERNATE_SESSION_KEY);
        
     // Start a new conversation or in the middle?
        if (disconnectedSession == null) {
            LoggerUtil.log(">>> New conversation");
            currentSession = sf.openSession();
            currentSession.setFlushMode(FlushMode.MANUAL);
        } else {
            LoggerUtil.log("< Continuing conversation");
            currentSession = (org.hibernate.classic.Session) disconnectedSession;
        }
 
        try {
            LoggerUtil.log("Binding the current Session");
            ManagedSessionContext.bind(currentSession);
 
            LoggerUtil.log("Starting a database transaction");
            currentSession.beginTransaction();
 
            LoggerUtil.log("Processing the event");
            chain.doFilter(request, response);
 
            LoggerUtil.log("Unbinding Session after processing");
            currentSession = ManagedSessionContext.unbind(sf);
 
            // End or continue the long-running conversation?
            if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null ||
                request.getParameter(END_OF_CONVERSATION_FLAG) != null) {
 
                LoggerUtil.log("Flushing Session");
                currentSession.flush();
 
                LoggerUtil.log("Committing the database transaction");
                currentSession.getTransaction().commit();
 
                LoggerUtil.log("Closing the Session");
                currentSession.close();
 
                LoggerUtil.log("Cleaning Session from HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
 
                LoggerUtil.log("<<< End of conversation");
 
            } else {
 
                LoggerUtil.log("Committing database transaction");
                currentSession.getTransaction().commit();
 
                LoggerUtil.log("Storing Session in the HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);
 
                LoggerUtil.log("> Returning to user in conversation");
            }
 
        } catch (StaleObjectStateException staleEx) {
            LoggerUtil.logError("This interceptor does not implement optimistic concurrency control!");
            LoggerUtil.logError("Your application will not work until you add compensation actions!");
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } catch (Throwable ex) {
            // Rollback only
            try {
                if (sf.getCurrentSession().getTransaction().isActive()) {
                    LoggerUtil.log("Trying to rollback database transaction after exception");
                    sf.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
                LoggerUtil.logError("Could not rollback transaction after exception!");
            } finally {
                LoggerUtil.logError("Cleanup after exception!");
 
                // Cleanup
                if (currentSession != null) {
                	LoggerUtil.log("Unbinding Session after exception");
                	currentSession = ManagedSessionContext.unbind(sf);

                	LoggerUtil.log("Closing Session after exception");
                	currentSession.close();
                }
 
                LoggerUtil.log("Removing Session from HttpSession");
                httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);
 
            } 
 
            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }
 
    }
 
    public void init(FilterConfig filterConfig) throws ServletException {
    	LoggerUtil.setup(HibernateSessionRequestFilter.class);
        LoggerUtil.log("Initializing filter...");
        LoggerUtil.log("Obtaining SessionFactory from static HibernateUtil singleton");
        sf = HibernateUtil.getSessionFactory();
    }
 
    public void destroy() {}
}