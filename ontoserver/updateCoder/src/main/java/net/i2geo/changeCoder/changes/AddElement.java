/**
 * 
 */
package net.i2geo.changeCoder.changes;

/**
 * @author afaulhab
 *
 */
public class AddElement
    extends ChangeRequestBase
{
    public AddElement(String objAsXml) {
        this.setObjAsXML(objAsXml);
    }
    
//    public AddElement(String competencyURI, Set<String> topicURIs)
//    {
//        // Parameter validity check
//        if (competencyURI == null || competencyURI.equals("")
//                || topicURIs == null || topicURIs.size() < 1) {
//            throw new InvalidParameterException("URI has to be valid" +
//            		", and at least 1 topic must be given");
//        }
//
//        uri = competencyURI;
//        topics = topicURIs;
//        
//    }
//    
//    private String uri = null;
//    private Set<String> topics = null;

    @Override
    protected String convertToXML()
    {
        StringBuffer sb = new StringBuffer();
//        // opening TAG with ID
//        sb.append("<owl:Competency rdf:ID=\"" + this.uri
//                + "\">\n");
//        
//        
//        
//        // closing TAG
//        sb.append("</owl:Competency>\n");
        
        // TODO Auto-generated method stub
        return sb.toString();
    }

    @Override
    public ChangeType getType()
    {
        return ChangeType.Addition;
    }

}
