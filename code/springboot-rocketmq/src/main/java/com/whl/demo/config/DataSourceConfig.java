package com.whl.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.whl.demo.mapper", sqlSessionTemplateRef = "testSqlSessionTemplate")
@ComponentScan
public class DataSourceConfig
{
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean(name = "testDataSource", initMethod = "init", destroyMethod = "close")
	public DruidDataSource dataSource()
	{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setDbType("mysql");
		return dataSource;
	}

	/*@Bean(name = "testDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}*/

	@Bean(name = "testSqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "testTransactionManager")
	public DataSourceTransactionManager testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "testSqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
