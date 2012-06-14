/**
 * 
 */
package net.i2geo.comped.service.impl;


import java.util.Iterator;
import java.util.List;
import java.util.Set;


import net.i2geo.comped.dao.AbstractTopicDao;
import net.i2geo.comped.dao.ConcreteCompetencyDao;
import net.i2geo.comped.dao.ConcreteTopicDao;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.AbstractTopicManager;

/**
 * @author Martin Homik
 *
 */
public class AbstractTopicManagerImpl extends GenericTopicManagerImpl<AbstractTopic>
		implements AbstractTopicManager {
	
	private AbstractTopicDao tgDao = null;
	private ConcreteTopicDao ctDao = null;
	private ConcreteCompetencyDao ccDao = null;
	
	public AbstractTopicManagerImpl(AbstractTopicDao abstractTopicDao) {
		super(abstractTopicDao);
		this.tgDao = abstractTopicDao;
	}

	public void setConcreteCompetencyDao(ConcreteCompetencyDao ccDao) {
		this.ccDao = ccDao;
	}

	public void setConcreteTopicDao(ConcreteTopicDao ctDao) {
		this.ctDao = ctDao;
	}

   	// detach children abstract topics
	public void detach(AbstractTopic topic) {
		topic.setParent(null);
		save(topic);
		
		// detach from linked abstract topics
		for (Iterator<AbstractTopic> iter = topic.getChildren().iterator(); iter.hasNext();) {
			AbstractTopic t = get(iter.next().getId());
			List<AbstractTopic> parents = t.getParent();
			parents.remove(topic);
			save(t);
		}
		
		// detach from linked concrete topics
		for (Iterator<ConcreteTopic> iter = topic.getItems().iterator(); iter.hasNext();) {
			ConcreteTopic t = ctDao.get(iter.next().getId());
			List<AbstractTopic> parents = t.getAts();
			parents.remove(topic);
			ctDao.save(t);
		}
	
		// detach concrete competencies that have a link to this concrete topic
		List<ConcreteCompetency> ccs = tgDao.findLinkedConcreteCompetency(topic);
		for (Iterator<ConcreteCompetency> iter = ccs.iterator(); iter.hasNext();) {
			ConcreteCompetency cc = ccDao.get(iter.next().getId());
			Set<Topic> topics = cc.getTopics();
			topics.remove(topic);
			ccDao.save(cc);
		}

		remove(topic.getId());

	}
}