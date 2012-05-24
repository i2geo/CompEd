/**
 * 
 */
package net.i2geo.comped.service.impl;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;

import net.i2geo.comped.dao.NameDao;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.CompetencyManager;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.ConcreteTopicManager;
import net.i2geo.comped.service.NameManager;
import net.i2geo.comped.service.TopicManager;

/**
 * @author Martin Homik
 * 
 */
public class NameManagerImpl extends GenericManagerImpl<Name, Long>
		implements NameManager {

	NameDao nameDao;
	
	private ConcreteCompetencyManager ccm;
	private CompetencyProcessManager cpm;
	private ConcreteTopicManager ctm;
	private AbstractTopicManager atm;
	
	
	public void setCcm(ConcreteCompetencyManager ccm) {
		this.ccm = ccm;
	}

	public void setCpm(CompetencyProcessManager cpm) {
		this.cpm = cpm;
	}

	public void setCtm(ConcreteTopicManager ctm) {
		this.ctm = ctm;
	}

	public void setAtm(AbstractTopicManager atm) {
		this.atm = atm;
	}

	public NameManagerImpl(NameDao nameDao) {
        super(nameDao);
        this.nameDao = nameDao;
    }

    
    /**
     * Return names of a nameable class. This can be Competency or Topic.
     * 
     */
    public List<Name> getNames(Nameable nameable) {
    	return nameable.getNames();
    }
    	
    // NOTE: Method overload would prevent long method names
    
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByNameExact(java.lang.String)
	 */
	public List<Name> findByNameExact(String name) {
		return nameDao.findByNameExact(name);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByNameLike(java.lang.String)
	 */
	public List<Name> findByNameLike(String name) {
		return nameDao.findByNameLike(name);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#getByLocale(java.lang.String)
	 */
	public List<Name> getByLocale(String locale) {
		return nameDao.getByLocale(locale);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByNameAndLocaleExact(java.lang.String, java.lang.String)
	 */
	public List<Name> findByNameAndLocaleExact(String name, String locale) {
		return nameDao.findByNameAndLocaleExact(name, locale);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByNameAndLocaleLike(java.lang.String, java.lang.String)
	 */
	public List<Name> findByNameAndLocaleLike(String name, String locale) {
		return nameDao.findByNameAndLocaleLike(name, locale);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByType(java.lang.String)
	 */
	public List<Name> findByType(String type) {
		return nameDao.findByType(type);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByTypeAndLocale(java.lang.String, java.lang.String)
	 */
	public List<Name> findByTypeAndLocale(String type, String locale) {
		return nameDao.findByTypeAndLocale(type, locale);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByThing(net.i2geo.model.Thing)
	 */
	public List<Name> findByThing(Thing thing) {
		return nameDao.findByThing(thing);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByThingAndType(net.i2geo.model.Thing, java.lang.String)
	 */
	public List<Name> findByThingAndType(Thing thing, String type) {
		return nameDao.findByThingAndType(thing, type);
	}
		
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findByThingAndTypeAndLocale(net.i2geo.model.ThingAndTypeAndLocale, java.lang.String, java.lang.String)
	 */
	public List<Name> findByThingAndTypeAndLocale(Thing thing, String type, String locale) {
		return nameDao.findByThingAndTypeAndLocale(thing, type, locale);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#findDefaultByThingAndTypeAndLocale(net.i2geo.model.ThingAndTypeAndLocale, java.lang.String, java.lang.String)
	 */
	public Name findDefaultByThingAndTypeAndLocale(Nameable nameable, String type, String locale) {
		List<Name> names = nameDao.findDefaultByThingAndTypeAndLocale(nameable, type, locale);
		if (names.isEmpty()) {
			return null; 
		} else {
			return names.get(0);
		}
	}
    
	/* (non-Javadoc)
	 * @see net.i2geo.service.NameManager#detach(net.i2geo.model.Name)
	 */
	public void detach(Name name) {
		List<Competency> cList = nameDao.findCompetenciesByName(name);
		if (!cList.isEmpty()) {
			for (Competency competency : cList) {
				List<Name> names = competency.getNames();
				names.remove(name);
				competency.setNames(names);
				if (competency instanceof ConcreteCompetency) {
					ccm.save((ConcreteCompetency) competency);
				}
				if (competency instanceof CompetencyProcess) {
					cpm.save((CompetencyProcess) competency);
				}
			}
		}

		List<Topic> tList = nameDao.findTopicsByName(name);
		if (!tList.isEmpty()) {
			for (Topic topic : tList) {
				List<Name> names = topic.getNames();
				names.remove(name);
				topic.setNames(names);
				
				if (topic instanceof ConcreteTopic) {
					ctm.save((ConcreteTopic) topic);
				}

				if (topic instanceof AbstractTopic) {
					atm.save((AbstractTopic) topic);
				}
			}
		}
		
		remove(name.getId());
	}

}