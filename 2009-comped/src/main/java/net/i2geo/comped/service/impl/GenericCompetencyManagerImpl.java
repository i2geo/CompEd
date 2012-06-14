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

import net.i2geo.comped.dao.GenericCompetencyDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.compare.DefaultCommonNamesByLanguageCompetencyComparator;
import net.i2geo.comped.service.GenericCompetencyManager;

/**
 * @author Martin Homik
 *
 */
abstract public class GenericCompetencyManagerImpl <T extends Competency> extends GenericManagerImpl<T, Long>
		implements GenericCompetencyManager<T>, LocaleProvider {

	private final transient TextProvider textProvider = new TextProviderFactory().createInstance(getClass(), this);
	
    /**
     * GenericDao instance, set by constructor of this class
     */
    protected GenericCompetencyDao<T> genericCompetencyDao;
    
    protected Locale locale = null;
    
    /**
     * Public constructor for creating a new GenericManagerImpl.
     * @param genericDao the GenericDao to use for persistence
     */
    public GenericCompetencyManagerImpl(GenericCompetencyDao<T> genericCompetencyDao) {
    	super(genericCompetencyDao);
    	this.genericCompetencyDao = genericCompetencyDao;
    }
    
    // implement the LocalProvider interface
    public Locale getLocale() {
        return locale; 
    }
    
    public void setLocale(Locale locale) {
   		this.locale = locale;
    }
    
    protected boolean isSetLocale() {
    	return (locale != null);
    }
        
    // NOTE: should I put the logic to the manager or to the Dao?
    public T get(Long id, String locale) {
    	T t = genericCompetencyDao.get(id);
    	t.setName(getName(t, locale));
    	
    	return t;
    }
    
    // NOTE: getAll retrieves all items. No proper pagination!
    public List<T> getAll(Locale locale) {
    	List<T> l = genericCompetencyDao.getAll();
		Collections.sort(l, 
				new DefaultCommonNamesByLanguageCompetencyComparator(locale.getLanguage()));

    	for (T t : l) {
    		t.setName(getName(t, locale.getLanguage()));
    	}
    	return l;
    }
    
	/* (non-Javadoc)
	 * @see net.i2geo.comped.service.GenericCompetencyManager#findByUriSuffix(java.util.Collection)
	 */
	public List<T> findByUriSuffix(Collection<String> suffixes) {
		List<T> items = new ArrayList<T>();

		if (suffixes == null) {return items;}
		
		for (String suffix : suffixes) {
			String searchToken = "%" + suffix;
			T t = findByUri(searchToken);
			if (t != null) {
				items.add(t);
			}
		}
		
		return items;
	}

	/**
	 * This method has a special purpose for the SKB. SKB can be
	 * invoked from a s:hidden component only. This accepts only Strings in
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
    
    public String getName(Competency c, String locale) {
    	String noTranslation = textProvider.getText("item.noTranslation", "no translation");
    	List<Name> names = c.getNames();
		
    	String n;
    	n = findDefaultCommonName(names, locale);
    	if (n != null) {return n;}

    	n = findCommonName(names, locale);
    	if (n != null) {return n;}

    	n = findCommonEnglishName(names, locale);
    	if (n != null) {return n;}
		
    	// TODO: i18n
   		return noTranslation + ": " + c.getUriSuffix(null);    	
    }
    
	public List<Name> getNames(T competency) {
    	return genericCompetencyDao.getNames(competency);
    }
    
	
	/* (non-Javadoc)
	 * @see org.comped.service.CompetencyManager#findByUri(java.lang.String)
	 */
	public T findByUri(String uri) {
		return genericCompetencyDao.findByUri(uri);
	}

	/*
	 * Returns a list of the last 'number' created competencies.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.CompetencyManager#findByLastCreated(int)	 
	 */
	public List<T> findByLastCreated(int number) {
		// return genericCompetencyDao.findByLastCreated(number);
		
		List<T> l = genericCompetencyDao.findByLastCreated(number);

		for (T t : l) {
			t.setName(getName(t, locale.getLanguage()));
		}
		
		return l;
	}

	/*
	 * Returns a list of the last 'number' modified competencies.
	 * 
	 * (non-Javadoc)
	 * @see org.comped.service.CompetencyManager#findByLastModified(int)	 
	 */
	public List<T> findByLastModified(int number) {
		// return genericCompetencyDao.findByLastModified(number);
		
		List<T> l = genericCompetencyDao.findByLastModified(number);
	
		for (T t : l) {
			t.setName(getName(t, locale.getLanguage()));
		}
		return l;
	}

	abstract public void detach(T competency);
	
	public T prepare(T competency) {
		boolean isNew = (competency.getId() == null);
		
        Date date = new Date();
        if (isNew) {
        	// add competency
        	competency.setCreated(date);
        	competency.setModified(date);
        	competency.setStatus(Thing.STATUS_DRAFT);
        	competency.setCommentStatus(Thing.COMMENTS_CLOSED);
        	competency.setCommentDays(0);
        } else {
        	// save competency: changing modified date
        	competency.setModified(date);
        }
        return competency;
	}
	
	public T addName(Long cId, Name name) {
		T c = get(cId);
		List<Name> cNames = c.getNames();

		// if there is no other name in the names locale then make this name
		// the default name.
		if (findCommonName(cNames, name.getLocale()) == null) {
			name.setDefName(true);
		}
		
		// if the new name is a default common name then 
		// set all other common names of this language to non-default
		if (name.isDefName() && name.getType().equals(Name.TYPE_COMMON)) {
			log.debug("Setting new name as default.");
			for (Name n : cNames) {
				if (name.getLocale().equals(n.getLocale()) && n.isCommon()) {
					n.setDefName(false);
				}
			}
		}
		
		cNames.add(name);
		c.setModified(new Date());
		c.setNames(cNames);
		
		return c;
		// save(c);
	}

	public T editName(Long cId, Name name) {
		T c = get(cId);
		List<Name> cpNames = c.getNames();
		
		// if the new name is a default common name then 
		// set all other common names of this language to non-default
		if (name.isDefName() && name.getType().equals(Name.TYPE_COMMON)) {
			log.debug("Setting edited name as default");
			for (Name n : cpNames) {
				if (name != n && name.getLocale().equals(n.getLocale()) && n.isCommon()) {
					n.setDefName(false);
				}
			}
		}
		
		c.setNames(cpNames);
		c.setModified(new Date());
		
		return c;
	}

	@SuppressWarnings("unchecked")
	public List mapToLocale(Collection list, String language) {	
		ArrayList mappedList = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Competency c = (Competency) iterator.next();
			c.setName(getName(c, language));
			mappedList.add(c);
		}
		return mappedList;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNameExact(String token, String language) {
		List<T> dbList = genericCompetencyDao.findByNameExact(token, language);	
		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNameExact(String token, String language, int number) {
		List<T> dbList = genericCompetencyDao.findByNameExact(token, language, number);
		return mapToLocale(dbList, language);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByNameLike(String token, String language) {
		List<T> dbList = genericCompetencyDao.findByNameLike(token, language);
		Collections.sort(dbList, 
				new DefaultCommonNamesByLanguageCompetencyComparator(language));

		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByNameLike(String token, String language, int number) {
		List<T> dbList = genericCompetencyDao.findByNameLike(token, language, number);
		return mapToLocale(dbList, language);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByName(String token, String language, boolean like, int number) {
		List<T> dbList = genericCompetencyDao.findByName(token, language, like, number);
		return mapToLocale(dbList, language);
	}
}
