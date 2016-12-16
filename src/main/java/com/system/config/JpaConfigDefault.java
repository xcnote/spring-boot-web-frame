package com.system.config;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(PropertyConfig.class)
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "defaultEntityManagerFactory", 
						transactionManagerRef = DataSourceConfig.DATASOUCE_DEFAULT, 
						 value = { "com.system.persistence.dao" })
@Slf4j
@EnableTransactionManagement
public class JpaConfigDefault {

	@Value("${hibernate.sql.database}")
    private String hibernateDatabase;
	
	@Value("${hibernate.sql.generateddl}")
	private Boolean hibernateGenerateDdl;

	@Value("${hibernate.sql.show}")
	private Boolean hibernateShowSql;

	@Autowired
    private JpaProperties jpaProperties;
	
	@Autowired 
	@Qualifier(DataSourceConfig.DATASOUCE_DEFAULT)
    private DataSource defaultDataSource;
	
	@Primary
	@Bean(name = "defaultServerEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactory(builder).getObject().createEntityManager();
    }

	@Primary
	@Bean(name = "defaultEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
		log.info("JPA config for default ... ");
		LocalContainerEntityManagerFactoryBean factory = builder.dataSource(defaultDataSource)
				.properties(jpaProperties.getHibernateProperties(defaultDataSource))
				.packages("com.system.domain") //设置实体类所在位置
				.persistenceUnit("default-persistence-unit")
                 .build();
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.valueOf(hibernateDatabase));
		jpaVendorAdapter.setGenerateDdl(hibernateGenerateDdl);
		jpaVendorAdapter.setShowSql(hibernateShowSql);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		return factory;
	}


	@Bean(name = "defaultTransactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactory(builder).getObject());
	}
	
	/**
	 * 将JPA异常统一翻译成Spring的数据访问异常体系中的某个异常
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	/**
	 * 使用注解来声明EntityManager时需要使用
	 * @PersistenceContext  
     * private EntityManager entityManager;  
	 */
	@Bean
    public static PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }
}
