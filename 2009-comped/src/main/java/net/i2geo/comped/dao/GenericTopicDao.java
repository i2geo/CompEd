/**
 * 
 */
package net.i2geo.comped.dao;

import java.util.List;

import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Topic;

import org.appfuse.dao.GenericDao;

/**
 * @author Martin Homik
 *
 */
public interface GenericTopicDao<T extends Topic> extends GenericDao<T, Long> {
	
	/**
	 * Retrieves a topic by the name part of the uri. Note, uri contains only the name part.
	 * @param uri
	 * @return
	 */
	public T findByUri(String uri);
	

	/**
	 * Find the last 'number' newly created topics.
	 * @param number
	 * @return topics
	 */
	public List<T> findByLastCreated(int number);
	
	/**
	 * Find the last 'number' modified topics.
	 * @param number
	 * @return
	 */
	public List<T> findByLastModified(int number);

	
	/**
	 * 
	 * @param token
	 * @param language
	 * @return
	 */
	public List<T>	 findByNameExact(String token, String language);

	/**
	 * 
	 * @param token
	 * @param language
	 * @param number
	 * @return
	 */
	public List<T>	 findByNameExact(String token, String language, int number);

	/**
	 * 
	 * @param token
	 * @param language
	 * @return
	 */
	public List<T> findByNameLike(String token, String language);
	
	/**
	 * 
	 * @param token
	 * @param language
	 * @param number
	 * @return
	 */
	public List<T>	findByNameLike(String token, String language, int number);

	/**
	 * 
	 * @param token
	 * @param language
	 * @param like
	 * @param number
	 * @return
	 */
	public List<T>	findByName(String token, String language, boolean like, int number);
	
	/**
	 * Find concrete competencies that are linked to requested abstract/concrete topic.
	 * 
	 * @param topic
	 * @return
	 */
	public List<ConcreteCompetency> findLinkedConcreteCompetency(T topic);
}
