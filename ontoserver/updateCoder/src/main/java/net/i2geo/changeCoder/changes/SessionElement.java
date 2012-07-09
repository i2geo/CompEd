/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class SessionElement
    extends ChangeRequestBase
{

    /**
     * 
     */
    public SessionElement(String responseURL, String sessionId)
    {
        this.responseURL = responseURL;
        this.id = sessionId;
    }

    private String responseURL = null;
    
    private String id = null;
    
    /**
     * @return the responseURL
     */
    public String getResponseURL()
    {
        return responseURL;
    }
        
    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }
    
    @Override
    protected String convertToXML() {
        String XMLString = "";
        // opening TAG
        XMLString += "<Session>\n";
        
        // add Name - Tag
        XMLString += "  <Name>"
            + getId() + "</Name>\n";
        
        // add ServerResponse - Tag
        XMLString += "  <ServerResponse>"
            + getResponseURL() + "</ServerResponse>\n";
        
        // closing TAG
        XMLString += "</Session>\n";
        
        return XMLString;
    }

    @Override
    public ChangeType getType()
    {
        return ChangeType.Session;
    }
}
