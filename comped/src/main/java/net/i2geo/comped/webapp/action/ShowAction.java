/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.List;

import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.service.ThingManager;

import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.appfuse.webapp.action.BaseAction;

/**
 * This actions looks for an object that has a specific uri. It retrieves its
 * type and redirects the action to the correct one.
 * 
 * Follow annotations documentation at Struts web site. Be careful, convention over 
 * configuration. In this case, the annotations overwrite actions defined in struts.xml.
 * 
 * @author Martin Homik
 *
 */
@Results({
	@Result(name="cancel", value="mainMenu", type=ServletActionRedirectResult.class),
	@Result(name="success", value="${#session.showActionName}", type=ServletActionRedirectResult.class,
			params={"actionName", "${#session.showActionName}", "uri", "${#session.showUri}"})
})
public class ShowAction extends BaseAction {
	
	private static final long serialVersionUID = 5345327923220894932L;

	private ThingManager thingManager;

	private String uri;
	private String actionName;
	
	public void setThingManager(ThingManager thingManager) {
		this.thingManager = thingManager;
	}
	
	public String execute() {
		log.debug("--- handling SHOW ---");
		
		if (uri == null || uri.isEmpty()) { log.debug("CANCEL"); return CANCEL;}
		
		if (uri.endsWith("_r")) {
			uri = uri.substring(0, uri.length()-2);
			setUri(uri);
		}
		
		List<Thing> things = thingManager.findByUri(uri);
		log.debug("--- thingManager.findByUri ---");
		
		if (things.isEmpty()) { log.debug("CANCEL"); return CANCEL; }
		
		Thing thing = things.get(0);
		log.debug("--- things.get() ---");
		
		if (thing instanceof ConcreteCompetency) {
			setActionName("showCompetency");
			log.debug("*** Concrete Competency ***");
		}

		if (thing instanceof CompetencyProcess) {
			setActionName("showProcess");
			log.debug("*** Competency Process ***");
		}

		if (thing instanceof ConcreteTopic) {
			setActionName("showTopic");
			log.debug("*** Concrete Topic ***");
		}

		if (thing instanceof AbstractTopic) {
			setActionName("showTopic");
			log.debug("*** AbstractTopic ***");
		}

		return SUCCESS;
	}

	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
		getSession().setAttribute("showUri", uri);
	}

	public String getActionName() {
		return actionName;
	}
	
	private void setActionName(String actionName) {
		this.actionName = actionName;
		getSession().setAttribute("showActionName", actionName);
	}
}
