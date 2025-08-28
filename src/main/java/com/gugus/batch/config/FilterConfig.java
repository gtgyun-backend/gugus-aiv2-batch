package com.gugus.batch.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class FilterConfig implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        if (servletRequest.getContentType() == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (servletRequest.getContentType().startsWith("multipart/form-data")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            MultiReadableRequest wrappedRequest = new MultiReadableRequest((HttpServletRequest) servletRequest);
            filterChain.doFilter(wrappedRequest, servletResponse);
        }
    }
}
