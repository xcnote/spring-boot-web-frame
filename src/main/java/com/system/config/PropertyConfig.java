package com.system.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * 
 * @author xiaocheng
 * 
 */
@Configuration
@PropertySource(value = { "classpath:system.properties", "file:${user.dir}/system.properties", "file:/etc/system-config/system.properties" }, ignoreResourceNotFound = true)
@Slf4j
public class PropertyConfig {

    /**
     * PropertySourcesPlaceholderConfigurer not PropertyPlaceholderConfigurer
     * 
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        if (log.isDebugEnabled()) {
            log.debug("placeHolderConfigurer init");
        }

        final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);

        return configurer;
    }

    public static String getEnv(final String name, final String defaultValue) {
        final String value = System.getenv(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
