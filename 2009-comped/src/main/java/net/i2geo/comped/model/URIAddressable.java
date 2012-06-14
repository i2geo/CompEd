/**
 * 
 */
package net.i2geo.comped.model;

/**
 * @author Martin
 *
 */
public interface URIAddressable {
	
	public String getUri(); 
	public String getUriSuffix();
	public String getUriSuffix(String match);
}
