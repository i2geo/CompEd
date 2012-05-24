/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import net.i2geo.comped.dao.GenericTopicDao;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Topic;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author Martin Homik
 *
 */
abstract public class GenericTopicDaoHibernate<T extends Topic> extends GenericDaoHibernate<T, Long> 
	implements GenericTopicDao<T> {

	private Class<T> persistedClass = null;
	private String persistedClassName = null;

	public GenericTopicDaoHibernate(Class<T> clazz) {
        super(clazz);
        this.persistedClass = clazz;
        this.persistedClassName = clazz.getName();

    }

	protected Class<T> getPersistedClass() {
		return persistedClass;
	}
	
	
    @SuppressWarnings("unchecked")
    public T findByUri(String uri) {
        List<T> result = getHibernateTemplate().find(
        		"from " + persistedClassName + " where uri like ?", uri);
        if (result == null || result.isEmpty()) {
        	return null;
        } 
        
        return result.get(0);
    }
        
    @SuppressWarnings("unchecked")
    public List<T> findByLastCreated(int number) {
    	// need to get a new HibernateTemplate to set own constraints
    	HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
    	ht.setMaxResults(number);
    	return ht.find("from " + persistedClassName + " t ORDER BY t.created desc");
    }

    @SuppressWarnings("unchecked")
    public List<T> findByLastModified(int number) {
    	// need to get a new HibernateTemplate to set own constraints
    	HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
    	ht.setMaxResults(number);
    	return ht.find("from " + persistedClassName + " t ORDER BY t.modified desc");
    }

	public List<T> findByNameExact(String token, String language) {
		return findByNameExact(token, language, -1);
	}

	public List<T> findByNameExact(String token, String language, int number) {
		return findByName(token, language, false, number);
	}

	public List<T> findByNameLike(String token, String language) {
		return findByNameLike(token, language, -1);
	}

	public List<T> findByNameLike(String token, String language, int number) {
		return findByName(token, language, true, number);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByName(String token, String language, boolean like, int number) {
    	String[] names  = {"token", "language"};
    	Object[] values = {token.toLowerCase(), language}; 
    	
    	String nameClause;
    	
    	if (like) {
    		nameClause = "where LOWER(name.name) like :token ";
    	} else {
    		nameClause = "where LOWER(name.name)=:token ";
    	}
    	
    	HibernateTemplate ht = null;
		if (number != -1) {
			ht = getHibernateTemplate();
		} else {
			// need to get a new HibernateTemplate to set own constraints
			ht = new HibernateTemplate(getSessionFactory());
			ht.setMaxResults(number);
		}
		return ht.findByNamedParam(
				"select distinct c "
				+ "from " + persistedClassName + " as c, Name as name " 
				+ nameClause 
				+ "and name.locale=:language "
				+ "and name member of c.names)"
				, names, values);
	}    
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.dao.GenericTopicDao#findByConcreteCompetency(net.i2geo.comped.model.Topic)
	 */
	@SuppressWarnings("unchecked")
	public List<ConcreteCompetency> findLinkedConcreteCompetency(T topic) {
        List<ConcreteCompetency> result = getHibernateTemplate().find(
        		"from ConcreteCompetency c where ? member of c.topics", topic);

        if (result == null) {
        	return new ArrayList<ConcreteCompetency>();
        } 
        
        return result;
	}

}
