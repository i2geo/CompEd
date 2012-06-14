/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import net.i2geo.comped.dao.ConcreteTopicDao;
import net.i2geo.comped.model.ConcreteTopic;

/**
 * @author Martin Homik
 *
 */
public class ConcreteTopicDaoHibernate extends GenericTopicDaoHibernate<ConcreteTopic> 
	implements ConcreteTopicDao {

	public ConcreteTopicDaoHibernate() {
        super(ConcreteTopic.class);
    }
}
