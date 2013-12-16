package sata.metastock.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

import sata.domain.util.SATAUtil;

public class HTTPSata {
	
	/**
	 * The POST method. Accepts 2 parameters
	 * 
	 * @param targetURL
	 *            : The URL to POST to.
	 * @param contentHash
	 *            : The hashtable of the paramters to be posted.
	 * 
	 * @return The String returned as a result of POSTing.
	 */
	public static String POST(String targetURL, Hashtable<String, String> contentHash) {
		try {
			if (SATAUtil.isAmbienteDesenvolvimento()) {
				System.setProperty("http.proxyHost", "proxyad.br-petrobras.com.br");
				System.setProperty("http.proxyPort", "9090");
				System.setProperty("https.proxyHost", "proxyad.br-petrobras.com.br");
				System.setProperty("https.proxyPort", "9090");
			}
			
			URL url;
			HttpURLConnection conn;

			// The data streams used to read from and write to the URL connection.
			DataOutputStream out;
			DataInputStream in;

			// String returned as the result of the POST.
			String returnString = "";

			// Create the URL object and make a connection to it.
			url = new URL(targetURL);
			conn = (HttpURLConnection) url.openConnection();

			// Set connection parameters. We need to perform input and output,
			// so set both as true.
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("charset", "UTF-8");

			// Disable use of caches.
			conn.setUseCaches(false);

			// Set the content type we are POSTing. We impersonate it as
			// encoded form data
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// get the output stream to POST to.
			out = new DataOutputStream(conn.getOutputStream());
			String content = "";

			// Create a single String value to be POSTED from the parameters passed
			// to us. This is done by making "name"="value" pairs for all the keys
			// in the Hashtable passed to us.
			Enumeration<String> e = contentHash.keys();
			boolean first = true;
			while (e.hasMoreElements()) {
				// For each key and value pair in the hashtable
				Object key = e.nextElement();
				Object value = contentHash.get(key);
				
				// If this is not the first key-value pair in the hashtable,
				// concantenate an "&" sign to the constructed String
				if (!first) {
					content += "&";
				}
					
				// append to a single string. Encode the value portion
				content += (String) key + "=" + URLEncoder.encode((String) value, "UTF-8");

				first = false;
			}
			
			// Write out the bytes of the content string to the stream.
			out.writeBytes(content);
			out.flush();
			out.close();

			// Read input from the input stream.
			in = new DataInputStream(conn.getInputStream());
			
			// Read the input stream
			Scanner sc = new Scanner(in);
			while(sc.hasNextLine()) {
				returnString += sc.nextLine() + "\n";
			}

			in.close();

			// return the string that was read.
			return returnString;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

}
