/**
 * 
 */
package net.i2geo.changeCoder.changes;

import java.security.InvalidParameterException;


/**
 * @author afaulhab
 *
 */
public class NameChange
    extends ChangeRequestBase
{

    private String oldName;
    private String newName;
    
    private NameChange() {
        
    }
    
    private NameChange(String oldName, String newName) {
        if (oldName == null || oldName.equals("")
                || newName == null || newName.equals("")) {
            throw new InvalidParameterException(
            "Old and new name must be given");
        }
        
        this.oldName = oldName;
        this.newName = newName;
    }
    
    
    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.ChangeElementBase#getType()
     */
    @Override
    public ChangeType getType()
    {
        return ChangeType.Namechange;
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.ChangeElementBase#toXML()
     */
    @Override
    protected String convertToXML()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("gs:Thing rdf:ID=\"");    
        sb.append(oldName);
        sb.append("\">");
        sb.append(newName);
        sb.append("</gs:Thing>\n");
            
        return sb.toString();
    }

}
