/**
 * 
 */
package net.i2geo.comped.dao.hibernate;


import net.i2geo.comped.dao.CompetencyDao;
import net.i2geo.comped.model.Competency;

/**
 * @author Martin Homik
 *
 */
public class CompetencyDaoHibernate extends GenericCompetencyDaoHibernate<Competency> implements CompetencyDao {
		
	public CompetencyDaoHibernate() {
		super(Competency.class);
    }
}
