/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.List;

import org.appfuse.dao.GenericDao;
import org.appfuse.model.User;

import net.i2geo.comped.model.Thing;

/**
 * @author Martin Homik
 *
 */
public interface ThingDao extends GenericDao<Thing, Long> {
	public List<Thing> findByCreator(User creator);
	public List<Thing> findByUri(String uri);
}
