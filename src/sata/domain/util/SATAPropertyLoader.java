package sata.domain.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class SATAPropertyLoader implements IConstants
{
    
	private static final boolean THROW_ON_LOAD_FAILURE = true;
    private static final boolean LOAD_AS_RESOURCE_BUNDLE = true;
    private static final String SUFFIX = ".properties";
    
    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extension. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     * 
     * @param name classpath resource name [may not be null]
     * @param loader classloader through which to load the resource [null
     * is equivalent to the application loader]
     * 
     * @return resource converted to java.util.Properties [may be null if the
     * resource was not found and THROW_ON_LOAD_FAILURE is false]
     * @throws IllegalArgumentException if the resource was not found and
     * THROW_ON_LOAD_FAILURE is true
     */
    public static Properties loadProperties (String name, ClassLoader loader)
    {
        if (name == null)
            throw new IllegalArgumentException ("null input: name");
        
        if (name.startsWith ("/"))
            name = name.substring (1);
            
        if (name.endsWith (SUFFIX))
            name = name.substring (0, name.length () - SUFFIX.length ());
        
        Properties result = null;
        
        InputStream in = null;
        try
        {
            if (loader == null) loader = ClassLoader.getSystemClassLoader ();
            
            if (LOAD_AS_RESOURCE_BUNDLE)
            {    
                name = name.replace ('/', '.');
                // Throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle (name,
                    Locale.getDefault (), loader);
                
                result = new Properties ();
                for (Enumeration<String> keys = rb.getKeys (); keys.hasMoreElements ();)
                {
                    final String key = (String) keys.nextElement ();
                    final String value = rb.getString (key);
                    
                    result.put (key, value);
                } 
            }
            else
            {
                name = name.replace ('.', '/');
                
                if (! name.endsWith (SUFFIX))
                    name = name.concat (SUFFIX);
                
                //primeiro tenta ler do arquivo fora da aplicacao, no diretorio raiz
                in = new FileInputStream(new File(name));                 
                if (in == null){ //segundo tenta ler o arquivo de dentro do classpath da aplicacao
                	in = loader.getResourceAsStream (name); 
                }
                if (in != null){
                	result = new Properties ();
                	result.load (in); // Can throw IOException
                }
            }
        }
        catch (Exception e)
        {
            result = null;
        }
        finally
        {
            if (in != null) try { in.close (); } catch (Throwable ignore) {}
        }
        
        if (THROW_ON_LOAD_FAILURE && (result == null))
        {
            throw new IllegalArgumentException ("could not load [" + name + "]"+
                " as " + (LOAD_AS_RESOURCE_BUNDLE
                ? "a resource bundle"
                : "a classloader resource"));
        }
        
        return result;
    }
    
    public static String getProperty(String name) {
    	ResourceBundle rb = ResourceBundle.getBundle("conf/sata-conf");

    	try {
    		return rb.getString(name);

    	} catch (MissingResourceException e) {
        	return rb.getString(name + rb.getString(PROP_SATA_AMBIENTE));
    	}
    }
    
    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     */
    public static Properties loadProperties (final String name)
    {
    	ClassLoader cl = Thread.currentThread ().getContextClassLoader();
    	cl.getResourceAsStream("/auth.properties");
    	return loadProperties (name,
            Thread.currentThread ().getContextClassLoader());
    }
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties SATAProps = SATAPropertyLoader.loadProperties(IConstants.ARQ_SATA_CONF);
		System.out.println(IConstants.PROP_CANDLE_PCTGEMPAVIOGRANDE + ": " + SATAProps.getProperty(IConstants.PROP_CANDLE_PCTGEMPAVIOGRANDE));
	}
	
}
