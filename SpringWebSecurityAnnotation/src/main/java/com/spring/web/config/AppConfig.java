package com.spring.web.config;

import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import com.spring.web.security.filter.JwtAuthenticationTokenFilter;

@EnableWebMvc
@Configuration
@ComponentScan({ "com.spring.web.*" })
@Import({WebSecurityConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(
	            getServletWebArgumentResolverAdapter(
	                getDeviceWebArgumentResolver()));
		super.addArgumentResolvers(argumentResolvers);
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getDeviceResolverHandlerInterceptor());
    }
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setLocation(new ClassPathResource("application.properties"));
        return c;
    }
	
	@Bean
	public ServletWebArgumentResolverAdapter getServletWebArgumentResolverAdapter(DeviceWebArgumentResolver deviceWebArgumentResolver){
		return new ServletWebArgumentResolverAdapter(deviceWebArgumentResolver);
	}
	
	@Bean
	public DeviceWebArgumentResolver getDeviceWebArgumentResolver(){
		return new DeviceWebArgumentResolver();
	}
	
	@Bean
	public DeviceResolverHandlerInterceptor getDeviceResolverHandlerInterceptor(){
		return new DeviceResolverHandlerInterceptor();
	}
	
	 @Bean(name="sessionFactory")
     public SessionFactory sessionFactory() {
		LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
		builder.scanPackages("com.spring.web.security.entity").addProperties(getHibernateProperties());

		return builder.buildSessionFactory();
     }

	private Properties getHibernateProperties() {
             Properties prop = new Properties();
             prop.put("hibernate.format_sql", "true");
             prop.put("hibernate.show_sql", "true");
             prop.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
             return prop;
     }

	@Bean(name = "dataSource")
	public BasicDataSource dataSource() {

		BasicDataSource ds = new BasicDataSource();
	    ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/auth01");
		ds.setUsername("phoud");
		ds.setPassword("Qf48d8uv12!@");
		ds.setInitialSize(3);
		return ds;
	}

	//Create a transaction manager
	@Bean
     public HibernateTransactionManager txManager() {
		HibernateTransactionManager tx = new HibernateTransactionManager();
		tx.setDataSource(dataSource());
		tx.setSessionFactory(sessionFactory());
        return tx;
     }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
	
}