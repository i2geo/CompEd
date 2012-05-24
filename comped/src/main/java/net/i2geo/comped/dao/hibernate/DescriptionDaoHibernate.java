/**
 * 
 */
package net.i2geo.comped.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Description;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.dao.DescriptionDao;

/**
 * @author Martin Homik
 *
 */
public class DescriptionDaoHibernate extends GenericDaoHibernate<Description, Long> implements DescriptionDao {

	public DescriptionDaoHibernate() {
        super(Description.class);
    }

	/*
    @SuppressWarnings("unchecked")
    public List<Description> findByThing(Thing thing) {
        return getHibernateTemplate().find(
        		"from Description where thing=?", thing);
	}
	*/
	
    @SuppressWarnings("unchecked")
    public List<Description> findByDescriptionExact(String description) {
        return getHibernateTemplate().find(
        		"from Description where description=?", description);
	}
	
    @SuppressWarnings("unchecked")
	public List<Description> findByDescriptionLike(String description) {
        return getHibernateTemplate().find(
        		"from Description where description like ?", description);
	}

    @SuppressWarnings("unchecked")
	public List<Description> getByLocale(String locale) {
        return getHibernateTemplate().find(
        		"from Description where locale=?", locale);
		
	}
    
    @SuppressWarnings("unchecked")
	public List<Description> findByDescriptionAndLocaleExact(String description, String locale) {
    	String[] names  = {"description", "locale"};
    	Object[] values = {description, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Description where description=:description and locale=:locale", names, values);
    }

    @SuppressWarnings("unchecked")
	public List<Description> findByDescriptionAndLocaleLike(String description, String locale) {
    	String[] names  = {"description", "locale"};
    	Object[] values = {description, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Description where description like :description and locale=:locale", names, values);
    }

    /*
    @SuppressWarnings("unchecked")
    public List<Description> findByThingAndLocale(Thing thing, String locale) {
    	String[] names  = {"thing", "locale"};
    	Object[] values = {thing, locale}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Description where thing=:thing and locale=:locale", names, values);
    	
    }        
    */

	@SuppressWarnings("unchecked")
    public List<Description> findByThing(Thing thing) {
    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
    		return getHibernateTemplate().find(
            		"select topic.description from Topic as topic where topic=?", topic);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
    		return getHibernateTemplate().find(
            		"select competency.descriptions from Competency as competency where competency=?", competency);
    	}
    	
    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Description>();
	}
    
    @SuppressWarnings("unchecked")
	public List<Description> findByThingAndLocale(Thing thing, String locale) {
    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
        	String[] names  = {"topic", "locale"};
        	Object[] values = {topic, locale}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select topic.descriptions from Topic as topic where topic=:topic and locale=:locale", names, values);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
        	String[] names  = {"competency", "locale"};
        	Object[] values = {competency, locale}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select competency.descriptions from Competency as competency where competency=:competency and locale=:locale", names, values);
    	}

    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Description>();
    }
    
    @SuppressWarnings("unchecked")
	public List<Description> findDefaultByThingLocale(Thing thing, String locale) {
    	if (thing instanceof Topic) {
    		Topic topic = (Topic) thing;
        	String[] names  = {"topic", "locale", "isDefName"};
        	Object[] values = {topic, locale, true}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select topic.descriptions from Topic as topic " +
            			"where topic=:topic and type=:type and isDefName=:isDefName", names, values);
    	}

    	if (thing instanceof Competency) {
    		Competency competency = (Competency) thing;
        	String[] names  = {"competency", "locale", "isDefName"};
        	Object[] values = {competency, locale, true}; 
    		return getHibernateTemplate().findByNamedParam(
            		"select competency.descriptions from Competency as competency " +
            			"where competency=:competency and locale=:locale and isDefName=:isDefName", names, values);
    	}

    	// if type mismatch, make sure to return an empty name list
    	return new ArrayList<Description>();

    	/*
    	String[] names  = {"thing", "type", "locale", "isDefName"};
    	Object[] values = {thing, type, locale, true}; 
    	
    	return getHibernateTemplate().findByNamedParam(
    			"from Name where thing=:thing and type=:type and locale=:locale and isDefName=:isDefName", names, values);
   */
    }

    
}
