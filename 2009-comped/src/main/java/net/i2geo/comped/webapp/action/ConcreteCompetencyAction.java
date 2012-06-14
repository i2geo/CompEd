/**
 * 
 */
package net.i2geo.comped.webapp.action;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.appfuse.model.User;

import com.jenkov.prizetags.tree.itf.ITree;
import com.opensymphony.xwork2.Preparable;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.CompetencyITreeManager;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.ThingManager;
import net.i2geo.comped.service.TopicManager;
import net.i2geo.comped.service.impl.CompetencyITreeManagerCacheProxyImpl;
import net.i2geo.comped.util.ActionUtil;
import net.i2geo.comped.util.UserUtil;

/**
 * @author Martin Homik
 *
 */
public class ConcreteCompetencyAction extends NameableAction implements Preparable  {

	private static final long serialVersionUID = 7511977112801036320L;

	// Managers
	private ConcreteCompetencyManager ccm;
	private CompetencyProcessManager cpm;
	private TopicManager tm;
	private ThingManager thingManager;
	
	// Tree information for creating the competency process hierarchy
	private ITree tree = null;

	
	// List of all competencies
	protected List<ConcreteCompetency> competencies;
	protected List<Competency> allSimilar;
	protected List<Topic> topics;

	protected String tStorage = "";
	protected String pStorage = "";
	
	// current competency
    private ConcreteCompetency competency;
    private Long id;
    private String uri;
	     
    /********************************
     * Inject manager beans
     ********************************/
	public void setConcreteCompetencyManager(ConcreteCompetencyManager ccm) {
		this.ccm = ccm;
		this.ccm.setLocale(getCurrentLocale());
	}

	public void setCompetencyProcessManager(CompetencyProcessManager cpm) {
		this.cpm = cpm;
		this.cpm.setLocale(getCurrentLocale());
	}

	public void setTopicManager(TopicManager tm) {
		this.tm = tm;
		this.tm.setLocale(getCurrentLocale());
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
            String competencyId = getRequest().getParameter("competency.id");
            if (competencyId != null && !competencyId.equals("")) {
                competency = ccm.get(new Long(competencyId), getCurrentLocale().getLanguage());
                setNameable(competency);
                findAllDefaultLangNames();
                findAllOtherLangNames();
                setOldDefaultCommonName(competency.getName());            }
        }
    }
    
    private void mapUriToId() {
    	 if (uri != null & uri != "") {
    		ConcreteCompetency c = ccm.findByUri("%#" + uri);
    		if (c != null) {
    			setId(c.getId());
    		}
    	 }
    }
	
    /***********************************************
     * Getter and setter for ValueStack properties.
     ***********************************************/

    // setter for id
    public void setId(Long id) {
        this.id = id;
    }

    public void setUri(String uri) {
    	this.uri = uri;
    }
    
    // getter for the competencies property
    public List<ConcreteCompetency> getCompetencies() {
    	return competencies;
    }

    // get all similar competencies
    @SuppressWarnings("unchecked")
	public List<Competency> getAllSimilar() {
    	if (allSimilar == null || allSimilar.isEmpty()) {
    		Collection c = ccm.mapToLocale(competency.getAllSimilar(), getCurrentLocale().getLanguage());
    		allSimilar = new ArrayList<Competency>();
    		allSimilar.addAll(c);
    	}
    	
    	return allSimilar;
    }

    // get all topics 
    // TODO: should go to manager
    @SuppressWarnings("unchecked")
	public List<Topic> getTopics() {
    	if (topics == null || topics.isEmpty()) {
    		Collection c = tm.mapToLocale(competency.getTopics(), getCurrentLocale().getLanguage());
    		topics = new ArrayList<Topic>(c);
    	}
    	
    	return topics;
    }

    // get all topic suffix ids
    // TODO: should go to manager
	public String getTStorage() {
   		tStorage = ActionUtil.tCollection2String(competency.getTopics());
    	log.debug("tStorage (GET): " + tStorage);
		return tStorage;
	}

		
    public void setTStorage(String tStorage) {
    	log.debug("TSTORE IS: " + tStorage);
    	this.tStorage = ActionUtil.correctSKBInput(tStorage);
    	log.debug("TSTORE IS (NEW): " + tStorage);    	
    }
    
    // get all topic suffix ids
	public String getPStorage() {
   		pStorage = ActionUtil.cCollection2String(competency.getProcesses());
    	log.debug("pStorage (GET): " + pStorage);
		return pStorage;
	}

    public void setPStorage(String pStorage) {
    	log.debug("PSTORE IS (SET): " + pStorage);
    	this.pStorage = ActionUtil.correctSKBInput(pStorage);
    	log.debug("PSTORE IS (SET-NEW): " + this.pStorage);    	
    }

    // getter for the competency property
    public ConcreteCompetency getCompetency() {
    	return competency;
    }

    // setter for competency
    public void setCompetency(ConcreteCompetency competency) {
    	this.competency = competency;
    }
    
	// getter for competency itree
	public ITree getTree() {
		if (tree == null) {
			CompetencyITreeManager iTreeManager = new CompetencyITreeManagerCacheProxyImpl(cpm);
			iTreeManager.setLocale(getCurrentLocale());
			cpm.setLocale(getCurrentLocale());
			tree = iTreeManager.createTree(competency);
			if (tree == null || tree.getRoot() == null) {
				 log.warn("CP Action: There is no ITree for competency " + competency);
			} 
		}

		return tree;
	}

    /*******************************
     * Struts2 action access methods
     *******************************/
   
    /**
     * Delete a competency. Returns a success value.
     * @return success value
     */
    public String delete() {
    	ccm.detach(competency);
    	String[] args = {getText("item.competency")};
        saveMessage(getText("interaction.deleted", args));

        return "delete";
    }
    
	/**
	 * Edit a competency. Returns a success value.
	 * @return success value
	 */
	public String edit() {
    	mapUriToId();
    	
    	if (id != null) {
            competency = ccm.get(id, getCurrentLocale().getLanguage());
        } else {
        	competency = new ConcreteCompetency();
        }

    	setNameable(competency);
    	
        return SUCCESS;
    }

	/**
	 * Overriding the cancel method - return back to main menu
	 */
    @Override
    public String cancel() {
        boolean isNew = (competency.getId() == null);
       	if (isNew) {
           	getSession().setAttribute("getBackAction", "mainMenu");
        }
    	
    	return CANCEL;
    }
	
    /**
     * Save a competency. Returns a success value.
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

    	boolean isNew = (competency.getId() == null);
    	
    	// set nameable object
    	setNameable(competency);
    	// check if the default common name has changed
    	handleDefaultCommonNameChange(isNew);
    	
    	// check if url is unique
    	if (!thingManager.isUnique(competency)) {
    		log.debug("NOT A UNIQUE URL");
    		addFieldError("competency.uri",getText("errors.unique.uri"));
    	}

    	log.debug("*** URI SUFFIX: " + competency.getUriSuffix());
    	if (competency.getUriSuffix().isEmpty()) {
    		addFieldError("competency.uri",getText("errors.no.uri"));    		
    	}
    	
    	if (competency.getName().isEmpty()) {
       		addFieldError("competency.name",getText("errors.no.name"));   		
    	}

    	if (!ActionUtil.isAlphaNumeric(competency.getUriSuffix())) {
			addFieldError("competency.uri", getText("errors.uri.alphaNumeric"));
    	}

        // set default values
        competency = ccm.prepare(competency);
        // set current user within the session
        competency = setCurrentUser(competency);
        // find and set topics by URI suffix    
        Set<Topic> topics = new HashSet<Topic>(tm.findByUriSuffix(tStorage));
        
        if (topics.isEmpty()) {
        	addFieldError("tStorage", getText("errors.no.topic"));
        }

        
        // log.debug("****************** SAVING TOPICS (tStorage): " + tStorage);
        // log.debug("****************** SAVING TOPICS: " + topics);
        competency.setTopics(topics);

        // find and set processes by URI suffix
        List<CompetencyProcess> cps = cpm.findByUriSuffix(pStorage);
        log.debug("CPS (SAVE (CPS): " + cps);
        competency.setProcesses(cps);

        if (cps.isEmpty()) {
        	addFieldError("pStorage", getText("errors.no.process"));
        }

        // set session atrtibutes just for safety to know where to go back
    	getSession().setAttribute("getBackAction", "showCompetency");
    	getSession().setAttribute("getBackId", competency.getId());

    	// go back to input when errors occurred
    	if (hasErrors()) {
    		log.debug("Field errors: " + getFieldErrors());
    		return INPUT;
    	}

        competency = ccm.save(competency);

    	String[] args = {getText("item.competency")};
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
        	competency = ccm.get(id, getCurrentLocale().getLanguage());
        	setNameable(competency);
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
    private ConcreteCompetency setCurrentUser(ConcreteCompetency competency) {
		User currentUser = null;
        if (competency.getCreator() == null) {
        	currentUser = UserUtil.getUserFromSecurityContext();
        	// NOTE: sets the creator on current User; should not be inside Action
        	competency.setCreator(currentUser);
        }
        
		return competency;
	} 
	

	/*******************************
     * Struts2 action access methods
     *******************************/
	/**
     * List all competencies. Returns a success value.
	 * @return success value
     */
    public String list() {
        competencies = ccm.getAll(getCurrentLocale());
        return SUCCESS;
    }
   
    /**
     * List all competencies alphabetically. Returns a success value.
	 * @return success value
     */
    public String listAlphabetically() {
	    competencies = ccm.getAll(getCurrentLocale());
	    return SUCCESS;
	}

}
