/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public abstract class ChangeRequestBase
{

    public abstract ChangeType getType();
    
    private String objAsXML = null;
    private boolean asXML = false;

    /**
     * @return the objAsXML
     */
    public final String getObjAsXML()
    {
        return objAsXML;
    }

    /**
     * @return the asXML
     */
    public final boolean isAsXML()
    {
        return asXML;
    }

    /**
     * @param objAsXML the objAsXML to set
     */
    public void setObjAsXML(String objAsXML)
    {
        this.objAsXML = objAsXML;
        asXML = true;
    }
    
    /**
     * Creates an XML-snippet from the ChangeElement
     * @return
     */
    public final String toXML() {
        if (this.isAsXML())
            return getObjAsXML();
        else
            return convertToXML();
    }
    
    protected abstract String convertToXML();
    
}
