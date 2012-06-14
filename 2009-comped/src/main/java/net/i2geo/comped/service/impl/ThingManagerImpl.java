/**
 * 
 */
package net.i2geo.comped.service.impl;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;

import net.i2geo.comped.dao.ThingDao;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.service.ThingManager;

/**
 * @author Martin Homik
 * 
 */
public class ThingManagerImpl extends GenericManagerImpl<Thing, Long>
		implements ThingManager {

	ThingDao thingDao;	
	
    public ThingManagerImpl(ThingDao thingDao) {
        super(thingDao);
        this.thingDao = thingDao;
    }


    public List<Thing> findByUri(String uri) {
    	return thingDao.findByUri("%"+uri);
    }

    public boolean  isUnique(Thing thing) {
    	List<Thing> things = findByUri(thing.getUri());
    	if (things != null && !things.isEmpty()) {
    		if (things.get(0).getId() == thing.getId()) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	return true;
    }
}