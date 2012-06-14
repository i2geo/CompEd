/**
 * 
 */
package net.i2geo.comped.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.appfuse.service.GenericManager;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;

/**
 * @author Martin Homik
 *
 */
public interface GenericCompetencyManager<T extends Competency> extends GenericManager<T, Long> {

	public void setLocale(Locale locale);
	
	public List<T> getAll(Locale locale);
	
	public T          get(Long id, String locale);

	public List<T>    findByUriSuffix(Collection<String> suffixes);
	public List<T>    findByUriSuffix(String suffixes);

	public T          findByUri(String uri);
	public List<T>    findByLastCreated(int number);
	public List<T>    findByLastModified(int number);
	
	public List<Name> getNames(T competency);
	public T          addName(Long cId, Name name);
	public T      	   editName(Long cId, Name name);
	public String     getName(Competency c, String locale);

	@SuppressWarnings("unchecked")
	public List    mapToLocale(Collection list, String language);

	public List<T>			findByNameExact(String token, String language);
	public List<T>			findByNameExact(String token, String language, int number);

	public List<T>			findByNameLike(String token, String language);
	public List<T>			findByNameLike(String token, String language, int number);
	
	public List<T>			findByName(String token, String language, boolean like, int number);
	
	public void detach(T competency);
	public T prepare(T competency);
}
