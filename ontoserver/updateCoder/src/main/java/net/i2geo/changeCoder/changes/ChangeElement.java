/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class ChangeElement
    extends ChangeRequestBase
{
    public ChangeElement(String objAsXml) {
        this.setObjAsXML(objAsXml);
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#convertToXML()
     */
    @Override
    protected String convertToXML()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.changes.ChangeRequestBase#getType()
     */
    @Override
    public ChangeType getType()
    {
        return ChangeType.Modification;
    }

}
