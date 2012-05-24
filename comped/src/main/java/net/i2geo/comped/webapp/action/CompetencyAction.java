/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;

import org.appfuse.model.User;

import com.opensymphony.xwork2.Preparable;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.service.CompetencyManager;
import net.i2geo.comped.service.NameManager;
import net.i2geo.comped.util.UserUtil;


/**
 * @author Martin Homik
 *
 */
public class CompetencyAction extends NameableAction implements Preparable {

	private static final long serialVersionUID = -6940752485940680256L;

	// Managers
	private CompetencyManager cm;
	private NameManager nameManager;
	
	// List of all competencies
	protected List<Competency> competencies;
		
	// current competency
    private Competency competency;
    private Long id;
    private String uri;
	
	/*******************************
     * Set manager beans
     *******************************/
	
	public void setCompetencyManager(CompetencyManager cm) {
		this.cm = cm;
	}
	
	public void setNameManager(NameManager nameManager) {
		this.nameManager = nameManager;
	}

	public NameManager getNameManager() {
		return nameManager;
	}

    /*******************************************************************************
     * Grab the entity from the database before populating with request parameters.
     *******************************************************************************/
    public void prepare() {
    	if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String competencyId = getRequest().getParameter("competency.id");
            if (competencyId != null && !competencyId.equals("")) {
                competency = cm.get(new Long(competencyId));
                setNameable(competency);
                findAllDefaultLangNames();
                findAllOtherLangNames();
            }
        }
    }
    
    private void mapUriToId() {
    	 if (uri != null & uri != "") {
    		Competency c = cm.findByUri("%#" + uri);
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
    public List<Competency> getCompetencies() {
        return competencies;
    }

    // getter for the competency property
    public Competency getCompetency() {
        return competency;
    }

    // setter for competency
    public void setCompetency(Competency competency) {
        this.competency = competency;
    }
       
    
    /*******************************
     * Struts2 action access methods
     *******************************/
   
    /**
     * Delete a competency. Returns a success value.
     * @return success value
     */
    public String delete() {
    	cm.detach(competency);
        saveMessage(getText("competency.deleted"));

        return SUCCESS;
    }
    
	/**
	 * Edit a competency. Returns a success value.
	 * @return success value
	 */
	public String edit() {
    	mapUriToId();

    	if (id != null) {
            competency = cm.get(id);
        } else {
        		competency = new Competency();
        }
        return SUCCESS;
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
        
        competency = cm.prepare(competency);
        // set current user within the session
        competency = setCurrentUser(competency);
        competency = cm.save(competency);

        String key = (isNew) ? "competency.added" : "competency.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }

    /**
     * Show a competency. Returns a success value.
     * @return a success value.
     * 
     */
    public String show() {
    	mapUriToId();
    	
    	if (id != null) {
        	competency = cm.get(id);
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
    private Competency setCurrentUser(Competency competency) {
		User currentUser = null;
        if (competency.getCreator() == null) {
        	currentUser = UserUtil.getUserFromSecurityContext();
        	// NOTE: sets the creator on current User; should not be inside Action
        	competency.setCreator(currentUser);
        }
        
		return competency;
	} 
	
	/**
     * List all competencies. Returns a success value.
	 * @return success value
     */
    public String list() {
        competencies = cm.getAll();
        return SUCCESS;
    }
   
    /**
     * List all competencies alphabetically. Returns a success value.
	 * @return success value
     */
    public String listAlphabetically() {
	    competencies = cm.getAll(getCurrentLocale());
	    return SUCCESS;
	}

}
