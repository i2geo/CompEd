/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class ClassChange
    extends ChangeRequestBase
{
    private String oldClassName;
    private String rdfId;
    private String newClassName;
    
    public ClassChange(String oldClassName, String rdfId, String newClassName) {
        this.oldClassName = oldClassName;
        this.newClassName = newClassName;
        this.rdfId = rdfId;
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#convertToXML()
     */
    @Override
    protected String convertToXML()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("gs:" + oldClassName);
        sb.append(" rdf:ID=\"");
        sb.append(rdfId);
        sb.append("\">");
        sb.append(newClassName);
        sb.append("</gs:" + oldClassName + ">\n");
            
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#getType()
     */
    @Override
    public ChangeType getType()
    {
        return ChangeType.Classchange;
    }

}
