/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class AddRelation
    extends ChangeRequestBase
{
    public AddRelation(String objAsXml) {
        this.setObjAsXML(objAsXml);
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.ChangeElementBase#convertToXML()
     */
    @Override
    protected String convertToXML()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see net.i2geo.changeCoder.ChangeElementBase#getType()
     */
    @Override
    public ChangeType getType()
    {
        return ChangeType.Addition;
    }

}
