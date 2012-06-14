/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import net.i2geo.comped.dao.GenericCompetencyDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;

/**
 * @author Martin Homik
 *
 */
abstract public class GenericCompetencyDaoHibernate<T extends Competency> extends GenericDaoHibernate<T, Long> 
	implements GenericCompetencyDao<T> {
	
	private Class<T> persistedClass = null;
	private String persistedClassName = null;
	
	public GenericCompetencyDaoHibernate(Class<T> clazz) {
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
    
    // TODO: move to manager
    public Set<Competency> findAllSimilarities(Competency competency) {
    	Set<Competency> c1 = competency.getSimilarTo();
    	Set<Competency> c2 = competency.getInverseSimilarTo();
    	c1.addAll(c2);
    	return c1;
    }

    @SuppressWarnings("unchecked")
    public List<T> findByLastCreated(int number) {
    	// need to get a new HibernateTemplate to set own constraints
    	HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
    	ht.setMaxResults(number);
    	return ht.find("from " + persistedClassName + " c ORDER BY c.created desc");
    }

    @SuppressWarnings("unchecked")
    public List<T> findByLastModified(int number) {
    	// need to get a new HibernateTemplate to set own constraints
    	HibernateTemplate ht = new HibernateTemplate(getSessionFactory());
    	ht.setMaxResults(number);
    	return ht.find("from " + persistedClassName + " c ORDER BY c.modified desc");
    }
    
    public List<Name> getNames(Competency competency) {
    	return competency.getNames();
    }
    
	public List<Name> findNames(Competency competency, String type, String locale, boolean isDefault){
		List<Name> names = new ArrayList<Name>();
		
		for (Iterator<Name> iterator = competency.getNames().iterator(); iterator.hasNext();) {
			Name name = (Name) iterator.next();
			
			if (type != null & type != "" & !name.getType().equals(type)){
				continue;
			}
			if (locale != null & locale != "" & !name.getLocale().equals(locale)){
				continue;
			}
			names.add(name);
		}
		return names;
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
}
