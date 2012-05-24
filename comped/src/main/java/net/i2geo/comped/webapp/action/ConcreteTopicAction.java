/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.ConcreteTopicManager;
import net.i2geo.comped.service.ThingManager;
import net.i2geo.comped.service.TopicITreeManager;
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
public class ConcreteTopicAction extends NameableAction implements Preparable {
	
	private static final long serialVersionUID = 5530526190776195358L;

	// Managers
	private ConcreteTopicManager tim;
	private AbstractTopicManager atm;
	private ThingManager thingManager;
	
	// List of all topic items
	private List<ConcreteTopic> topics;

	// current topic item
    private ConcreteTopic topic;
    private Long id;
    private String uri;

    // SKB storage
    private String atStorage;

    // Tree information for creating the topic group hierarchy
	private ITree tree = null;    
    
    /********************************
     * Inject manager beans
     ********************************/
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
                topic = tim.get(new Long(topicId), getCurrentLocale().getLanguage());
                setNameable(topic);
                findAllDefaultLangNames();
                findAllOtherLangNames();
                setOldDefaultCommonName(topic.getName());            
            }
        }
    }

    private void mapUriToId() {
      	 if (uri != null & uri != "") {
      		ConcreteTopic t = tim.findByUri("%#" + uri);
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

	// getter for the topic items property
    public List<ConcreteTopic> getTopics() {
        return topics;
    }
    
    // getter for the topic item property
	public ConcreteTopic getTopic() {
	    return topic;
	}


	// setter for topic item
	public void setTopic(ConcreteTopic topic) {
	    this.topic = topic;
	}

	
    // get all topic suffix ids
    public String getAtStorage() {
   		atStorage = ActionUtil.tCollection2String(topic.getAts());
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
			tree = iTreeManager.createTree(topic);

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
     * List all topic items. Returns a success value.
	 * @return success value
     */
    public String list() {
        topics = tim.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }

    /**
     * List all topic items alphabetically. Returns a success value.
	 * @return success value
     */
    public String listAlphabetically() {
        topics = tim.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }
    
    /**
     * Delete a topic item. Returns a success value.
     * @return success value
     */
    public String delete() {
    	String[] args={getText("item.topic")};
    	
    	tim.detach(topic);
        saveMessage(getText("interaction.deleted", args));

        return "delete";
    }
    
	/**
	 * Edit a topic item. Returns a success value.
	 * @return success value
	 */
    public String edit() {
    	
    	mapUriToId();
    	
        if (id != null) {
            topic = tim.get(id, getCurrentLocale().getLanguage());
        } else {
            topic = new ConcreteTopic();
        }
        setNameable(topic);
        
        return SUCCESS;
    }

    /**
     * Save a topic item. Returns a success value.
     * @return a success value.
     * @throws Exception
     */
    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = (topic.getId() == null);

    	// set nameable object
    	setNameable(topic);
    	// check if the default common name has changed
    	handleDefaultCommonNameChange(isNew);

    	// check if url is unique
    	if (!thingManager.isUnique(topic)) {
    		log.debug("NOT A UNIQUE URL");
    		saveMessage(getText("errors.unique.uri"));
    		return ERROR;
    	}


        topic = tim.prepare(topic);
        // set current user within the session
        topic = setCurrentUser(topic);

        // find and set abstract topics by URI suffix
        List<AbstractTopic> ats = (List<AbstractTopic>) atm.findByUriSuffix(atStorage);
        log.debug("ATS (SAVE (ATS): " + ats);
        topic.setAts(ats);

        topic = tim.save(topic);
        
        String[] args = {getText("item.topic")};
        String key = (isNew) ? "interaction.added" : "interaction.updated";
        saveMessage(getText(key, args));

       	getSession().setAttribute("getBackAction", "showTopic");
       	getSession().setAttribute("getBackId", topic.getId());

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    /**
     * Show a topic item group. Returns a success value.
     * @return a success value.
     * 
     */
    public String show() {
    	
    	mapUriToId();
    	
    	if (id != null) {
        	topic = tim.get(id, getCurrentLocale().getLanguage());
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
    private ConcreteTopic setCurrentUser(ConcreteTopic concreteTopic) {
		User currentUser = null;
        if (concreteTopic.getCreator() == null) {
        	currentUser = UserUtil.getUserFromSecurityContext();
        	// NOTE: sets the creator on current User; should not be inside Action
        	concreteTopic.setCreator(currentUser);
        }
        
		return concreteTopic;
	}        
}
