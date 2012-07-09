/**
 * 
 */
package net.i2geo.changeCoder;

import java.util.*;

import net.i2geo.changeCoder.changes.*;

/**
 * The purpose of this class is to encode (and decode) update requests
 * to (or from) the ontology server into the appropriate XML format.
 * 
 * @author afaulhab
 *
 */
public class OntoUpdateEncoder
{
    /**
     * Encodes a set of ChangeElements into an XML-Request
     * @param changeElements - a set of ChangeElements
     * @return An XML string encoding a change request
     * @throws Exception 
     */
    public String encode(Set<ChangeRequestBase> changeElements)
        throws Exception
    { // tODO: should be encoding to a writer instead
        StringBuffer result = new StringBuffer();
        // append header
        addHeader(result);

        boolean sessionCheck = false;

        // convert stuff here...
        for (ChangeRequestBase changeElement : changeElements) {
            if (changeElement.getType() == ChangeType.Session) {
                result.append(changeElement.toXML());
                sessionCheck = true;
            }
        }

        if (!sessionCheck) {
            throw new Exception("No SessionElement available!!!");
        }

        boolean activityCheck = false;

        for (ChangeRequestBase changeElement : changeElements) {
            // Addition elements
            if (changeElement.getType() == ChangeType.Addition) {
                result.append("<Additions>\n");
                result.append(changeElement.toXML());
                activityCheck = true;
                result.append("</Additions>\n");
            } else
            // modification elements
            if (changeElement.getType() == ChangeType.Modification) {
                result.append("<Updates>\n");
                result.append(changeElement.toXML());
                activityCheck = true;
                result.append("</Updates>\n");
            } else
            // deletion elements
            if (changeElement.getType() == ChangeType.Deletion) {
                result.append("<Deletions>\n");
                result.append(changeElement.toXML());
                activityCheck = true;
                result.append("</Deletions>\n");
            } else
            // namechange elements
            if (changeElement.getType() == ChangeType.Namechange) {
                result.append("<NameChange>\n");
                result.append(changeElement.toXML());
                activityCheck = true;
                result.append("</NameChange>\n");
            } else
            // class change
            if (changeElement.getType() == ChangeType.Classchange) {
                result.append("<ClassChange>\n");
                result.append(changeElement.toXML());
                activityCheck = true;
                result.append("</ClassChange>\n");
            }
        }

        if (!activityCheck) {
            throw new Exception("Nothing to do...");
        }
        
        // append footer
        addFooter(result);

        return result.toString();
    }

    public boolean decode(String XMLmessage)
    {

        //        String result = "";

        return false;
    }

    private StringBuffer addHeader(StringBuffer message)
    {

        String header = "<ontoUpdates\n"
                + "xmlns:gs=\"http://www.inter2geo.eu/2008/ontology/GeoSkills#\"\n"
                + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
                + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"
                + "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"
                + "xmlns=\"http://www.inter2geo.eu/2008/ontology/ontoUpdates.owl#\"\n" + ">\n";

        message.append(header);

        return message;
    }

    private StringBuffer addFooter(StringBuffer message)
    {
        String footer = "</ontoUpdates>\n";

        message.append(footer);

        return message;

    }
}
