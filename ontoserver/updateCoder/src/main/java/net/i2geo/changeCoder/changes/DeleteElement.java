/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class DeleteElement
    extends ChangeRequestBase
{

    private final String toDeleteID;

    public DeleteElement(String elementID)
    {
        toDeleteID = elementID;
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#convertToXML()
     */
    @Override
    protected String convertToXML()
    {
        StringBuffer sb = new StringBuffer();
        // opening TAG with ID
        sb.append("<gs:Thing rdf:ID=\"" + this.toDeleteID + "\" />\n");

        return sb.toString();
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#getType()
     */
    @Override
    public ChangeType getType()
    {
        return ChangeType.Deletion;
    }

}
