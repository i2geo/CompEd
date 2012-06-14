/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.ArrayList;
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
public class SearchAction extends BaseAction {
	
	private static final long serialVersionUID = 5546628460041310223L;

	// Managers
	private ConcreteCompetencyManager ccm;
	private CompetencyProcessManager cpm;
	private TopicManager tm;
	
	// List of matched concrete competencies
	private List<ConcreteCompetency> ccMatch = new ArrayList<ConcreteCompetency>();
	// List of all matched concrete competencies
	private List<CompetencyProcess> cpMatch = new ArrayList<CompetencyProcess>();

	// List of all matched topics
	private List<Topic> tMatch = new ArrayList<Topic>();

	private String searchString = "";

	// set managers    
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
	
	// get matches
	public List<ConcreteCompetency> getCcMatch() {
		return ccMatch;
	}

	public List<CompetencyProcess> getCpMatch() {
		return cpMatch;
	}

	public List<Topic> getTMatch() {
		return tMatch;
	}
	
	public void setSearchString(String searchString) {
		this.searchString = "%" + searchString + "%";
	}
	
	
	public String search() {
		
		if (searchString != null && !searchString.isEmpty()) {
			cpMatch = cpm.findByNameLike(searchString, getLocale().getLanguage());
			// log.debug("cpMatch hat Anzahl Items: " + cpMatch.size());
			ccMatch = ccm.findByNameLike(searchString, getLocale().getLanguage());
			// log.debug("ccMatch hat Anzahl Items: " + ccMatch.size());
			
			tMatch = tm.findByNameLike(searchString, getLocale().getLanguage());

			return SUCCESS;
		}
		
		return INPUT;
	}
	
	@Override
	public Locale getLocale() {
		String language = getCurrentLocale().getLanguage();
		try {
			return new Locale(language, "");
		} catch (Exception e) {
			return super.getLocale();
		}
	}
	
	public Locale getCurrentLocale() {
		// NOTE: should be written in some properties file
		Locale defaultLocale = super.getLocale();
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
