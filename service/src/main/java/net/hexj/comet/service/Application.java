package net.hexj.comet.service;

import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Spring boot 应用配置
 */
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"net.hexj.comet.service"})
@PropertySources(value = {@PropertySource({"classpath:service.properties"})})
@MapperScan(basePackages = "net.hexj.comet.service.mapper")
public class Application {
  @Autowired
  public Environment env;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public DataSource dataSource() throws PropertyVetoException {
    ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
    comboPooledDataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
    comboPooledDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
    comboPooledDataSource.setUser(env.getProperty("jdbc.username"));
    comboPooledDataSource.setPassword(env.getProperty("jdbc.password"));
    comboPooledDataSource.setAutoCommitOnClose(false);
    comboPooledDataSource.setCheckoutTimeout(env.getProperty("checkoutTimeout", Integer.class,
        10000));
    comboPooledDataSource.setInitialPoolSize(env.getProperty("minPoolSize", Integer.class, 3));
    comboPooledDataSource.setMinPoolSize(env.getProperty("minPoolSize", Integer.class, 3));
    comboPooledDataSource.setMaxPoolSize(env.getProperty("maxPoolSize", Integer.class, 100));
    comboPooledDataSource.setMaxIdleTime(env.getProperty("maxIdleTime", Integer.class, 600));
    comboPooledDataSource.setPreferredTestQuery(env.getProperty("preferredTestQuery",
        "select 1 from dual"));
    comboPooledDataSource.setIdleConnectionTestPeriod(env.getProperty("idleConnectionTestPeriod",
        Integer.class, 10));
    comboPooledDataSource.setTestConnectionOnCheckin(env.getProperty("testConnectionOnCheckin",
        Boolean.class, true));
    comboPooledDataSource.setMaxIdleTimeExcessConnections(env.getProperty(
        "maxIdleTimeExcessConnections", Integer.class, 300));
    return comboPooledDataSource;
  }

  @Bean
  public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource)
      throws PropertyVetoException, IOException {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] mapperLocations = resolver.getResources("classpath:mybatis/**/*.xml");
    sqlSessionFactoryBean.setMapperLocations(mapperLocations);
    return sqlSessionFactoryBean;
  }

  @Bean
  public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource)
      throws PropertyVetoException {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    dataSourceTransactionManager.setDataSource(dataSource);

    return dataSourceTransactionManager;
  }

  @Bean
  public SqlSession getSqlSessionTemplate(SqlSessionFactoryBean sqlSessionFactoryBean)
      throws Exception {
    SqlSessionTemplate sqlSessionTemplate =
        new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
    return sqlSessionTemplate;
  }
}
