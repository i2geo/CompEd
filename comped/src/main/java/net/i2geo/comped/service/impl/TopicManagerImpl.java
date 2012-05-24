/**
 * 
 */
package net.i2geo.comped.service.impl;


import net.i2geo.comped.dao.TopicDao;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.TopicManager;

/**
 * @author Martin Homik
 * 
 */
public class TopicManagerImpl extends GenericTopicManagerImpl<Topic>
		implements TopicManager {

	TopicDao topicDao;
	
    public TopicManagerImpl(TopicDao topicDao) {
        super(topicDao);
        this.topicDao = topicDao;
    }

	public void detach(Topic topic) {
		remove(topic.getId());
	}

    
}