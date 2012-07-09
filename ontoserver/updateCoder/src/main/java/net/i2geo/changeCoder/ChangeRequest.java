/**
 * 
 */
package net.i2geo.changeCoder;

import java.io.*;
import java.net.*;
import java.util.*;

import net.i2geo.changeCoder.changes.*;

/**
 * @author afaulhab
 *
 */
public class ChangeRequest
{
    private Set<ChangeRequestBase> changeElements = new HashSet<ChangeRequestBase>();

    private final String destURL;
    
    private String responseMsg = null; 

    private String XMLmsg = null;
    
    
    public ChangeRequest(String responseURL, String sessionID, String destinationURL)
    {
        changeElements.add(new SessionElement(responseURL, sessionID));
        destURL = destinationURL;
    }

    /**
     * Create a new request providing the complete XML request protocol
     * @param XMLMessage
     * @param destinationURL - where to send the request to
     */
    public ChangeRequest(String XMLMessage, String destinationURL) {
        this.XMLmsg = XMLMessage;
        this.destURL = destinationURL;
    }
    
    /**
     * Get the XML change-Request
     * @return A String containing all the change objects encoded
     * in XML change request
     * @throws Exception
     */
    public String getMessage()
        throws Exception
    {
        if (this.XMLmsg == null) {
            return new OntoUpdateEncoder().encode(changeElements);
        } else {
            return this.XMLmsg;
        }
    }

    /**
     * Add an element encoding a change (addition, modification or deletion)
     * @param cElement an Element
     */
    public void addChangeElement(ChangeRequestBase cElement)
    {
        changeElements.add(cElement);
    }

    public boolean sendMessage()
        throws Exception
    {

        // create a HTML-POST request
        URL url = new URL(destURL);
        URLConnection conn = url.openConnection();
        conn.addRequestProperty("Content-Type","application/xml;charset=utf-8");
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
        wr.write(getMessage());
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
     * Get the message from the server (non-decoded)
     * @return The HTML-Response
     */
    public String getResponseMsg() {
        return responseMsg;
    }
    
    /**
     * Get an XML file containing information about the server's response
     * @return
     */
    public String getDecodedServerResponse() {
        // TODO: decode server response
        return null;
    }
    
    /**
     * Parse server reply for possible error messages
     * @param replyMessage - response received from the server
     * @return true iff no Error occurred and message could be
     * interpreted by the server
     */
    private boolean checkError(StringBuffer replyMessage)
    {
        String msg = replyMessage.toString();
        // remember for later use
        responseMsg = msg;
        
        if (msg.matches(".*<title>Ok</title>.*"))
            return true;
        
        return false;
    }
}
