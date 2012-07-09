/**
 * This ontology action class handles updates to the ontology.
 * @author Arndt Faulhaber
 * @author Martin Homik
 * 
 */
package net.i2geo.onto.updates.actions;

import java.net.*;
import java.util.*;

import org.semanticweb.owl.model.OWLOntologyChangeException;

/**
 * @author afaulhab
 *
 */
public class OntoUpdateSession
    extends OntologyActionBase
{

    /**
     * Priority queue that holds a sequence of actions
     */
    private Queue<OntologyActionBase> actionSequence =
        new ArrayDeque<OntologyActionBase>();

    private List<IOntoUpdateError> errorList =
        new ArrayList<IOntoUpdateError>();
    
    /* (non-Javadoc)
     * @see net.i2geo.onto.updates.actions.OntologyActionBase#perform()
     */
    @Override
    public void perform()
        throws OWLOntologyChangeException
    {
        for (OntologyActionBase action : actionSequence) {
            action.perform();
        }

    }

    /**
     * Add an action to the sequence
     * @param action - an action
     */
    public void addAction(OntologyActionBase action) {
        actionSequence.add(action);
    }

    private URL notificationURL = null;

    /**
     * Get the URL, for sending a result notification (Errors,
     *  additional information etc.).
     * 
     * @return An URL, where to send a notification about results
     * of the action
     */
    public URL getNotificationURL() {
        return notificationURL;
    }
    
    public void setNotificationURL(String url) throws MalformedURLException {
        URL u = new URL(url);
        setNotificationURL(u);
    }
    
    
    public void setNotificationURL(URL url) {
        notificationURL = url;
    }
    
    private String initiatorID = null;
    
    /**
     * Who initiated the action
     * @return - an ID of the initiator...
     */
    public String getInitiatorID() {
        return initiatorID;
    }
    
    public void setInitiatorID(String initiator) {
        initiatorID = initiator;
    }
    
    private String actionId = null;
    
    /**
     * Get an ID of the action so it can be referenced in a reply message... (if
     * needed)
     * @return - an ID of the action
     */
    public String getActionId() {
        return actionId;
    }
    
    public void setActionId(String id) {
        actionId = id;
    }
    
}
