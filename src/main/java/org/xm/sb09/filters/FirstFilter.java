package org.xm.sb09.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirstFilter implements Filter{
    
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("FirstFilter init");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("A request from IP address: " + request.getRemoteAddr() + "was received!");
        log.info("Request URL is" + ((HttpServletRequest) request).getRequestURI() + "!");
        chain.doFilter(request, response);
        log.info("FirstFilter response");
    }

    @Override
    public void destroy() {
        log.info("FirstFilter destroy");
    }
    
}
