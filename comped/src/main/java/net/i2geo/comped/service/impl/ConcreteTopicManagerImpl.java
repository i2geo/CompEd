/**
 * 
 */
package net.i2geo.comped.service.impl;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.i2geo.comped.dao.ConcreteCompetencyDao;
import net.i2geo.comped.dao.ConcreteTopicDao;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.ConcreteTopicManager;

/**
 * @author Martin Homik
 *
 */
public class ConcreteTopicManagerImpl extends GenericTopicManagerImpl<ConcreteTopic>
		implements ConcreteTopicManager {

	private ConcreteTopicDao ctDao;
	private ConcreteCompetencyDao ccDao;
	
	public ConcreteTopicManagerImpl(ConcreteTopicDao concreteTopicDao) {
		super(concreteTopicDao);
		this.ctDao = concreteTopicDao;
	}
	
	public void setConcreteCompetencyDao(ConcreteCompetencyDao ccDao) {
		this.ccDao = ccDao;
	}
	
    public void detach(ConcreteTopic topic) {
    	
    	topic.setAts(null);
    	save(topic);
    	
    	List<ConcreteCompetency> ccs = ctDao.findLinkedConcreteCompetency(topic);
    	
    	// detach concrete competencies that have a link to this concrete topic
    	for (Iterator<ConcreteCompetency> iter = ccs.iterator(); iter.hasNext();) {
	   		ConcreteCompetency cc = ccDao.get(iter.next().getId());
	   		Set<Topic> topics = cc.getTopics();
	   		topics.remove(topic);
	   		ccDao.save(cc);
	   	}

    	remove(topic.getId());
    }

}
