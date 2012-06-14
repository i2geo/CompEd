/**
 * 
 */
package net.i2geo.comped.service;

import java.util.List;

import org.appfuse.service.GenericManager;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;
import net.i2geo.comped.model.Thing;

/**
 * @author Martin Homik
 *
 */
public interface NameManager extends GenericManager<Name, Long> {
	
	public List<Name> getNames(Nameable nameable);
	public List<Name> findByNameExact(String name);
	public List<Name> findByNameLike(String name);
	public List<Name> getByLocale(String locale);
	public List<Name> findByNameAndLocaleExact(String name, String locale);
	public List<Name> findByNameAndLocaleLike(String name, String locale);
	public List<Name> findByType(String type);
	public List<Name> findByTypeAndLocale(String type, String locale);
	public List<Name> findByThing(Thing thing);
	public List<Name> findByThingAndType(Thing thing, String type);
	public List<Name> findByThingAndTypeAndLocale(Thing thing, String type, String locale);
	public Name       findDefaultByThingAndTypeAndLocale(Nameable nameable, String type, String locale);	
	
	public void detach(Name name);
	
}