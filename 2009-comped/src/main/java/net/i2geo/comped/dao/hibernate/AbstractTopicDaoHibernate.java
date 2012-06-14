/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import net.i2geo.comped.dao.AbstractTopicDao;
import net.i2geo.comped.model.AbstractTopic;

/**
 * @author Martin Homik
 *
 */
public class AbstractTopicDaoHibernate extends GenericTopicDaoHibernate<AbstractTopic> 
	implements AbstractTopicDao {

	public AbstractTopicDaoHibernate() {
        super(AbstractTopic.class);
    }
	
}
