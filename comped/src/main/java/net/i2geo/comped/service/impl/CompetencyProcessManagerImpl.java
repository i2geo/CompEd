/**
 * 
 */
package net.i2geo.comped.service.impl;




import java.util.Iterator;
import java.util.List;

import net.i2geo.comped.dao.CompetencyProcessDao;
import net.i2geo.comped.dao.ConcreteCompetencyDao;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.service.CompetencyProcessManager;

/**
 * @author Martin Homik
 *
 */
public class CompetencyProcessManagerImpl extends GenericCompetencyManagerImpl<CompetencyProcess>
		implements CompetencyProcessManager {

	private CompetencyProcessDao  cpDao = null;
	private ConcreteCompetencyDao ccDao = null;
	
    public CompetencyProcessManagerImpl(CompetencyProcessDao competencyDao) {
        super(competencyDao);
        this.cpDao = competencyDao;
    }

    public void setConcreteCompetencyDao(ConcreteCompetencyDao ccDao) {
    	this.ccDao = ccDao;
    }
    
    public CompetencyProcess getRootCompetencyProcess() {
    	return cpDao.findByUri("http://www.inter2geo.eu/2008/ontology/GeoSkills#Reproduce");
    }

    public void detach(CompetencyProcess competency) {
		    
	   	competency = get(competency.getId());
	   	competency.setComposes(null);
	   	competency.setSubsumes(null);
	   	competency.setDescriptions(null);
	   	competency.setSimilarTo(null);
	   	
	   	save(competency);
	   	
	   	// detach composed objects
	   	for (Iterator<Competency> iter = competency.getComposedBy().iterator(); iter.hasNext();) {
	   		CompetencyProcess parent = get(iter.next().getId());
	   		parent.removeComposes(competency);
	   		save(parent);
	   	}
	   	
	   	// detach subsumed objects
	   	for (Iterator<Competency> iter = competency.getSubsumedBy().iterator(); iter.hasNext();) {
	   		CompetencyProcess parent = get(iter.next().getId());
	   		parent.removeSubsumes(competency);
	   		save(parent);
	   	}
	   	    	// detach similar objects
	   	for (Iterator<Competency> iter = competency.getInverseSimilarTo().iterator(); iter.hasNext();) {
	   		CompetencyProcess c = get(iter.next().getId());
	   		c.removeSimilarTo(competency);
	   		save(c);
	   	}
	   
	   	// detach children competency processes
	   	for (Iterator<CompetencyProcess> iter = competency.getChildren().iterator(); iter.hasNext();) {
	   		CompetencyProcess c = get(iter.next().getId());
	   		List<CompetencyProcess> parents = c.getParent();
	   		parents.remove(competency);
	   		save(c);
	   	}
	    	// detach concrete competencies
    	for (Iterator<ConcreteCompetency> iter = competency.getCcompetencies().iterator(); iter.hasNext();) {
    		ConcreteCompetency c = ccDao.get(iter.next().getId());
    		List<CompetencyProcess> processes = c.getProcesses();
    		processes.remove(competency);
    		ccDao.save(c);
    	}
    	
    	remove(competency.getId());    	
    }
}

