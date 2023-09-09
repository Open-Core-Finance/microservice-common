package com.finance.core.common.filter;

import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "com.finance.core.common.enabled", name = "auto-response-encoding")
public class EncodingHeaderFilter implements Filter {
    @Value("${com.finance.core.common.enabled.auto-response-encoding}")
    private String charset;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contentType = response.getContentType();
        if(contentType == null || !contentType.contains("charset=") && isTextContentType(contentType)){
            response.setCharacterEncoding(charset);
        }
        chain.doFilter(request, response);
    }
    private boolean isTextContentType(String contentType) {
        MediaType mediaType = MediaType.parseMediaType(contentType);
        return mediaType.getType().equals("text") || TEXT_CONTENT_TYPES.contains(contentType);
    }
}
