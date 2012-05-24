/**
 * 
 */
package net.i2geo.comped.model.compare;

import java.util.Comparator;

import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Topic;

/**
 * @author mhomik
 *
 */
public class DefaultCommonNamesByLanguageTopicComparator implements Comparator<Topic> {

	private String language;
	
	public DefaultCommonNamesByLanguageTopicComparator(String language) {
		this.language = language;
	}
	
	public int compare(Topic t1, Topic t2) {
	    String s1 = getDefaultName(t1, language).toUpperCase();
	    String s2 = getDefaultName(t2, language).toUpperCase();
	    return s1.compareTo(s2);
	}

	private String getDefaultName(Topic t, String language) {
		for (Name name : t.getNames()) {
			if (name.isCommon() && name.isDefName() && name.getLocale().equals(language)) {
				return name.getName();
			}
		}
		
		return "zzz";
	}

	
}
