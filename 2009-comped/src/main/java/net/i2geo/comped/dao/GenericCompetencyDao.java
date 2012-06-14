/**
 * 
 */
package net.i2geo.comped.dao;

import java.util.List;
import java.util.Set;

import org.appfuse.dao.GenericDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;

/**
 * @author Martin Homik
 *
 */
public interface GenericCompetencyDao<T extends Competency> extends GenericDao<T,Long> {
	
	public T       			findByUri(String uri);

	// TODO: move to Manager
	public Set<Competency>  findAllSimilarities(Competency competency);
	
	public List<T> 		findByLastCreated(int number);
	public List<T> 		findByLastModified(int number);

	public List<Name> 		getNames(Competency competency);
	public List<Name> 		findNames(Competency competency, String type, String locale, boolean isDefault);

	public List<T>			findByNameExact(String token, String language);
	public List<T>			findByNameExact(String token, String language, int number);

	public List<T>			findByNameLike(String token, String language);
	public List<T>			findByNameLike(String token, String language, int number);
	
	public List<T>			findByName(String token, String language, boolean like, int number);
}
