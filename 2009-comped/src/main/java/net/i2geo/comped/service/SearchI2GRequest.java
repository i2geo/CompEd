/**
 * 
 */
package net.i2geo.comped.service;

import java.io.*;
import java.net.*;

/**
 * This class simply embeds the details for sending a request to SearchI2G.
 * 
 * @author Arndt Faulhaber
 * @author Martin Homik
 *
 */
public class SearchI2GRequest {
    private final String destURL;
    private String XMLmsg = null;
    
    
    /**
     * Create a new request providing the complete XML request protocol
     * @param XMLMessage
     * @param destinationURL - where to send the request to
     */
    public SearchI2GRequest(String XMLMessage, String destinationURL) {
        this.XMLmsg = XMLMessage;
        this.destURL = destinationURL;
    }
    
    public boolean sendMessage() throws Exception {

        // create a HTML-POST request
        String data = URLEncoder.encode("action", "UTF-8") + "="
                + URLEncoder.encode(XMLmsg, "UTF-8");

        // send the encoded message to supplied URL
        URL url = new URL(destURL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer replyMessage = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            replyMessage.append(line);
        }
        wr.close();
        rd.close();

        // decode response
        // return true iff no error
        return checkError(replyMessage);
        
    }
        
    /**
     * Parse server reply for possible error messages
     * @param replyMessage - response received from the server
     * @return true iff no Error occurred and message could be
     * interpreted by the server
     */
    private boolean checkError(StringBuffer replyMessage) {
        String msg = replyMessage.toString();        
        if (msg.matches(".*<title>Ok</title>.*"))
            return true;
        
        return false;
    }
}
