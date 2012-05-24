/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;

import net.i2geo.comped.model.Thing;
import net.i2geo.comped.dao.ThingDao;

/**
 * @author Martin Homik
 *
 */
public class ThingDaoHibernate extends GenericDaoHibernate<Thing, Long> implements ThingDao {

	public ThingDaoHibernate() {
        super(Thing.class);
    }
	
    @SuppressWarnings("unchecked")
    public List<Thing> findByCreator(User creator) {
        return getHibernateTemplate().find(
        		"from Thing where creator=?", creator);
    }

    @SuppressWarnings("unchecked")
    public List<Thing> findByUri(String uri) {
        List<Thing> result = getHibernateTemplate().find(
        		"from Thing where uri like ?", uri);
        if (result == null || result.isEmpty()) {
        	return new ArrayList<Thing>();
        } 
        
        return result;
    }

}
