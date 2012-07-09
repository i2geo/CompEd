/**
 * 
 */
package net.i2geo.onto.parse;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

import net.i2geo.onto.updates.actions.*;

/**
 * @author afaulhab
 *
 */
public class OntoUpdateParser
{
    private static Logger log = LogManager.getLogger(OntoUpdateParser.class);
    private static final Namespace ontoUpdateNS = Namespace
            .getNamespace("http://www.inter2geo.eu/2008/ontology/ontoUpdates.owl#"),
            rdfNS = Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

    /**
     * Parse an XML-string and create action objects from those
     * @param changeDoc - A JDOM Document containing an XML ontology change request
     * @return A List of Actions from the xmlDocument
     * @throws ActionCreationException 
     */
    public OntoUpdateSession parseActions(Document changeDoc) throws ActionCreationException {
    	
    	Element root = changeDoc.getRootElement();
        OntoUpdateSession updSession = new OntoUpdateSession();

        parseSession(root, updSession);
        parseChangeInstructions(root, updSession);
        
        return updSession;
    }

	@SuppressWarnings("unchecked")
	private void parseChangeInstructions(Element root, OntoUpdateSession updSession) {
		List<Element> children = root.getChildren();
        for (Element actionType : children) {
            // sanity check
            if (actionType.getChildren().size() == 0)
                continue;
            
            OntologyActionBase action;
            
            List<Element> items = actionType.getChildren();
            for (Element item : items) {
            
            	// sanity check
            	if (item == null) {
            		// TODO: Add to error list 
            		continue;
            	}
            
            	// create the appropriate new action
            	if (actionType.getName().equals("Additions")) {
            		action = new OntoAddAction();
            	} else if (actionType.getName().equals("Updates")) {
            		action = new OntoChangeAction();
            	} else if (actionType.getName().equals("Deletions")) {
            		action = new OntoDeleteAction();
            	} else if (actionType.getName().equals("ClassChange")) {
            		OntoClassChangeAction classChangeAction =
            			new OntoClassChangeAction();
            		classChangeAction.setNewClass(item.getValue());
            		action = classChangeAction;
            	} else if (actionType.getName().equals("NameChange")) {
            		OntoNameChangeAction nameChangeAction =
            			new OntoNameChangeAction();
            		nameChangeAction.setNewName(item.getValue());
            		action = nameChangeAction;
            	} else {
            		continue;
            	}

            	// Extract values of the element
            	String id = item.getAttributeValue("ID", rdfNS);
            	if (id == null) {
            		id=item.getAttributeValue("about", rdfNS);
            	}
            	
            	String elementClass;
            	
            	// TODO: this is sort of dirty that I have to check for a "gs" namespace
            	// but otherwise there will be a namespace problem in the reasoner 
            	// which will be visible to the outside
            	if (item.getNamespacePrefix().equals("gs")) {
            		 elementClass = item.getName();
            	} else {
            		// it is an OWL class
            		elementClass = item.getQualifiedName();
            	}
            
            	List<Element> subElements = new ArrayList<Element>();

            	for (Object subElement : item.getChildren()) {
            		subElements.add((Element) subElement);
            	}

            	// set the element for usage in perform()
            	action.setElement(item);
            	
            	// set the element Class
            	action.setElementClass(elementClass);
            	// set the element ID
            	action.setElementId(id);
            	// set the subElements
            	action.setSubelements(subElements);

            	updSession.addAction(action);
            }
        }
	}

	private void parseSession(Element root, OntoUpdateSession updSession) throws ActionCreationException {
		// SESSION: this one is mandatory ********
        Element sessionElem = root.getChild("Session", ontoUpdateNS);
        if (sessionElem == null) {
            log.error("No session information in XML!!!");
            throw new ActionCreationException("No session information in XML!!!", null);
        }

        updSession.setActionId(sessionElem.getChild("Name", ontoUpdateNS).getValue());

        try {
            updSession.setNotificationURL(sessionElem.getChild("ServerResponse", ontoUpdateNS)
                    .getValue());
        }
        catch (Exception ex) {
            log.error("Malformed responseURL...", ex);
            //            return null; //possibly throw exception
        }
        // end of SESSION part           *********
	}

    public OntoUpdateSession parseActions(String XMLstring)
        throws ActionCreationException
    {
        try {
            // make it jdom
            StringReader is = new StringReader(XMLstring);
            Document changeDoc = new SAXBuilder().build(is);
            return this.parseActions(changeDoc);

        }
        catch (Exception e) {
            throw new ActionCreationException("Cannot parse XML-request", e);
        }
    }
    public OntoUpdateSession parseActions(Reader in)
        throws ActionCreationException
    {
        try {
            // make it jdom
            Document changeDoc = new SAXBuilder().build(in);
            return this.parseActions(changeDoc);

        }
        catch (Exception e) {
            throw new ActionCreationException("Cannot parse XML-request", e);
        }
    }
}
