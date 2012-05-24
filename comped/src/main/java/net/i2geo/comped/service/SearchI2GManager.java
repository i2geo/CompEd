/**
 * 
 */
package net.i2geo.comped.service;

/**
 * A POJO manager class that simply stores the browser path which is injected by Spring
 * through beans setter injection. This POJO is needed for the form JSP pages to set
 * the browser path to SkillsTextBox dynamically.
 * 
 * @see net.i2geo.comped.webapp.action.NameableAction#setSearchI2GManager(SearchI2GManager)
 * 
 * @author Martin Homik
 *
 */
public class SearchI2GManager {

	private String browserPath;

	public String getBrowserPath() {
		return browserPath;
	}

	public void setBrowserPath(String browserPath) {
		this.browserPath = browserPath;
	}
	
	
}
