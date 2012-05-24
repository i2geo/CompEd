/**
 * 
 */
package net.i2geo.comped.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import ognl.DefaultTypeConverter;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convert timestamp to String and vis versa.
 * @author dusty
 * Found here: http://www.nabble.com/Convert-Timestamp-with-DateConverter-to19020744s2369.html
 *
 */
public class CalendarConverter extends DefaultTypeConverter {
    
	Log log = LogFactory.getLog(CalendarConverter.class);

	public Object convertValue(Map map, Object object, Class aClass) {
		// standard format
		String[] parsePatterns = {
                "MM/dd/yyyy hh:mm a",
                "MM/dd/yyyy hh:mm:ss a",
                "dd-MMM-yyyy hh:mm a",
                "dd-MMM-yyyy hh:mm:ss a",
                "MM/dd/yyyy HH:mm",
                "MM/dd/yyyy HH:mm:ss",
                "dd-MMM-yyyy HH:mm",
                "dd-MMM-yyyy HH:mm:ss",
        };
		
		FastDateFormat df =  FastDateFormat.getInstance(parsePatterns[0]);
		if (aClass == Calendar.class) {
			// Get first value in parameters array
			String source = ((String[]) object)[0];		
			// Create target Calendar object
			Calendar returnCal = new GregorianCalendar();
            Date transfer;
            try {
            	// Call Commons DateUtils parse with array of patterns
            	// Currently only one pattern that forces the time to be present.
            	// Could include a MM/dd/yyyy pattern but you
            	// should use a java.util.Date object for that type
            	transfer = DateUtils.parseDate(source, parsePatterns);
                returnCal = new GregorianCalendar();
                returnCal.setTime(transfer);
                return returnCal;
            } catch (ParseException e) {
                throw new RuntimeException("Cannot convert " + source + " to calendar type");
            }
        } else if (aClass == String.class) {
            Calendar o = (Calendar) object;
            log.debug(o.getTime());
            return df.format(o.getTime());
        }
		return null;
	}
}
