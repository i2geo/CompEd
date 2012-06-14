/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.appfuse.dao.hibernate.GenericDaoHibernate;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.dao.NameDao;

/**
 * @author Martin Homik
 *
 */
public class NameDaoHibernate extends GenericDaoHibernate<Name, Long> implements NameDao {

	
	public NameDaoHibernate() {
        super(Name.class);
    }
	
    @SuppressWarnings("unchecked")
    public List<Name> findByNameExact(String name) {
        return getHibernateTemplate().find(
        		"from Name where name=?", name);
	}
	
    @SuppressWarnings("unchecked")
	public List<Name> findByNameLike(String name) {
        return getHibernateTemplate().find(
        		"from Name where name like ?", name);
	}

    @SuppressWarnings("unchecked")
	public List<Name> getByLocale(String locale) {
        return getHibernateTemplate().find(
        		"from Name where locale=?", locale);
		
	}
    
    @SuppressWarnings("unchecked")
	public List<Name> findByNameAndLocaleExact(String name, String locale) {
    	String[] names  = {"name", "locale"};
    	Object[] values = {name, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Name where name=:name and locale=:locale", names, values);
    }

    @SuppressWarnings("unchecked")
	public List<Name> findByNameAndLocaleLike(String name, String locale) {
    	String[] names  = {"name", "locale"};
    	Object[] values = {name, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Name where name like :name and locale=:locale", names, values);
    }

    @SuppressWarnings("unchecked")
	public List<Name> findByType(String type) {
        return getHibernateTemplate().find(
        		"from Name where type=?", type);
	}

    @SuppressWarnings("unchecked")
	public List<Name> findByTypeAndLocale(String type, String locale) {
    	String[] names  = {"type", "locale"};
    	Object[] values = {type, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Name where type=:type and locale=:locale", names, values);		
	}

    @SuppressWarnings("unchecked")
    public List<Name> findByThing(Thing thing) {
    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
    		return getHibernateTemplate().find(
            		"select topic.names from Topic as topic where topic=?", topic);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
    		return getHibernateTemplate().find(
            		"select competency.names from Competency as competency where competency=?", competency);
    	}
    	
    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Name>();
	}

    @SuppressWarnings("unchecked")
    public List<Name> findByThingAndType(Thing thing, String type) {

    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
        	String[] names  = {"topic", "type"};
        	Object[] values = {topic, type}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select topic.names from Topic as topic where topic=:topic and type=:type", names, values);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
        	String[] names  = {"competency", "type"};
        	Object[] values = {competency, type}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select competency.names from Competency as competency where competency=:competency and type=:type", names, values);
    	}

    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Name>();
    	
    }
    
    @SuppressWarnings("unchecked")
	public List<Name> findByThingAndTypeAndLocale(Thing thing, String type, String locale) {
    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
        	String[] names  = {"topic", "type", "locale"};
        	Object[] values = {topic, type, locale}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select topic.names from Topic as topic where topic=:topic and type=:type and locale=:locale", names, values);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
        	String[] names  = {"competency", "type", "locale"};
        	Object[] values = {competency, type, locale}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select competency.names from Competency as competency where competency=:competency and type=:type and locale=:locale", names, values);
    	}

    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Name>();
    }
    
    @SuppressWarnings("unchecked")
	public List<Name> findDefaultByThingAndTypeAndLocale(Nameable nameable, String type, String locale) {
    	if (nameable instanceof Topic) {
    		Topic topic = (Topic) nameable;
        	String[] names  = {"topic", "type", "locale", "isDefName"};
        	Object[] values = {topic, type, locale, true}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select topic.names from Topic as topic " +
            			"where topic=:topic and type=:type and locale=:locale and isDefName=:isDefName", names, values);
    	}

    	if (nameable instanceof Competency) {
    		Competency competency = (Competency) nameable;
        	String[] names  = {"competency", "type", "locale", "isDefName"};
        	Object[] values = {competency, type, locale, true}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select competency.names from Competency as competency " +
            			"where competency=:competency and type=:type and locale=:locale and isDefName=:isDefName", names, values);
    	}

    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Name>();
    }

    
    @SuppressWarnings("unchecked")
    public List<Competency> findCompetenciesByName(Name name) {
    	return getHibernateTemplate().find(
        		"from Competency where ? in elements(names)", name);
    	
    }
    
    @SuppressWarnings("unchecked")
    public List<Topic> findTopicsByName(Name name) {
    	return getHibernateTemplate().find(
        		"from Topic where ? in elements(names)", name);
    }
 
    public void detach(Name name) {
    	// do nothing
    }
}
