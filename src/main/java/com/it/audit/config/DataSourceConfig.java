package com.it.audit.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	
	public static final String DATASOUCE_DEFAULT = "defaultDataSource";
	
	@Primary
	@Bean(name = DATASOUCE_DEFAULT)
	@ConfigurationProperties(prefix = "db.default")
	public DataSource primaryDataSource() {
		return buildDataSource(false);
	}
	
	@Bean(name = "secondDataSource")
	@ConfigurationProperties(prefix = "db.secondary")
	public DataSource secondaryDataSource() {
		return null;
	}

	private DataSource buildDataSource(Boolean defaultReadOnly) {
		final org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setInitialSize(10);
		dataSource.setMaxActive(20);
		dataSource.setMaxIdle(5);
		dataSource.setMinIdle(1);
		dataSource.setValidationQuery("SELECT 1");
		//dataSource.setValidationInterval(1 * 60 * 1000);
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(true);
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnConnect(true);
		dataSource.setLogAbandoned(true);
		dataSource.setRemoveAbandoned(true);
		dataSource.setRemoveAbandonedTimeout(60);
		dataSource.setMaxAge(4 * 60 * 1000);
		dataSource.setMaxWait(60 * 1000);
		dataSource.setMinEvictableIdleTimeMillis(1 * 60 * 1000);
		dataSource.setTimeBetweenEvictionRunsMillis(2 * 60 * 1000);
		dataSource.setDefaultReadOnly(defaultReadOnly);
		return dataSource;
	}
}
