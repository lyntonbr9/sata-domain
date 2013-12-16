package sata.metastock.ftp;

/*
 * @(#)FtpMdownloadExample.java
 *
 * Copyright (c) 2001-2002 JScape
 * 1147 S. 53rd Pl., Mesa, Arizona, 85206, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JScape. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement you entered into with JScape.
 */

import com.jscape.inet.ftp.*;
import java.io.*;
import java.util.Enumeration;

public class FtpExample extends FtpAdapter {
    private String hostname;
    private String username;
    private String password;
    private String filter;
    
    // perform multiple file download
    public void doDownload(String hostname, String username, String password, String filter) throws FtpException {
        Ftp ftp = new Ftp();
        
        //capture Ftp related events
        ftp.addFtpListener(this);
        ftp.setHostname(hostname);
        ftp.setUsername(username);
        ftp.setPassword(password);
        ftp.connect();
        ftp.setBinary();
        ftp.mdownload(filter);
        File file = ftp.download("teste.txt");

        try {
			FileInputStream f = new FileInputStream(file);
			DataInputStream myInput = new DataInputStream(f);
			
			String thisLine = "";
    
			while ((thisLine = myInput.readLine()) != null){         	
				System.out.println(thisLine);
    	
			}
    	
    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
			
        
        ftp.disconnect();
    }
    
    // captures download event
    public void download(FtpDownloadEvent evt) {
        System.out.println("Downloaded file: " + evt.getFilename());
    }
    
    // captures connect event
    public void connected(FtpConnectedEvent evt) {
        System.out.println("Connected to server: " + evt.getHostname());
    }
    
    // captures disconnect event
    public void disconnected(FtpDisconnectedEvent evt) {
        System.out.println("Disconnected from server: " + evt.getHostname());
    }
    
    
    public static void main(String[] args) {
        String hostname = "201.53.40.173";
        String username = "flaviogc";
        String password = "flatolas";
        String filter = "*.txt";
        try {
            
           
            FtpExample example = new FtpExample();
            example.doDownload(hostname,username,password,filter);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
