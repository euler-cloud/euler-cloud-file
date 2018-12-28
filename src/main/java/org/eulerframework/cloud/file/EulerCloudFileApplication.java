package org.eulerframework.cloud.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
@EnableDiscoveryClient
public class EulerCloudFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(EulerCloudFileApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ShallowEtagHeaderFilter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        registrationBean.setFilter(shallowEtagHeaderFilter);
        Collection<String> urlPatterns = new HashSet<>();
        urlPatterns.add("/archived/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
