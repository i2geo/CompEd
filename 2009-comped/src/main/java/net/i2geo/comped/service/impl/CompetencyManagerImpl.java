/**
 * 
 */
package net.i2geo.comped.service.impl;


import java.util.Iterator;

import net.i2geo.comped.dao.GenericCompetencyDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.service.CompetencyManager;

/**
 * @author Martin Homik
 *
 */
public class CompetencyManagerImpl extends GenericCompetencyManagerImpl<Competency>
		implements CompetencyManager {

	public CompetencyManagerImpl(
			GenericCompetencyDao<Competency> genericCompetencyDao) {
		super(genericCompetencyDao);
	}

	public void detach(Competency competency) {
	    
	   	competency = get(competency.getId());
	   	competency.setComposes(null);
	   	competency.setSubsumes(null);
	   	competency.setDescriptions(null);
	   	competency.setSimilarTo(null);
	   	
	   	save(competency);
	   	
	   	// detach composed objects
	   	for (Iterator<Competency> iter = competency.getComposedBy().iterator(); iter.hasNext();) {
	   		Competency parent = get(iter.next().getId());
	   		parent.removeComposes(competency);
	   		save(parent);
	   	}
	   	
	   	// detach subsumed objects
	   	for (Iterator<Competency> iter = competency.getSubsumedBy().iterator(); iter.hasNext();) {
	   		Competency parent = get(iter.next().getId());
	   		parent.removeSubsumes(competency);
	   		save(parent);
	   	}

	   	// detach similar objects
	   	for (Iterator<Competency> iter = competency.getInverseSimilarTo().iterator(); iter.hasNext();) {
	   		Competency c = get(iter.next().getId());
	   		c.removeSimilarTo(competency);
	   		save(c);
	   	}
	   
    	
    	remove(competency.getId());    	
    }

}
