package org.eulerframework.cloud.file;

import org.eulerframework.cloud.EnableEulerCloud;
import org.eulerframework.web.core.i18n.ClassPathReloadableResourceBundleMessageSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
@EnableEulerCloud
@EnableDiscoveryClient
public class EulerCloudFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(EulerCloudFileApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ClassPathReloadableResourceBundleMessageSource messageSource = new ClassPathReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(10);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasename("classpath*:language/**/*");
        return messageSource;
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
