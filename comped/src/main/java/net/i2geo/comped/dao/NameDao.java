/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.List;
import org.appfuse.dao.GenericDao;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;

/**
 * @author Martin Homik
 *
 */
public interface NameDao extends GenericDao<Name, Long> {
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
	public List<Name> findDefaultByThingAndTypeAndLocale(Nameable name, String type, String locale);	
	
	public List<Competency> findCompetenciesByName(Name name);
	public List<Topic> findTopicsByName(Name name);

}
