/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.ConcreteTopicManager;
import net.i2geo.comped.service.ThingManager;
import net.i2geo.comped.service.TopicITreeManager;
import net.i2geo.comped.service.TopicManager;
import net.i2geo.comped.service.impl.TopicITreeManagerImpl;
import net.i2geo.comped.util.ActionUtil;
import net.i2geo.comped.util.UserUtil;

import org.appfuse.model.User;

import com.jenkov.prizetags.tree.itf.ITree;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Martin Homik
 *
 */
public class TopicAction extends NameableAction implements Preparable {
	
	private static final long serialVersionUID = 5530526190776195358L;
	// Managers
	private ConcreteTopicManager tim;
	private AbstractTopicManager atm;
	private ThingManager thingManager;
	private TopicManager topicManager;
		
	// List of all topics
	private List<? extends Topic> topics;

	// current topic
    private Topic topic;
    private Long id;
    private String uri;

    // SKB storage
    private String atStorage;

    // Tree information for creating the topic group hierarchy
	private ITree tree = null;    
    
    /********************************
     * Inject manager beans
     ********************************/
	public void setTopicManager(TopicManager topicManager) {
		this.topicManager = topicManager;
		this.topicManager.setLocale(getCurrentLocale());
	}

	public void setConcreteTopicManager(ConcreteTopicManager tim) {
		this.tim = tim;
		this.tim.setLocale(getCurrentLocale());
	}

	public void setAbstractTopicManager(AbstractTopicManager atm) {
		this.atm = atm;
		this.atm.setLocale(getCurrentLocale());
	}
	
	public void setThingManager(ThingManager thingManager) {
		this.thingManager = thingManager;
	}

    
    /*******************************************************************************
     * Grab the entity from the database before populating with request parameters.
     *******************************************************************************/
    public void prepare() {
    	if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String topicId = getRequest().getParameter("topic.id");
            if (topicId != null && !topicId.equals("")) {
                topic = topicManager.get(new Long(topicId), getCurrentLocale().getLanguage());
                setNameable(topic);
                findAllDefaultLangNames();
                findAllOtherLangNames();
                setOldDefaultCommonName(topic.getName());            
            }
        }
    }

    private void mapUriToId() {
      	 if (uri != null & uri != "") {
      		Topic t = topicManager.findByUri("%#" + uri);
      		if (t != null) {
      			setId(t.getId());
      		}
      	 }
      }


    /***********************************************
     * Getter and setter for ValueStack properties.
     ***********************************************/
    
    // setter for id (set on ValueStack)
	public void setId(Long id) {
	    this.id = id;
	}
	
	// setter for uri (set on ValueStack)
    public void setUri(String uri) {
    	this.uri = uri;
    }

	// getter for the topics property
    public List<? extends Topic> getTopics() {
        return topics;
    }
    
    // getter for the topic property
	public Topic getTopic() {
	    return topic;
	}


	// setter for topic
	public void setTopic(Topic topic) {
	    this.topic = topic;
	}

    // get all topic suffix ids
    public String getAtStorage() {
   		if (topic instanceof ConcreteTopic) {
   			ConcreteTopic t = (ConcreteTopic) topic;   		
   			atStorage = ActionUtil.tCollection2String(t.getAts());
   		} else {
  			AbstractTopic t = (AbstractTopic) topic;   		
   			atStorage = ActionUtil.tCollection2String(t.getParent());
   		}
    	log.debug("AtStorage (GET): " + atStorage);
		return atStorage;
	}

	
    public void setAtStorage(String atStorage) {
    	log.debug("ATSTORE IS (SET): " + atStorage);
    	this.atStorage = ActionUtil.correctSKBInput(atStorage);
    	log.debug("ATSTORE IS (SET-NEW): " + this.atStorage);    	
    }

	// getter for competency itree
	public ITree getTree() {
		if (tree == null) {
			atm.setLocale(getCurrentLocale());
			TopicITreeManager iTreeManager = new TopicITreeManagerImpl(atm);
			iTreeManager.setLocale(getCurrentLocale());
			if (topic instanceof ConcreteTopic) {
				tree = iTreeManager.createTree((ConcreteTopic) topic);
			} 
			if (topic instanceof AbstractTopic) {
				tree = iTreeManager.createTree((AbstractTopic) topic);
			}
			
			 if (tree == null || tree.getRoot() == null) {
				 log.warn("There is no ITree for topic " + topic);
			 } 
		}

		return tree;
	}

	
	/*******************************
     * Struts2 action access methods
     *******************************/
    
    /**
     * List all topics. Returns a success value.
	 * @return success value
     */
    public String list() {
        topics = topicManager.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }

    /**
     * List all topics alphabetically. Returns a success value.
	 * @return success value
     */
    public String listAlphabetically() {
        topics = topicManager.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }
    
    /**
     * Delete a topic. Returns a success value.
     * @return success value
     */
    public String delete() {
    	String[] args={getText("item.topic")};
    	
    	if (topic instanceof AbstractTopic) {
    		atm.detach((AbstractTopic) topic);
    	}
    	if (topic instanceof ConcreteTopic) {
    		tim.detach((ConcreteTopic) topic);
    	}
        saveMessage(getText("interaction.deleted", args));

        return "delete";
    }
    
	/**
	 * Edit a topic. Returns a success value.
	 * @return success value
	 */
    public String edit() {
    	
    	mapUriToId();
    	
        if (id != null) {
            topic = topicManager.get(id, getCurrentLocale().getLanguage());
        } else {
            topic = new Topic();
        }
        setNameable(topic);
        
        return SUCCESS;
    }

	/**
	 * Overriding the cancel method - return back to main menu
	 */
    @Override
    public String cancel() {
        boolean isNew = (topic.getId() == null);
       	if (isNew) {
           	getSession().setAttribute("getBackAction", "mainMenu");
        }
    	
    	return CANCEL;
    }

    /**
     * Save a topic. Returns a success value.
     * @return a success value.
     * @throws Exception
     */
    public String save() throws Exception {
        boolean isNew = (topic.getId() == null);

        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
        	return delete();
        }

        
    	// set nameable object
    	setNameable(topic);
    	// check if the default common name has changed
    	handleDefaultCommonNameChange(isNew);

    	// check if url is unique
    	if (!thingManager.isUnique(topic)) {
    		log.debug("NOT A UNIQUE URL");
    		addFieldError("topic.uri", getText("errors.unique.uri"));
    	}
    	
    	if (topic.getUriSuffix().isEmpty()) {
    		addFieldError("topic.uri", getText("errors.no.uri"));
    	}
        
    	if (!ActionUtil.isAlphaNumeric(topic.getUriSuffix())) {
			addFieldError("competency.uri", getText("errors.uri.alphaNumeric"));
	}

        // topic = topicManager.prepare(topic);
        // set current user within the session
        topic = setCurrentUser(topic);
        
        
        // find and set abstract topics by URI suffix
        List<AbstractTopic> ats = (List<AbstractTopic>) atm.findByUriSuffix(atStorage);
        log.debug("ATS (SAVE (ATS): " + ats);

        
        boolean isConcreteTopic;
        // save session attribute for forwarding to an appropriate URL
       	
        getSession().setAttribute("getBackAction", "showTopic");
       	getSession().setAttribute("getBackId", topic.getId());

       	
        try {
        	isConcreteTopic = testIsConcreteTopic(ats);        	
        } catch (Exception e) {
        	String errorType = e.getMessage();
        	
        	if (errorType.equals("mixed")) {
        		addActionError(getText("errors.topic.mixed"));
        	}
        	
        	if (errorType.equals("empty")) {
        		addActionError(getText("errors.no.topic"));
        	}
        	
        	return INPUT;
        }
        
    	if (isNew) {
    		if (isConcreteTopic) {
    			topic = ConcreteTopic.createConcreteTopic(topic);
    		} else {
    			topic = AbstractTopic.createAbstractTopic(topic);
    			((AbstractTopic) topic).setType(Topic.TYPE_REPRESENTATIVE);
    		}
    	}


               
       	
        if (topic instanceof ConcreteTopic) {
        	if (ats.isEmpty()) {
        		addFieldError("topic.ats", getText("errors.no.topic"));
        	}

        	if (hasErrors()) {
        		return INPUT;
        	}
        	
        	((ConcreteTopic)topic).setAts(ats);
    		topic = tim.prepare((ConcreteTopic) topic);
            // set current user within the session
        	topic = tim.save((ConcreteTopic) topic);
        }


       	if (topic instanceof AbstractTopic) {
        	if (ats.isEmpty()) {
        		addFieldError("topic.parent", getText("errors.no.topic"));
        	}

        	if (hasErrors()) {
        		return INPUT;
        	}

        	((AbstractTopic)topic).setParent(ats);
    		topic = atm.prepare((AbstractTopic) topic);
            // set current user within the session
            topic = setCurrentUser(topic);
        	topic = atm.save((AbstractTopic) topic);
        }
        
                
        String[] args = {getText("item.topic")};
        String key = (isNew) ? "interaction.added" : "interaction.updated";
        saveMessage(getText(key, args));

    	return SUCCESS;
}

    /**
     * Show a competency. Returns a success value.
     * @return a success value.
     * 
     */
    public String show() {
    	
    	mapUriToId();
    	
    	if (id != null) {
        	topic = topicManager.get(id, getCurrentLocale().getLanguage());
        	setNameable(topic);
        	findAllDefaultLangNames();
        	findAllOtherLangNames();
    	} else {
            return ERROR;
        }

        return SUCCESS;
    }
    

    /**
	 * retrieve current user; could use session info; here, uses security context 
     * This method should go into the BaseAction class in full source projects
     * 
     */
    private Topic setCurrentUser(Topic competency) {
		User currentUser = null;
        if (topic.getCreator() == null) {
        	currentUser = UserUtil.getUserFromSecurityContext();
        	// NOTE: sets the creator on current User; should not be inside Action
        	topic.setCreator(currentUser);
        }
        
		return competency;
	}        

    private boolean testIsConcreteTopic(List<AbstractTopic> ats) throws Exception {
    	
    	boolean allPure = true;
    	boolean allReps = true;
    	
    	// throw exception if ats is empty
    	if (ats.isEmpty()) {
    		throw new Exception("empty");
    	}
    	
    	for (AbstractTopic at : ats) {
    		if (at.getType().equals(AbstractTopic.TYPE_REPRESENTATIVE)) {
    			allPure = false;
    		}
    		if (at.getType().equals(AbstractTopic.TYPE_PURE)) {
    			allReps = false;
    		}	
    	}
    	
    	// test if mixed types
    	if (!(allPure || allReps)) {
    		throw new Exception("mixed");
    	}
    	    		
    	return allPure;
    }

}
