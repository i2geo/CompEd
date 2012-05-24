/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import net.i2geo.comped.dao.ConcreteCompetencyDao;
import net.i2geo.comped.model.ConcreteCompetency;

/**
 * @author Martin Homik
 *
 */
public class ConcreteCompetencyDaoHibernate extends GenericCompetencyDaoHibernate<ConcreteCompetency> 
	implements ConcreteCompetencyDao {

	public ConcreteCompetencyDaoHibernate() {
        super(ConcreteCompetency.class);
    }	
}
