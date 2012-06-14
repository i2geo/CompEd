/**
 * 
 */
package net.i2geo.comped.model.compare;

import java.util.Comparator;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;

/**
 * @author mhomik
 *
 */
public class DefaultCommonNamesByLanguageCompetencyComparator implements Comparator<Competency> {

	private String language;
	
	public DefaultCommonNamesByLanguageCompetencyComparator(String language) {
		this.language = language;
	}
	
	public int compare(Competency c1, Competency c2) {
	    String s1 = getDefaultName(c1, language).toUpperCase();
	    String s2 = getDefaultName(c2, language).toUpperCase();
	    return s1.compareTo(s2);
	}

	private String getDefaultName(Competency c1, String language) {
		for (Name name : c1.getNames()) {
			if (name.isCommon() && name.isDefName() && name.getLocale().equals(language)) {
				return name.getName();
			}
		}
		
		return "zzz";
	}

	
}
