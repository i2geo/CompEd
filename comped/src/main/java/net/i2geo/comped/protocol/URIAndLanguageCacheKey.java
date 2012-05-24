package net.i2geo.comped.protocol;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.opensymphony.oscache.web.filter.ICacheKeyProvider;
import org.appfuse.webapp.filter.UserLocaleFilter;
import javax.servlet.http.HttpServletRequest;

public class URIAndLanguageCacheKey implements ICacheKeyProvider {

    public String createCacheKey(HttpServletRequest httpServletRequest, ServletCacheAdministrator servletCacheAdministrator, Cache cache) {
        (LocaleRequestWrapper) httpServletRequest.getSession();
    }
}
