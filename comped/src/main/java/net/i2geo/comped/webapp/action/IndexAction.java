/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;
import java.util.Locale;

import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.TopicManager;
import net.i2geo.comped.util.UserUtil;

import org.appfuse.model.User;
import org.appfuse.webapp.action.BaseAction;
import org.springframework.security.AccessDeniedException;

/**
 * @author Martin Homik
 *
 */
public class IndexAction extends BaseAction {
	
	private static final long serialVersionUID = 5546628460041310223L;

	// Managers
	private ConcreteCompetencyManager ccm;
	private CompetencyProcessManager cpm;
		
	private TopicManager tm;
	
	// List of all recently created concrete competencies
	private List<ConcreteCompetency> lastCreatedConcreteCompetencies;
	// List of all recently modified concrete competencies
	private List<ConcreteCompetency> lastModifiedConcreteCompetencies;
	// List of all recently created competency processes
	private List<CompetencyProcess> lastCreatedCompetencyProcesses;
	// List of all recently modified competency processes
	private List<CompetencyProcess> lastModifiedCompetencyProcesses;

	// List of all recently created topics
	private List<Topic> lastCreatedTopics;
	// List of all recently modified topics
	private List<Topic> lastModifiedTopics;
	
	
	// set managers    
	public void setConcreteCompetencyManager(ConcreteCompetencyManager ccm) {
		this.ccm = ccm;
	}

	public void setCompetencyProcessManager(CompetencyProcessManager cpm) {
		this.cpm = cpm;
	}

	public void setTopicManager(TopicManager tm) {
		this.tm = tm;
	}
		
	// get recently created concrete competencies
    public List<ConcreteCompetency> getLastCreatedConcreteCompetencies() {
		return lastCreatedConcreteCompetencies;
	}


	// get recently modified concrete competencies
	public List<ConcreteCompetency> getLastModifiedConcreteCompetencies() {
		return lastModifiedConcreteCompetencies;
	}

	// get recently created competency processes
    public List<CompetencyProcess> getLastCreatedCompetencyProcesses() {
		return lastCreatedCompetencyProcesses;
	}


	// get recently modified competency processes
	public List<CompetencyProcess> getLastModifiedCompetencyProcesses() {
		return lastModifiedCompetencyProcesses;
	}
	
	// get recently created topic items
	public List<Topic> getLastCreatedTopics() {
		return lastCreatedTopics;
	}
	// get recently modified topic items
	public List<Topic> getLastModifiedTopics() {
		return lastModifiedTopics;
	}
		

	// access method
	@Override
	public String execute() {
		ccm.setLocale(getCurrentLocale());
		cpm.setLocale(getCurrentLocale());
		tm.setLocale(getCurrentLocale());
		
		lastCreatedConcreteCompetencies  = ccm.findByLastCreated(5);
		lastCreatedCompetencyProcesses   = cpm.findByLastCreated(5);
		lastModifiedConcreteCompetencies = ccm.findByLastModified(5);
		lastModifiedCompetencyProcesses  = cpm.findByLastModified(5);
		
		lastCreatedTopics  = tm.findByLastCreated(5);
		lastModifiedTopics = tm.findByLastModified(5);
		
		return SUCCESS;
	}
	
    /***********************************************
     * Utility functions to get the cirrent locale.
     ***********************************************/

	public Locale getCurrentLocale() {
		// NOTE: should be written in some properties file
		Locale defaultLocale = getLocale();
		Locale locale = getUsersLocale();
		
		if (locale == null) {
			return defaultLocale;
		}
		return locale;
	}
	
	private Locale getUsersLocale() {
		try {
			User currentUser = UserUtil.getUserFromSecurityContext();
			if (currentUser != null) {
				return new Locale(currentUser.getLanguage());
			}
			return null;
		} catch (AccessDeniedException e) {
			return null;
		}
    }

    
}
