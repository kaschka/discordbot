package org.kaschka.fersagers.discord.configuration;

import org.kaschka.fersagers.discord.logging.RequestLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "org.kaschka.fersagers" })
@EntityScan(basePackages = { "org.kaschka.fersagers" })
@ComponentScan(basePackages = { "org.kaschka.fersagers" })
public class ApplicationConfiguration {

    public static void main(String[] args) {
        new SpringApplication(ApplicationConfiguration.class).run();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        return restTemplate;
    }

    @Bean
    public FilterRegistrationBean<RequestLogger> filterRegistrationBean() {
        FilterRegistrationBean<RequestLogger> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLogger());
        return registrationBean;
    }
}
