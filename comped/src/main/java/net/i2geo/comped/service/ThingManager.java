/**
 * 
 */
package net.i2geo.comped.service;

import java.util.List;

import org.appfuse.service.GenericManager;
import net.i2geo.comped.model.Thing;

/**
 * @author Martin Homik
 *
 */
public interface ThingManager extends GenericManager<Thing, Long> {
	
	public List<Thing> findByUri(String uri);
	public boolean   isUnique(Thing thing);
	
}