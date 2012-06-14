/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;
import net.i2geo.comped.service.NameManager;
import net.i2geo.comped.service.SearchI2GManager;
import net.i2geo.comped.util.NameUtil;
import net.i2geo.comped.util.UserUtil;

import org.appfuse.model.User;
import org.appfuse.webapp.action.BaseAction;
import org.springframework.security.AccessDeniedException;

/**
 * @author Martin Homik
 *
 */
abstract public class NameableAction extends BaseAction {

	private static final long serialVersionUID = -7891367813464926553L;

	// managers
	protected NameManager nameManager;
	protected SearchI2GManager searchI2GManager;
	
	// current nameable object
    private Nameable nameable;

    // nameable names in default language
    private List<Name> defaultLangCommonNames      = new ArrayList<Name>();
    private List<Name> defaultLangUncommonNames    = new ArrayList<Name>();
    private List<Name> defaultLangRareNames        = new ArrayList<Name>();
    private List<Name> defaultLangFalseFriendNames = new ArrayList<Name>();

    // nameable names in default language
    private List<Name> otherLangCommonNames        = new ArrayList<Name>();
    private List<Name> otherLangUncommonNames      = new ArrayList<Name>();
    private List<Name> otherLangRareNames          = new ArrayList<Name>();
    private List<Name> otherLangFalseFriendNames   = new ArrayList<Name>();

    private String oldDefaultCommonName = "";
    
    /********************************
     * Inject manager beans
     ********************************/
    public void setNameManager(NameManager nameManager) {
    	this.nameManager = nameManager;
    }
    
    public void setSearchI2GManager(SearchI2GManager searchI2GManager) {
    	this.searchI2GManager = searchI2GManager;
    }

    public SearchI2GManager getSearchI2GManager() {
    	return searchI2GManager;
    }
    
    protected void setNameable(Nameable nameable) {
    	this.nameable = nameable;
    }

    protected Nameable getNameable(){
    	return nameable;
    }
    
    protected void findAllDefaultLangNames(){
    	findDefaultLangCommonNames();
    	findDefaultLangUncommonNames();
    	findDefaultLangRareNames();
    	findDefaultLangFalseFriendNames();
    }

    protected void findAllOtherLangNames(){
    	findOtherLangCommonNames();
    	findOtherLangUncommonNames();
    	findOtherLangRareNames();
    	findOtherLangFalseFriendNames();
    }
    
    protected void findDefaultLangCommonNames() {
    	String locale = getCurrentLocale().getLanguage();
       	this.defaultLangCommonNames = 
       		NameUtil.findNames(this.nameable, Name.TYPE_COMMON, locale, false);
    }

    protected void findDefaultLangUncommonNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.defaultLangUncommonNames =
    		NameUtil.findNames(this.nameable, Name.TYPE_UNCOMMON, locale, false);
    }

    protected void findDefaultLangRareNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.defaultLangRareNames = 
    		NameUtil.findNames(this.nameable, Name.TYPE_RARE, locale, false);
    }

    protected void findDefaultLangFalseFriendNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.defaultLangFalseFriendNames =
    		NameUtil.findNames(this.nameable, Name.TYPE_FALSEFRIEND, locale, false);
    }

    public List<Name> getDefaultLangCommonNames() {
		return defaultLangCommonNames;
	}

	public List<Name> getDefaultLangUncommonNames() {
		return defaultLangUncommonNames;
	}

	public List<Name> getDefaultLangRareNames() {
		return defaultLangRareNames;
	}

	public List<Name> getDefaultLangFalseFriendNames() {
		return defaultLangFalseFriendNames;
	}    

    private void findOtherLangCommonNames() {
    	String locale = getCurrentLocale().getLanguage();
       	this.otherLangCommonNames = 
       		NameUtil.findOtherNames(this.nameable, Name.TYPE_COMMON, locale, false);
    }

    private void findOtherLangUncommonNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.otherLangUncommonNames =
    		NameUtil.findOtherNames(this.nameable, Name.TYPE_UNCOMMON, locale, false);
    }

    private void findOtherLangRareNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.otherLangRareNames = 
    		NameUtil.findOtherNames(this.nameable, Name.TYPE_RARE, locale, false);
    }

    private void findOtherLangFalseFriendNames() {
   		String locale = getCurrentLocale().getLanguage();
    	this.otherLangFalseFriendNames =
    		NameUtil.findOtherNames(this.nameable, Name.TYPE_FALSEFRIEND, locale, false);
    }

    public List<Name> getOtherLangCommonNames() {
		return otherLangCommonNames;
	}

	public List<Name> getOtherLangUncommonNames() {
		return otherLangUncommonNames;
	}

	public List<Name> getOtherLangRareNames() {
		return otherLangRareNames;
	}

	public List<Name> getOtherLangFalseFriendNames() {
		return otherLangFalseFriendNames;
	}

    protected void setOldDefaultCommonName(String oldDefaultCommonName) {
    	this.oldDefaultCommonName = oldDefaultCommonName;
    }

    protected String getOldDefaultCommonName() {
    	return oldDefaultCommonName;
    }
    
	protected void handleDefaultCommonNameChange(boolean isNewNameable) {
		
		// nameable is freshly created -> give it a default common name
		if (isNewNameable) {
			Name name = new Name();
			name.setName(nameable.getName());
			name.setType(Name.TYPE_COMMON);
			name.setLocale(getCurrentLocale().getLanguage());
			name.setDefName(true);
			nameable.getNames().add(name);
			return;
		}
		
		// otherwise: Nameable already exists; 
		if (!getOldDefaultCommonName().equals(nameable.getName())) {
			Name name = nameManager.findDefaultByThingAndTypeAndLocale(
					nameable, Name.TYPE_COMMON, getCurrentLocale().getLanguage());
			if (name == null) {
				name = new Name();
				name.setName(nameable.getName());
				name.setType(Name.TYPE_COMMON);
				name.setLocale(getCurrentLocale().getLanguage());
				name.setDefName(true);
				nameable.getNames().add(name);
			} else {
				name.setName(nameable.getName());
			}
		}
	}
	
	@Override
	public Locale getLocale() {
		String language = getCurrentLocale().getLanguage();
		try {
			return new Locale(language, "");
		} catch (Exception e) {
			return super.getLocale();
		}
	}
	
	public Locale getCurrentLocale() {
		// NOTE: should be written in some properties file
		Locale defaultLocale = super.getLocale();
		Locale locale = getUsersLocale();
		
		if (locale == null) {
			return defaultLocale;
		}
		return locale;
	}
	
	private Locale getUsersLocale() {
		try {
			User currentUser = UserUtil.getUserFromSecurityContext();
			if (currentUser != null) {
				return new Locale(currentUser.getLanguage());
			}
			return null;
		} catch (AccessDeniedException e) {
			return null;
		}
    }

}


