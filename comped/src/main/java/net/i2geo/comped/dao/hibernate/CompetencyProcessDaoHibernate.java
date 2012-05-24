/**
 * 
 */
package net.i2geo.comped.dao.hibernate;


import net.i2geo.comped.dao.CompetencyProcessDao;
import net.i2geo.comped.model.CompetencyProcess;

/**
 * @author Martin Homik
 *
 */
public class CompetencyProcessDaoHibernate extends GenericCompetencyDaoHibernate<CompetencyProcess> 
	implements CompetencyProcessDao {
	
	public CompetencyProcessDaoHibernate() {
        super(CompetencyProcess.class);
    }
}
