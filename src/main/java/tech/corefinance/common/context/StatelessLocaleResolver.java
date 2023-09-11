package tech.corefinance.common.context;

import tech.corefinance.common.enums.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

public class StatelessLocaleResolver extends AbstractLocaleContextResolver implements LocaleResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        var localeKey = request.getHeader(CommonConstants.LANGUAGE_HEADER_KEY);
        if (localeKey == null) {
        	localeKey = CommonConstants.DEFAULT_LANGUAGE_HEADER;
            logger.debug("No local in header, used to default locale [{}]", localeKey);
        } else {
            logger.debug("Resolved local in header [{}]", localeKey);
        }
        Locale locale = new Locale(localeKey);
        return new SimpleTimeZoneAwareLocaleContext(locale, getDefaultTimeZone());
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        // DO nothing because we don't keep state.
    }

}
