/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.List;
import org.appfuse.dao.GenericDao;
import net.i2geo.comped.model.Description;
import net.i2geo.comped.model.Thing;

/**
 * @author Martin Homik
 *
 */
public interface DescriptionDao extends GenericDao<Description, Long> {
	public List<Description> findByDescriptionExact(String description);
	public List<Description> findByDescriptionLike(String description);
	public List<Description> getByLocale(String locale);
	public List<Description> findByDescriptionAndLocaleExact(String description, String locale);
	public List<Description> findByDescriptionAndLocaleLike(String description, String locale);
	public List<Description> findByThing(Thing thing);
	public List<Description> findByThingAndLocale(Thing thing, String locale);
}
