/**
 * 
 */
package net.i2geo.comped.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Nameable;

/**
 * This class comprises help methods for working with names.
 * 
 * @author Martin Homik
 *
 */
public class NameUtil {

	/**
	/*
	 * Returns a list of names of a nameable instance. The names are relative to the type and locale,
	 * and they are restricted by enabling values.
	 * If no type is provided then the type test will be ignored. If no 
	 * locale is provided then the locale test will be ignored. If neither
	 * type nor locale is provided and isDefault is set to true, then all
	 * default names will be returned.
	 * 
	 * TODO: Doesn't care about isDefault!!!
	 * 
	 * @param nameable
	 * @param types
	 * @param locales
	 * @param isDefault
	 * @return
	 * 
	 */
	public static List<Name> findNames(Nameable nameable, Set<String> types, Set<String> locales, boolean isDefault){
		List<Name> names = new ArrayList<Name>();
		
		for (Iterator<Name> iterator = nameable.getNames().iterator(); iterator.hasNext();) {
			Name name = (Name) iterator.next();
			
			if (types != null & !types.isEmpty() & !types.contains(name.getType())){
				continue;
			}
			if (locales != null & !locales.isEmpty() & !locales.contains(name.getLocale())){
				continue;
			}
			names.add(name);
		}
		return names;
	}
	
	/**
	 * TODO: Doesn't care about isDefault!!!
	 * 
	 * @param nameable
	 * @param types
	 * @param locales
	 * @param isDefault
	 * @return
	 */
	public static List<Name> findOtherNames(Nameable nameable, Set<String> types, Set<String> locales, boolean isDefault){
		List<Name> names = new ArrayList<Name>();
		
		for (Iterator<Name> iterator = nameable.getNames().iterator(); iterator.hasNext();) {
			Name name = (Name) iterator.next();
			
			if (types != null & !types.isEmpty() & !types.contains(name.getType())){
				continue;
			}
			if (locales != null & !locales.isEmpty() & locales.contains(name.getLocale())){
				continue;
			}
			names.add(name);
		}
		return names;
	}
	
	/**
	 * Find names of Nameable instances where locale and type are limited to single instances.
	 * 
	 * @param nameable
	 * @param type
	 * @param locale
	 * @param isDefault
	 * @return
	 */
	public static List<Name> findNames(Nameable nameable, String type, String locale, boolean isDefault){
		Set<String> types   = new HashSet<String>();
		Set<String> locales = new HashSet<String>();

		// add type if not null
		if (type != null & type != "") {
			types.add(type);
		}
		
		if (locale != null & locale != "" ) {
			locales.add(locale);
		}
		
		return NameUtil.findNames(nameable, types, locales, isDefault);		
	}

	public static List<Name> findOtherNames(Nameable nameable, String type, String locale, boolean isDefault){
		Set<String> types   = new HashSet<String>();
		Set<String> locales = new HashSet<String>();

		// add type if not null
		if (type != null & type != "") {
			types.add(type);
		}
		
		if (locale != null & locale != "" ) {
			locales.add(locale);
		}
		
		return NameUtil.findOtherNames(nameable, types, locales, isDefault);		
	}

	
	public static List<Name> getNamesByLocale(Nameable nameable, String locale){
		List<Name> result = new ArrayList<Name>();
		List<Name> names   = nameable.getNames();
		
		for (Iterator<Name> iterator = names.iterator(); iterator.hasNext();) {
			Name name = (Name) iterator.next();
			if (name.getLocale().equals(locale)) {
				result.add(name);
			}
		}
		return result;
	}
	
}
