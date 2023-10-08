package tech.corefinance.common.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "tech.corefinance.common.enabled", name = "auto-response-encoding")
@Slf4j
public class EncodingHeaderFilter implements Filter, Ordered {

    @Value("${tech.corefinance.common.default-response-charset:utf-8}")
    private String charset;
    @Value("${tech.corefinance.common.filter-ordered.encoding-response-order:0}")
    private int order;

    private static final List<String> TEXT_CONTENT_TYPES = List.of(
            MediaType.TEXT_HTML_VALUE,
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_XHTML_XML_VALUE,
            "application/vnd.mozilla.xul+xml",
            "application/vnd.oasis.opendocument.text",
            "application/ld+json"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
        try {
            String contentType = response.getContentType();
            if (contentType == null || !contentType.contains("charset=") && isTextContentType(contentType)) {
                log.debug("Setting encoding charset [{}] to response...", charset);
                response.setCharacterEncoding(charset);
            }
        } catch (Throwable t) {
            log.error("Set encoding error!", t);
        }
    }

    private boolean isTextContentType(String contentType) {
        MediaType mediaType = MediaType.parseMediaType(contentType);
        return mediaType.getType().equals("text") || TEXT_CONTENT_TYPES.contains(contentType);
    }

    @Override
    public int getOrder() {
        return order;
    }
}
