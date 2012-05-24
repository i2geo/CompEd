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
public class ReadOnlyAction extends BaseAction {
	
	// managers
	// set managers    
	
	/**
	 * Disable writing/storage.
	 *
	 * @return SUCCESS if read-only mode is established.
	 */
	public String set() {

		if (!getRequest().getRemoteAddr().equals("127.0.0.1")) { 
			return ERROR;
		}
				
		saveMessage(getText("readOnly.set"));
        return SUCCESS;
	}

	
	/**
	 * 
	 * @return SUCCESS if write permissions are allowed again.
	 */
	public String unset() {
		if (!getRequest().getRemoteAddr().equals("127.0.0.1")) { 
			return ERROR;
		}

		saveMessage(getText("readOnly.unset"));
        return SUCCESS;
	}

    /***********************************************
     * Utility functions to get the cirrent locale.
     ***********************************************/    
}
