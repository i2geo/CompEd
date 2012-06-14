/**
 * 
 */
package net.i2geo.comped.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.appfuse.service.impl.GenericManagerImpl;

import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;

import net.i2geo.comped.dao.GenericTopicDao;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.compare.DefaultCommonNamesByLanguageCompetencyComparator;
import net.i2geo.comped.model.compare.DefaultCommonNamesByLanguageTopicComparator;
import net.i2geo.comped.service.GenericTopicManager;

/**
 * @author Martin Homik
 * 
 */
abstract public class GenericTopicManagerImpl<T extends Topic> extends GenericManagerImpl<T, Long>
		implements GenericTopicManager<T>, LocaleProvider {

	private final transient TextProvider textProvider = new TextProviderFactory().createInstance(getClass(), this);

	/**
     * GenericDao instance, set by constructor of this class
     */
    protected GenericTopicDao<T> genericTopicDao;
	
    protected Locale locale = null;

    /**
     * Public constructor for creating a new GenericManagerImpl.
     * @param genericDao the GenericDao to use for persistence
     */
    public GenericTopicManagerImpl(GenericTopicDao<T> genericTopicDao) {
    	super(genericTopicDao);
    	this.genericTopicDao = genericTopicDao;
    }

    public void setLocale(Locale locale) {
    	this.locale = locale;
    }

    public Locale getLocale() {
    	return locale;
    }
    
    protected boolean isSetLocale() {
    	return (locale != null);
    }

    
    // NOTE: should I put the logic to the manager or to the Dao?
    public T get(Long id, String locale) {
    	T t = genericTopicDao.get(id);
    	t.setName(getName(t, locale));
    	
    	return t;
    }
    
    // NOTE: getAll retrieves all items. No proper pagination!
    public List<T> getAll(String locale) {
    	List<T> l = genericTopicDao.getAll();
    	
		Collections.sort(l, 
				new DefaultCommonNamesByLanguageTopicComparator(locale));

    	for (T t : l) {
    		t.setName(getName(t, locale));
    	}
    	return l;
    }
   
    
    private String findDefaultCommonName(List<Name> names, String locale) {
    	for (Name name : names) {
			if (name.getType().toUpperCase().equals(Name.TYPE_COMMON)
					&& name.getLocale().toLowerCase().equals(locale.toLowerCase())
					&& name.isDefName()) {
				return name.getName();
			}
		}    	
    	 return null;
    }
    
    private String findCommonName(List<Name> names, String locale) {
    	// first: find the common name in requested locale
    	for (Name name : names) {
			if (name.getType().toUpperCase().equals(Name.TYPE_COMMON)
					&& name.getLocale().toLowerCase().equals(locale.toLowerCase())) {
				return name.getName();
			}
		}    
    	return null;
    }

	// second: find the common name in English
	// TODO: i18n
    private String findCommonEnglishName(List<Name> names, String locale) {
    	String noTranslation = textProvider.getText("item.noTranslation", "no translation");
    	for (Name name : names) {
			if (name.getType().toUpperCase().equals(Name.TYPE_COMMON)
					&& name.getLocale().toLowerCase().equals("en")) {
				return noTranslation + ": " + name.getName();
			}
		}    	
    	return null;
    }

    
    public String getName(Topic t, String locale) {
    	String noTranslation;
    	if (textProvider != null) {
    		noTranslation = textProvider.getText("item.noTranslation", "no translation");
    	} else {
    		noTranslation = "BLABLA";
    		log.debug("******** NO TRANSLATION");
    	}
    		List<Name> names = t.getNames();
			
    	String n;
    	n = findDefaultCommonName(names, locale);
    	if (n != null) {return n;}

    	n = findCommonName(names, locale);
    	if (n != null) {return n;}

    	n = findCommonEnglishName(names, locale);
    	if (n != null) {return n;}
		
    	// TODO: i18n
   		return noTranslation + ": " + t.getUriSuffix(null);    	
    }
    
	/* (non-Javadoc)
	 * @see org.comped.service.TopicManager#findByUri(java.lang.String)
	 */
	public T findByUri(String uri) {
		return genericTopicDao.findByUri(uri);
	}
	
	/* (non-Javadoc)
	 * @see net.i2geo.comped.service.GenericTopicManager#findByUriSuffix(java.util.Collection)
	 */
	public List<T> findByUriSuffix(Collection<String> suffixes) {
		List<T> topics = new ArrayList<T>();

		if (suffixes == null) {return topics;}
		
		for (String suffix : suffixes) {
			String searchToken = "%" + suffix;
			if (searchToken.endsWith("_r")) {
				searchToken = searchToken.substring(0, searchToken.length()-2);
			}
			T t = findByUri(searchToken);
			if (t != null) {
				topics.add(t);
			}
		}
		
		return topics;
	}

	/**
	 * This method has a special purpose for the SKB. SKB can be
	 * invoked from an s:hidden component only. This accepts only Strings in
	 * the value parameter. Therefore, the String must be interpreted as 
	 * a list of Strings, separated by comma.
	 * @param suffixes
	 * @return
	 */
	public List<T> findByUriSuffix(String suffixes) {
		Collection<String> dummy = new ArrayList<String>();
		if (suffixes == null || suffixes.isEmpty()) {return new ArrayList<T>();}
		for (String suffix : suffixes.split(",")) {
			dummy.add(suffix.trim());
		}
		
		return findByUriSuffix(dummy);
	}
	
		
	/*
	 * Returns a list of the last 'number' created topics.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.TopicManager#findByLastCreated(int)	 
	 */
	public List<T> findByLastCreated(int number) {
		// return genericTopicDao.findByLastCreated(number);
		List<T> l = genericTopicDao.findByLastCreated(number);
		
		for (T t : l) {
			t.setName(getName(t, locale.getLanguage()));
		}
		return l;
	}

	/*
	 * Returns a list of the last 'number' modified topics.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.TopicManager#findByLastModified(int)	 
	 */
	public List<T> findByLastModified(int number) {
		// return genericTopicDao.findByLastModified(number);
		List<T> l = genericTopicDao.findByLastModified(number);
		
		for (T t : l) {
			t.setName(getName(t, locale.getLanguage()));
		}
		return l;
	}

	abstract public void detach(T topic);
	
	public T prepare(T topic) {
		boolean isNew = (topic.getId() == null);
		
        Date date = new Date();
        if (isNew) {
        	// add topic
        	topic.setCreated(date);
        	topic.setModified(date);
        	topic.setStatus(Thing.STATUS_DRAFT);
        	topic.setCommentStatus(Thing.COMMENTS_CLOSED);
        	topic.setCommentDays(0);
        } else {
        	// save topic: changing modified date
        	topic.setModified(date);
        }
        return topic;
	} 
	
	/*
	 * Edit a name to the list of names of a topic.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.TopicManager#editName(java.lang.Long, net.i2geo.comped.model.Name)	 
	 */
	public T editName(Long topicId, Name name) {
		T topic = get(topicId);
		List<Name> tNames = topic.getNames();
	
		// if the new name is a default common name then 
		// set all other common names of this language to non-default
		if (name.isDefName() && name.getType().equals(Name.TYPE_COMMON)) {
			log.debug("Setting edited name as default name.");
			for (Name n : tNames) {
				if (name != n && name.getLocale().equals(n.getLocale()) && n.isCommon()) {
					n.setDefName(false);
				}
			}
		}

		topic.setNames(tNames);
		topic.setModified(new Date());

		return topic;
	}

	/*
	 * Adds a name to the list of names of a topic.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.TopicManager#addName(java.lang.Long, net.i2geo.comped.model.Name)	 
	 */
	public T addName(Long topicId, Name name) {
		T topic = get(topicId);
		List<Name> tNames = topic.getNames();

		// if there is no other name in the names locale then make this name
		// the default name.
		if (findCommonName(tNames, name.getLocale()) == null) {
			name.setDefName(true);
		}
		
		// if the new name is a default common name then 
		// set all other common names of this language to non-default
		if (name.isDefName() && name.getType().equals(Name.TYPE_COMMON)) {
			for (Name n : tNames) {
				log.debug("Setting new name as default name.");
				if (name.getLocale().equals(n.getLocale()) && n.isCommon()) {
					n.setDefName(false);
				}
			}
		}

		
		tNames.add(name);
		topic.setNames(tNames);
		topic.setModified(new Date());
		
		return topic;
	}

	@SuppressWarnings("unchecked")
	public List mapToLocale(Collection list, String language) {
		ArrayList mappedList = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Topic t = (Topic) iterator.next();
			t.setName(getName(t, language));
			mappedList.add(t);
		}
		return mappedList;
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNameExact(String token, String language) {
		List<T> dbList =  genericTopicDao.findByNameExact(token, language);	
		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNameExact(String token, String language, int number) {
		List<T> dbList = genericTopicDao.findByNameExact(token, language, number);
		return mapToLocale(dbList, language);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNameLike(String token, String language) {
		List<T> dbList = genericTopicDao.findByNameLike(token, language);
		Collections.sort(dbList, 
				new DefaultCommonNamesByLanguageTopicComparator(language));

		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNameLike(String token, String language, int number) {
		List<T> dbList = genericTopicDao.findByNameLike(token, language, number);
		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByName(String token, String language, boolean like, int number) {
		List<T> dbList = genericTopicDao.findByName(token, language, like, number);
		return mapToLocale(dbList, language);
	}
	
}