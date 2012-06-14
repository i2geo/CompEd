/**
 * 
 */
package net.i2geo.comped.service;

import net.i2geo.comped.model.CompetencyProcess;

/**
 * @author Martin Homik
 *
 */
public interface CompetencyProcessManager extends GenericCompetencyManager<CompetencyProcess> {
	
	public CompetencyProcess getRootCompetencyProcess();
	
}
