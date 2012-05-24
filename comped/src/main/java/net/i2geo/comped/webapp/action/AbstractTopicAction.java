/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.AbstractTopicManager;
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
public class AbstractTopicAction extends NameableAction implements Preparable {
	
	private static final long serialVersionUID = 5530526190776195358L;

	// Managers
	private AbstractTopicManager tgm;
	private ThingManager thingManager;
	
	// List of all topic groups
	private List<AbstractTopic> topics;

	// current topic group
    private AbstractTopic topic;
    private Long id;
    private String uri;

	// Tree information for creating the topic group hierarchy
	private ITree tree = null;

	private String atStorage;
    
    /********************************
     * Inject manager beans
     ********************************/
	public void setAbstractTopicManager(AbstractTopicManager tgm) {
		this.tgm = tgm;
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
                topic = tgm.get(new Long(topicId), getCurrentLocale().getLanguage());
                setNameable(topic);
                findAllDefaultLangNames();
                findAllOtherLangNames();
                setOldDefaultCommonName(topic.getName());            
            }
        }
    }
    

    private void mapUriToId() {
      	 if (uri != null & uri != "") {
      		AbstractTopic t = tgm.findByUri("%#" + uri);
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

	// getter for the topic groups property
    public List<AbstractTopic> getTopics() {
        return topics;
    }
    
    // getter for the topic group property
	public AbstractTopic getTopic() {
	    return topic;
	}


	// setter for topic group
	public void setTopic(AbstractTopic topic) {
	    this.topic = topic;
	}

	// getter for competency itree
	public ITree getTree() {
		if (tree == null) {
			tgm.setLocale(getCurrentLocale());
			TopicITreeManager iTreeManager = new TopicITreeManagerImpl(tgm);
			iTreeManager.setLocale(getCurrentLocale());
			tree = iTreeManager.createTree(topic,true);

			 if (tree == null || tree.getRoot() == null) {
				 log.warn("There is no ITree for topic " + topic);
			 } 
		}

		return tree;
	}

    // get all topic suffix ids
    // TODO: should go to manager
	public String getAtStorage() {
   		atStorage = ActionUtil.tCollection2String(topic.getParent());
    	log.debug("atStorage (GET): " + atStorage);
		return atStorage;
	}

	
	
    public void setAtStorage(String atStorage) {
    	log.debug("ATSTORE IS (SET): " + atStorage);
    	this.atStorage = ActionUtil.correctSKBInput(atStorage);
    	log.debug("ATSTORE IS (SET-NEW): " + this.atStorage);    	
    }
    	
	/*******************************
     * Struts2 action access methods
     *******************************/
    
    /**
     * List all topic groups. Returns a success value.
	 * @return success value
     */
    public String list() {
        topics = tgm.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }

    /**
     * List all topic groups alphabetically. Returns a success value.
	 * @return success value
     */
    public String listAlphabetically() {
        topics = tgm.getAll(getCurrentLocale().getLanguage());
        return SUCCESS;
    }
    
    /**
     * Delete a topic group. Returns a success value.
     * @return success value
     */
    public String delete() {
    	String[] args={getText("item.topic")};
    	
    	tgm.detach(topic);
        saveMessage(getText("interaction.deleted", args));

        return "delete";
    }
    
	/**
	 * Edit a topic group. Returns a success value.
	 * @return success value
	 */
    public String edit() {
    	
    	mapUriToId();
    	
        if (id != null) {
            topic = tgm.get(id, getCurrentLocale().getLanguage());
        } else {
            topic = new AbstractTopic();
        }
        setNameable(topic);
        
        return SUCCESS;
    }

    /**
     * Save a topic group. Returns a success value.
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

        topic = tgm.prepare(topic);
        // set current user within the session
        topic = setCurrentUser(topic);
        topic.setType(Topic.TYPE_REPRESENTATIVE);

        // find and set abstract topics by URI suffix
        List<AbstractTopic> parents = (List<AbstractTopic>) tgm.findByUriSuffix(atStorage);
        topic.setParent(parents);
        log.debug("ATS (SAVE (ATS): " + parents);        
        
        topic = tgm.save(topic);
        
        String[] args = {getText("item.topic")};
        String key = (isNew) ? "interaction.added" : "interaction.updated";
        saveMessage(getText(key, args));

       	getSession().setAttribute("getBackAction", "showAbstractTopic");
       	getSession().setAttribute("getBackId", topic.getId());
       	
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    /**
     * Show a topic group group. Returns a success value.
     * @return a success value.
     * 
     */
    public String show() {
    	
    	mapUriToId();
    	
    	if (id != null) {
        	topic = tgm.get(id, getCurrentLocale().getLanguage());
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
    private AbstractTopic setCurrentUser(AbstractTopic abstractTopic) {
		User currentUser = null;
        if (abstractTopic.getCreator() == null) {
        	currentUser = UserUtil.getUserFromSecurityContext();
        	// NOTE: sets the creator on current User; should not be inside Action
        	abstractTopic.setCreator(currentUser);
        }
        
		return abstractTopic;
	}        
}
