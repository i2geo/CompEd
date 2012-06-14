/**
 * 
 */
package net.i2geo.comped.util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.appfuse.model.User;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

/**
 * Get the current user from SecrityContext.
 * 
 * @author Martin Homik
 *
 */
public class UserUtil {

	public static User getUserFromSecurityContext() {
		User currentUser = null;
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx.getAuthentication() != null) {
			Authentication auth = ctx.getAuthentication();
			if (auth.getPrincipal() instanceof UserDetails) {
				currentUser = (User) auth.getPrincipal();
			} else if (auth.getDetails() instanceof UserDetails) {
				currentUser = (User) auth.getDetails();
			} else {
				throw new AccessDeniedException("User not properly authenticated.");
			}
		} 
		
		return currentUser;
	}
	
	/**
	 * Get the user from currentSession. 
	 * NOTE: Is there any entryPoint?
	 * 
	 * @return
	 */
	public static String getUserFromSession() {
		String currentUser = null;
		return currentUser;
	}
}
