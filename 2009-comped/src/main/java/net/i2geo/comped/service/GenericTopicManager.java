/**
 * 
 */
package net.i2geo.comped.service;


import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.appfuse.service.GenericManager;

import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Topic;

/**
 * @author Martin Homik
 *
 */
public interface GenericTopicManager<T extends Topic> extends GenericManager<T, Long> {
	
	public void setLocale (Locale locale);
	
	public List<T> getAll(String locale);
	
	public T          get(Long id, String locale);
		
	public T       findByUri(String uri);
	public List<T> findByUriSuffix(Collection<String> suffixes);
	public List<T> findByUriSuffix(String suffixes);
	
	public List<T> findByLastCreated(int number);
	public List<T> findByLastModified(int number);
	
	public void detach(T topic);
	public T prepare(T topic);
	
	public String      getName(Topic t, String locale);
	public T 	        addName(Long topicId, Name name);
	public T           editName(Long topicId, Name name);

	@SuppressWarnings("unchecked")
	public List    mapToLocale(Collection list, String language);

	public List<T>			findByNameExact(String token, String language);
	public List<T>			findByNameExact(String token, String language, int number);

	public List<T>			findByNameLike(String token, String language);
	public List<T>			findByNameLike(String token, String language, int number);
	
	public List<T>			findByName(String token, String language, boolean like, int number);

}