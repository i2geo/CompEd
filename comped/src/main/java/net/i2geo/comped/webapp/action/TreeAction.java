/**
 * 
 */
package net.i2geo.comped.webapp.action;


import java.util.Locale;

import net.i2geo.comped.service.CompetencyITreeManager;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.TopicITreeManager;
import net.i2geo.comped.service.impl.CompetencyITreeManagerImpl;
import net.i2geo.comped.service.impl.TopicITreeManagerImpl;
import net.i2geo.comped.util.UserUtil;

import org.appfuse.model.User;
import org.appfuse.webapp.action.BaseAction;
import org.springframework.security.AccessDeniedException;

import com.jenkov.prizetags.tree.itf.ITree;

/**
 * @author Martin Homik
 *
 */
public class TreeAction extends BaseAction {
	
	private static final long serialVersionUID = 5546628460041310223L;

	// Managers
	private CompetencyProcessManager cpm;
	private AbstractTopicManager tgm;
	
	
	// Tree information for creating the competency process hierarchy
	private ITree tree;
	private String type;
	
	private boolean individuals;
	
	/*******************************
     * Set manager beans
     *******************************/
	
	public void setCompetencyProcessManager(CompetencyProcessManager cpm) {
		this.cpm = cpm;
	}

	public void setAbstractTopicManager(AbstractTopicManager tgm) {
		this.tgm = tgm;
	}
	
	// access method
	@Override
	public String execute() {
		if (tree == null) {
			if (type.equals("competency")) {
				CompetencyITreeManager iTreeManager = new CompetencyITreeManagerImpl(cpm);
				iTreeManager.setLocale(getCurrentLocale());
				cpm.setLocale(getCurrentLocale());
				tree = iTreeManager.createTree(individuals);
				return SUCCESS;
			}
			
			if (type.equals("topic")) {
				TopicITreeManager iTreeManager = new TopicITreeManagerImpl(tgm);
				iTreeManager.setLocale(getCurrentLocale());
				tgm.setLocale(getCurrentLocale());
				tree = iTreeManager.createTree(individuals);
				return SUCCESS;
			}
		}
		
		return ERROR;
	}

    /***********************************************
     * Getter and setter for ValueStack properties.
     ***********************************************/
	public ITree getTree(){
		return tree;
	}

	public boolean isIndividuals() {
		return individuals;
	}

	public void setIndividuals(boolean individuals) {
		this.individuals = individuals;
	}

	public void setType(String type) {
		this.type = type;
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

