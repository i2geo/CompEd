/**
 * 
 */
package net.i2geo.comped.util;

import java.util.Collection;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.URIAddressable;

/**
 * Some helper methods for Action classes.
 * 
 * @author Martin Homik
 *
 */
public class ActionUtil {
	
	
	public static String cCollection2String(Collection<? extends Competency> uriAddressables) {
		String result = "";
		
		// if no competencies/topics are passed return the empty string
		if (uriAddressables == null || uriAddressables.isEmpty()) {
			return result;
		}
		
		// otherwise iterate through the collection
		for (Object o : uriAddressables) {
			URIAddressable i = (URIAddressable) o;
			result+=i.getUriSuffix() + ", ";
		}

		// ... and finally add array brackets
		result = "[" + result.substring(0, result.length()-2) + "]";

		return result;
	}

	public static String tCollection2String(Collection<? extends Topic> uriAddressables) {
		String result = "";

		// if no competencies/topics are passed return the empty string
		if (uriAddressables == null || uriAddressables.isEmpty()) {
			return result;
		}

		// otherwise iterate through the collection
		for (Object o : uriAddressables) {
			if (o instanceof AbstractTopic) {
				AbstractTopic t = (AbstractTopic) o;
				result+=t.getUriSuffix(null)+"_r, ";
			} else {
				ConcreteTopic t = (ConcreteTopic) o;
				result+=t.getUriSuffix(null) + ", ";
			}
		}

		// ... and finally add array brackets
		result = "[" + result.substring(0, result.length()-2) + "]";
		
		return result;
		
	}
	
    public static String correctSKBInput(String storage) {
    	String dummy = storage.replace("#", "");
    	dummy = dummy.replace(",", ", ");

    	return dummy;
    }
    
    public static boolean isAlphaNumeric(final String s) {
    	  final char[] chars = s.toCharArray();
    	  for (int x = 0; x < chars.length; x++) {      
    	    final char c = chars[x];
    	    if ((c >= 'a') && (c <= 'z')) continue; // lowercase
    	    if ((c >= 'A') && (c <= 'Z')) continue; // uppercase
    	    if ((c >= '0') && (c <= '9')) continue; // numeric
    	    if (c == '_') continue;                   // allow underscores
    	    return false;
    	  }  
    	  return true;
    	}


}