package org.xm.sb09.configurations;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xm.sb09.filters.FirstFilter;
import org.xm.sb09.filters.SecondFilter;

import jakarta.servlet.Filter;

/* All the filter-related configurations go here
 * These include:
 *  * Registering filters as bean
 *  * Defining the order in which the filter will be applied
 *  * Specifying the URLs for which the filter will be applied
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> firstFilter() {
        FilterRegistrationBean<Filter> firstFilter = new FilterRegistrationBean<>();
        firstFilter.setFilter(new FirstFilter());
        firstFilter.setOrder(1);
        firstFilter.addUrlPatterns("/test");
        return firstFilter;
    }

    @Bean
    public FilterRegistrationBean<Filter> secondFilter() {
        FilterRegistrationBean<Filter> secondFilter = new FilterRegistrationBean<>();
        secondFilter.setFilter(new SecondFilter());
        secondFilter.setOrder(2);
        secondFilter.addUrlPatterns("/test");
        return secondFilter;
    }
}
