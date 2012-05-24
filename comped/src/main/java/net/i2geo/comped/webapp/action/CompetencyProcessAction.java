/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.appfuse.model.User;

import com.jenkov.prizetags.tree.itf.ITree;
import com.opensymphony.xwork2.Preparable;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.service.CompetencyITreeManager;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.ThingManager;
import net.i2geo.comped.service.impl.CompetencyITreeManagerImpl;
import net.i2geo.comped.util.ActionUtil;
import net.i2geo.comped.util.UserUtil;

/**
 * @author Martin Homik
 * 
 */
public class CompetencyProcessAction extends NameableAction implements
		Preparable {

	private static final long serialVersionUID = -6940752485940680256L;

	// Managers
	private CompetencyProcessManager cpm;
	private ConcreteCompetencyManager ccm;
	private ThingManager thingManager;
	
	// List of all competencies
	protected List<CompetencyProcess> competencies;
	protected List<Competency> allSimilar;

	// Tree information for creating the competency process hierarchy
	private ITree tree = null;

	// current competency
	private CompetencyProcess competency;
	private Long id;
	private String uri;

	private String pStorage = "";

	/*******************************
	 * Set manager beans
	 *******************************/

	public void setCompetencyProcessManager(CompetencyProcessManager cpm) {
		this.cpm = cpm;
		this.cpm.setLocale(getCurrentLocale());
	}

	public void setConcreteCompetencyManager(ConcreteCompetencyManager ccm) {
		this.ccm = ccm;
		this.ccm.setLocale(getCurrentLocale());
	}
	
	public void setThingManager(ThingManager thingManager) {
		this.thingManager = thingManager;
	}

	/*******************************************************************************
	 * Grab the entity from the database before populating with request
	 * parameters.
	 *******************************************************************************/
	public void prepare() {
		if (getRequest().getMethod().equalsIgnoreCase("post")) {
			// prevent failures on new
			String competencyId = getRequest().getParameter("competency.id");
			if (competencyId != null && !competencyId.equals("")) {
				competency = cpm.get(new Long(competencyId), getCurrentLocale().getLanguage());
				setNameable(competency);
				findAllDefaultLangNames();
				findAllOtherLangNames();
                setOldDefaultCommonName(competency.getName());            
			}
		}
	}

	private void mapUriToId() {
		if (uri != null & uri != "") {
			CompetencyProcess c = cpm.findByUri("%#" + uri);
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
	public List<CompetencyProcess> getCompetencies() {
		return competencies;
	}

	// getter for the competency property
	public CompetencyProcess getCompetency() {
		return competency;
	}

	// setter for competency
	public void setCompetency(CompetencyProcess competency) {
		this.competency = competency;
	}

    // get all topic suffix ids
    // TODO: should go to manager
	public String getPStorage() {
   		pStorage = ActionUtil.cCollection2String(competency.getParent());
    	log.debug("pStorage (GET): " + pStorage);
		return pStorage;
	}

	
	
    public void setPStorage(String pStorage) {
    	log.debug("PSTORE IS (SET): " + pStorage);
    	this.pStorage = ActionUtil.correctSKBInput(pStorage);
    	log.debug("PSTORE IS (SET-NEW): " + this.pStorage);    	
    }
        
	// get all similar competencies
    @SuppressWarnings("unchecked")
	public List<Competency> getAllSimilar() {
    	if (allSimilar == null || allSimilar.isEmpty()) {
    		Collection c = cpm.mapToLocale(competency.getAllSimilar(), getCurrentLocale().getLanguage());
    		allSimilar = new ArrayList<Competency>();
    		allSimilar.addAll(c);
    	}
    	
    	return allSimilar;
    }

	// getter for competency itree
	public ITree getTree() {
		if (tree == null) {
			CompetencyITreeManager iTreeManager = new CompetencyITreeManagerImpl(cpm);
			iTreeManager.setLocale(getCurrentLocale());
			cpm.setLocale(getCurrentLocale());
			tree = iTreeManager.createTree(competency, true, false);
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
	 * 
	 * @return success value
	 */
	public String delete() {
		cpm.detach(competency);
		String[] args = { getText("item.process") };
		saveMessage(getText("interaction.deleted", args));

		return "delete";
	}

	/**
	 * Edit a competency. Returns a success value.
	 * 
	 * @return success value
	 */
	public String edit() {
		mapUriToId();

		if (id != null) {
			competency = cpm.get(id, getCurrentLocale().getLanguage());
		} else {
			competency = new CompetencyProcess();
			CompetencyProcess root = cpm.getRootCompetencyProcess();
			log.debug("*** ROOT: " + root);
			competency.addParent(root);
;		}
		setNameable(competency);
		return SUCCESS;
	}

	/**
	 * Save a competency. Returns a success value.
	 * 
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
    		addFieldError("competency.uri", getText("errors.unique.uri"));
    	}

    	if(competency.getUriSuffix().isEmpty()) {
    		addFieldError("competency.uri", getText("errors.no.uri"));
    	}
    	
    	if (!ActionUtil.isAlphaNumeric(competency.getUriSuffix())) {
    			addFieldError("competency.uri", getText("errors.uri.alphaNumeric"));
    	}
    	
    	
    	if (competency.getName().isEmpty()) {
    		addFieldError("competency.name", getText("errors.no.name"));
    	}
    	
		competency = cpm.prepare(competency);
		// set current user within the session
		competency = setCurrentUser(competency);

        // find and set topics by URI suffix
        List<CompetencyProcess> parents = cpm.findByUriSuffix(pStorage);

       	getSession().setAttribute("getBackAction", "showProcess");
       	getSession().setAttribute("getBackId", competency.getId());
       	
        if (parents.isEmpty()) {
        	addFieldError("pStorage", getText("errors.no.process"));
        }
        
        competency.setParent(parents);

        if (hasErrors()) {
    		log.debug("Field errors: " + getFieldErrors());
        	return INPUT;
        }
        
		competency = cpm.save(competency);
		
		String[] args = { getText("item.process") };
		String key = (isNew) ? "interaction.added" : "interaction.updated";
		saveMessage(getText(key, args));

		
       	
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
	 * Show a competency. Returns a success value.
	 * 
	 * @return a success value.
	 * 
	 */
	public String show() {
		mapUriToId();

		if (id != null) {
			competency = cpm.get(id, getCurrentLocale().getLanguage());
			setNameable(competency);
			findAllDefaultLangNames();
			findAllOtherLangNames();
		} else {
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * retrieve current user; could use session info; here, uses security
	 * context This method should go into the BaseAction class in full source
	 * projects
	 * 
	 */
	private CompetencyProcess setCurrentUser(CompetencyProcess competency) {
		User currentUser = null;
		if (competency.getCreator() == null) {
			currentUser = UserUtil.getUserFromSecurityContext();
			// NOTE: sets the creator on current User; should not be inside
			// Action
			competency.setCreator(currentUser);
		}

		return competency;
	}

	/**
	 * List all competency processes. Returns a success value.
	 * 
	 * @return success value
	 */
	public String list() {
		competencies = cpm.getAll(getCurrentLocale());
		return SUCCESS;
	}

	/**
	 * List all competency process alphabetically. Returns a success value.
	 * 
	 * @return success value
	 */
	public String listAlphabetically() {
		competencies = cpm.getAll(getCurrentLocale());
		return SUCCESS;
	}

}
