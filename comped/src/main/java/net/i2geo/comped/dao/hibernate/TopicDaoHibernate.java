/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import net.i2geo.comped.dao.TopicDao;
import net.i2geo.comped.model.Topic;

/**
 * @author Martin Homik
 *
 */
public class TopicDaoHibernate extends GenericTopicDaoHibernate<Topic> implements TopicDao {

	public TopicDaoHibernate() {
        super(Topic.class);
    }

}
