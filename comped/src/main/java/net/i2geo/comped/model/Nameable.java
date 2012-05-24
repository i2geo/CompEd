/**
 * 
 */
package net.i2geo.comped.model;

import java.util.List;

/**
 * This interface acts as a proxy to competencies and topics. Both have names.
 * 
 * @author Martin Homik
 *
 */
public interface Nameable {

	/**
	 * Get a list of all known names for this nameable object
	 * @return list of allknown names
	 */
	public List<Name> getNames();

	/**
	 * Get the current default name of the object. This name may vary depending on 
	 * the current language. 
	 * @return
	 */
	public String getName();
}
