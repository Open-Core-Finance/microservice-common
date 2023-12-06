package tech.corefinance.common.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import tech.corefinance.common.enums.CommonConstants;

import java.util.Locale;

@Slf4j
public class StatelessLocaleResolver extends AbstractLocaleContextResolver implements LocaleResolver {

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        var localeKey = request.getHeader(CommonConstants.LANGUAGE_HEADER_KEY);
        if (localeKey == null) {
        	localeKey = CommonConstants.DEFAULT_LANGUAGE_HEADER;
            log.debug("No local in header, used to default locale [{}]", localeKey);
        } else {
            log.debug("Resolved local in header [{}]", localeKey);
        }
        Locale locale = new Locale(localeKey);
        return new SimpleTimeZoneAwareLocaleContext(locale, getDefaultTimeZone());
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        // DO nothing because we don't keep state.
    }

}
