/**
 * 
 */
package net.i2geo.comped.util;

import java.util.Date;
import java.util.Map;
import java.text.ParseException;

import ognl.DefaultTypeConverter;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Convert timestamp to String and vis versa.
 * @author dusty
 * Found here: http://www.nabble.com/Convert-Timestamp-with-DateConverter-to19020744s2369.html
 */
public class DateConverter extends DefaultTypeConverter {
    
	Log log = LogFactory.getLog(DateConverter.class);

	public Object convertValue(Map map, Object object, Class aClass) {
		
		// set standard format
		String[] parsePatterns = {"MM/dd/yyyy", "dd-MMM-yyyy",  "MM.dd.yyyy", "mm/dd/yy"};
		
		FastDateFormat df =  FastDateFormat.getInstance(parsePatterns[1]);

		if (aClass == Date.class) {
			// Get first value in parameters array
			String source = ((String[]) object)[0];
			Date transfer;
			
			try {
                transfer = DateUtils.parseDate(source, parsePatterns);
                return transfer;
            } catch (ParseException e) {
                throw new RuntimeException("Cannot convert " + source  + " to calendar type");
            }
		} else if (aClass == String.class) {
			Date o = (Date) object;
			return df.format(o);
		}
		return null;
	}
}

