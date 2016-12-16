package com.system;

import java.util.Collection;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.system.config.SystemWebConfig;
import com.system.config.PropertyConfig;
import com.system.task.TaskManager;


@Configuration
@SpringBootApplication
@Import({PropertyConfig.class,SystemWebConfig.class})
public class SystemWebApplication extends WebMvcAutoConfiguration {

	public static int port = 8081;

	public static void main(String[] args) {
		try {
			String portNumString = System.getProperty("port");
			if (portNumString != null)
				port = Integer.parseInt(portNumString.trim());
		} catch (Exception e) {
		}
		final ApplicationContext ctx = SpringApplication.run(SystemWebApplication.class, args);
		initStaticField(ctx);
		
		TaskManager taskManager = ctx.getBean(TaskManager.class);
		taskManager.runLocalCacheExpire(60);
	}
	
	private static void initStaticField(ApplicationContext ctx) {
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory(port);
		factory.addServerCustomizers(new JettyServerCustomizer() {
			@Override
			public void customize(Server server) {
				final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
				threadPool.setMaxThreads(200);//设置最大线程数量
				threadPool.setMinThreads(10);//设置最小线程数量
				threadPool.setIdleTimeout(300 * 1000);//设置空闲超时时间为300s
				for (Connector connector : server.getConnectors()) {
					if (connector instanceof ServerConnector) {
						Collection<ConnectionFactory> collection = connector.getConnectionFactories();
						for (ConnectionFactory connectionfactory : collection) {
							if (connectionfactory instanceof HttpConnectionFactory) {
								// HttpConnectionFactory
								// httpConnectionFactory=(HttpConnectionFactory)connectionfactory;
								// HttpConfiguration
								// httpConfig=httpConnectionFactory.getHttpConfiguration();
								// httpConnectionFactory.setInputBufferSize(1024*256);
							}
						}

					}
				}

			}
		});
		return factory;
	}
}
