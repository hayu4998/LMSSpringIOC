/**
 * 
 */
package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.service.AdminService;

/**
 * @author ppradhan
 *
 */
@Configuration
public class LMSConfig {
	
	public String driverName = "com.mysql.cj.jdbc.Driver";
	public String url = "jdbc:mysql://localhost:3306/library?useSSL=true";
	public String username = "root";
	public String password = "root";
	
	
	@Bean
//	@Scope(value="prototype")
	public BasicDataSource mysqlDataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean
	@Qualifier(value="mysqlTemplate")
	public JdbcTemplate mysqlTemplate(){
		return new JdbcTemplate(mysqlDataSource());
	}
	
	@Bean
//	@Scope(value="prototype")
	public BasicDataSource oracleDataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean
	@Qualifier(value="oracleTemplate")
	public JdbcTemplate oracleTemplate(){
		return new JdbcTemplate(oracleDataSource());
	}
	
	@Bean
	public BookDAO bdao(){
		return new BookDAO();
	}
	
	@Bean
	public AuthorDAO adao(){
		return new AuthorDAO();
	}
	
	@Bean
	public AdminService adminService(){
		return new AdminService();
	}
	//all daos
	
	@Bean
	DataSourceTransactionManager txManager(){
		return new DataSourceTransactionManager(mysqlDataSource());
	}
}
