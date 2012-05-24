/**
 * 
 */
package net.i2geo.comped.service.impl;


import java.util.Iterator;

import net.i2geo.comped.dao.GenericCompetencyDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.service.ConcreteCompetencyManager;

/**
 * @author Martin Homik
 *
 */
public class ConcreteCompetencyManagerImpl extends GenericCompetencyManagerImpl<ConcreteCompetency>
		implements ConcreteCompetencyManager {

	public ConcreteCompetencyManagerImpl(GenericCompetencyDao<ConcreteCompetency> genericCompetencyDao) {
		super(genericCompetencyDao);
	}

	public void detach(ConcreteCompetency competency) {
	    
	   	competency = get(competency.getId());
	   	competency.setComposes(null);
	   	competency.setSubsumes(null);
	   	competency.setDescriptions(null);
	   	competency.setSimilarTo(null);
	   	competency.setTopics(null);
	   	
	   	save(competency);
	   	
	   	// detach composed objects
	   	for (Iterator<Competency> iter = competency.getComposedBy().iterator(); iter.hasNext();) {
	   		ConcreteCompetency parent = get(iter.next().getId());
	   		parent.removeComposes(competency);
	   		save(parent);
	   	}
	   	
	   	// detach subsumed objects
	   	for (Iterator<Competency> iter = competency.getSubsumedBy().iterator(); iter.hasNext();) {
	   		ConcreteCompetency parent = get(iter.next().getId());
	   		parent.removeSubsumes(competency);
	   		save(parent);
	   	}

	   	// detach similar objects
	   	for (Iterator<Competency> iter = competency.getInverseSimilarTo().iterator(); iter.hasNext();) {
	   		ConcreteCompetency c = get(iter.next().getId());
	   		c.removeSimilarTo(competency);
	   		save(c);
	   	}
	       	
    	remove(competency.getId());    	
    }
	
}
