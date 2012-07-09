/**
 * 
 */
package net.i2geo.onto.updates.actions;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.semanticweb.owl.model.*;

/**
 * @author Arndt Faulhaber
 * @author Martin Homik
 *
 */
public abstract class OntologyActionBase {    

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    private Element element = null;
    private String elementId = null;
    private String elementClass = null; 
    private List<Element> subelements = null;


	/**
     * Perform the action, whatever it does...
     */
    public abstract void perform() throws OWLOntologyChangeException;

    public void setElement(Element element) {
    	this.element = element;
    }
    
    public Element getElement() {
    	return element;
    }
    
    /**
     * @return the elementClass
     */
    public String getElementClass() {
        return elementClass;
    }

    /**
     * @param elementClass the elementClass to set
     */
    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

    /**
     * Get the ID of the Element at stake
     * @return the elementId
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * @param elementId the elementId to set
     */
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
    
    /**
     * Get the List of subelements
     * @return the subelements
     */
    public List<Element> getSubelements() {
        return subelements;
    }

    /**
     * @param subelements the subelements to set
     */
    public void setSubelements(List<Element> subelements) {
        this.subelements = subelements;
    }
    
    
}
