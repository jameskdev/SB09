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
public class SecondFilter implements Filter{
    
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("SecondFilter init");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("Query string is" + ((HttpServletRequest) request).getQueryString() + "!");
        chain.doFilter(request, response);
        log.info("Secondfilter response");
    }

    @Override
    public void destroy() {
        log.info("SecondFilter destroy");
    }
    
}
