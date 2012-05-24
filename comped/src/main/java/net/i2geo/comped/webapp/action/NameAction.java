/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.NameManager;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.ConcreteTopicManager;
import net.i2geo.comped.util.UserUtil;

import org.appfuse.model.User;
import org.appfuse.webapp.action.BaseAction;
import org.springframework.security.AccessDeniedException;

import com.opensymphony.xwork2.Preparable;

/**
 * @author Martin Homik
 *
 */
public class NameAction extends BaseAction implements Preparable {
	
	private static final long serialVersionUID = 5530526190776195358L;

	private Map<String, String> nameTypes = null;
	
	// Managers
	private NameManager nameManager;

	private ConcreteCompetencyManager ccm;
	private CompetencyProcessManager cpm;
	private AbstractTopicManager tgm;
	private ConcreteTopicManager tim;
	
	// List of all names
	private List<Name> names;

	// current name
    private Name name;
    private Long id;
    
    
	public void setNameManager(NameManager nameManager) {
		this.nameManager = nameManager;
	}
    
	public void setConcreteCompetencyManager(ConcreteCompetencyManager concreteCompetencyManager) {
		this.ccm = concreteCompetencyManager;
	}
	
	public void setCompetencyProcessManager(CompetencyProcessManager competencyProcessManager) {
		this.cpm = competencyProcessManager;
	}
	
	public void setAbstractTopicManager(AbstractTopicManager tgm) {
		this.tgm = tgm;
	}

	public void setConcreteTopicManager(ConcreteTopicManager tim) {
		this.tim = tim;
	}

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
    	if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String nameId = getRequest().getParameter("name.id");
            if (nameId != null && !nameId.equals("")) {
                name = nameManager.get(new Long(nameId));
            }
        }
    }

    private void createNameTypes() {
    	nameTypes = new HashMap<String,String>();
    	for (String type : Name.TYPE_ARRAY) {
			nameTypes.put(type, getText("name.type." + type.toLowerCase()));
		}
    }
    
    // getter for the names property
    public List<Name> getNames() {
        return names;
    }
    
    public Map<String,String> getNameTypes() {
    	if (nameTypes == null) {
    		createNameTypes();
    	}
    	return nameTypes;
    }
    
    // access method to name list
    public String list() {
        names = nameManager.getAll();
        return SUCCESS;
    }
    
    // setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // getter for the name property
    public Name getName() {
        return name;
    }
    
    // setter for name
    public void setName(Name name) {
        this.name = name;
    }
    
    // access method for deleting name
	public String delete() {
    	log.debug("About to delete a name.");
    	
    	if (getSession().getAttribute("getBackAction") != null) {
    		log.debug("I got the getBack value: " + getSession().getAttribute("getBackAction"));
    	} else {
    		log.warn("NO getBack value");
    	}

    	// check because a created URL in competency can only set the id
    	if (name == null && id != null) {
    		log.debug("No name. ID EXISTS!");
    		name = nameManager.get(id);
    	}
    	
    	if (name != null) {
    		log.debug("Now, I have a name.");
    		nameManager.detach(name);
    		
    		String[] args = {getText("item.name")};
    		saveMessage(getText("interaction.deleted", args));

    		return SUCCESS;
    	}
    	
    	log.error("Outch! Something went terribly wrong.");
    	
    	return ERROR;
    }
    
    // access method for editing name
    public String edit() {
        if (id != null) {
            name = nameManager.get(id);
        } else {
            name = new Name();
            name.setDefName(false);
            name.setType(Name.TYPE_COMMON);
            name.setLocale(getCurrentLocale());
        }

        return SUCCESS;
    }

    // access method for saving name
    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = (name.getId() == null);

        name = nameManager.save(name);
        
        String[] args = {getText("item.name")};
        String key = (isNew) ? "interaction.added" : "interaction.updated";
        saveMessage(getText(key, args));

        
        if (isNew) {
        	addToNameable();
        } else {
        	editNameable();
        }

        return SUCCESS;
    }

    // access method to show a competency
    public String show() {
    	if (id != null) {
        	name = nameManager.get(id);
    	} else {
            return ERROR;
        }

        return SUCCESS;
    }
    
    private void editNameable() {
    	
    	// if (name.isDefName() && name.isCommon()) {
    		
    		log.debug("editing name ...");
    		String backAction = (String) getSession().getAttribute("getBackAction");
    		Long backId = (Long) getSession().getAttribute("getBackId");

        	if (backAction == null) { 
        		log.debug("no action ... no fun");
        		return;
        	}
        	
        	if (backAction.equals("showCompetency")) {
        		log.debug("adding name to competency ...");
        		ConcreteCompetency c = ccm.editName(backId, name);
        		ccm.save(c);
        	}

        	if (backAction.equals("showProcess")) {
        		log.debug("adding name to competency process ...");
        		CompetencyProcess c = cpm.editName(backId, name);
        		cpm.save(c);
        	}

        	if (backAction.equals("showTopic")) {
        		// check if it is a ConcreteTopic
        		if (tim.exists(backId)) {
        			log.debug("adding name to topic ...");
        			ConcreteTopic t = tim.editName(backId, name);
        			tim.save(t);
        		} else {
        			// it is an abstract topic
            		log.debug("adding name to topic group ...");
            		AbstractTopic t = tgm.editName(backId, name);
            		tgm.save(t);        			
        		}
        	}
    	// }
    }
    
    private void addToNameable() {
    	log.debug("adding name ...");
    	String backAction = (String) getSession().getAttribute("getBackAction");
    	Long backId = (Long) getSession().getAttribute("getBackId");

    	
    	if (backAction == null) { 
    		log.debug("no action ... no fun");
    		return;
    	}
    	
    	if (backAction.equals("showCompetency")) {
    		log.debug("adding name to competency ...");
    		ConcreteCompetency c = ccm.addName(backId, name);
    		ccm.save(c);
    	}

    	if (backAction.equals("showProcess")) {
    		log.debug("adding name to competency process ...");
    		CompetencyProcess c = cpm.addName(backId, name);
    		cpm.save(c);
    	}

    	if (backAction.equals("showTopic")) {
    		// check if it is a ConcreteTopic
    		if (tim.exists(backId)) {
        		log.debug("adding name to concrete topic ...");
    			ConcreteTopic t = tim.addName(backId, name);
    			tim.save(t);
    		} else {
    			// it is an abstract topic
        		log.debug("adding name to abstract topic ...");
        		AbstractTopic t = tgm.addName(backId, name);
        		tgm.save(t);
    		}
    	}
    }
    
	public String getCurrentLocale() {
		// NOTE: should be written in some properties file
		String defaultLocale = getLocale().getLanguage();
		String locale = getUsersLocale();
		
		if (locale == null || locale == "") {
			return defaultLocale;
		}
		return locale;
	}
	
	private String getUsersLocale() {
		try {
			User currentUser = UserUtil.getUserFromSecurityContext();
			if (currentUser != null) {
				return currentUser.getLanguage();
			}
			return null;
		} catch (AccessDeniedException e) {
			return null;
		}
    }

}
