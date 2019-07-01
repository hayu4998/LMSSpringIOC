package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;

@Configuration 
public class LMSConfig {
	
	public String driverName = "com.mysql.cj.jdbc.Driver";
	public String url = "jdbc:mysql://localhost:3306/library?useSSL=true";
	public String username = "root";
	public String password = "Yh19930718";
	
	@Bean
	public BasicDataSource mysqlDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	public JdbcTemplate mysqlTemplate() {
		return new JdbcTemplate(mysqlDataSource());
	}
	
	@Bean
	public BookDAO bdao() {
		return new BookDAO();
	}
	
	@Bean
	public AuthorDAO adao() {
		return new AuthorDAO();
	}
	
	@Bean
	public BookCopiesDAO bcdao() {
		return new BookCopiesDAO();
	}
	
	@Bean
	public BookLoansDAO bldao() {
		return new BookLoansDAO();
	}
	
	@Bean
	public GenreDAO gdao() {
		return new GenreDAO();
	}
	
	@Bean
	public LibraryBranchDAO lbdao() {
		return new LibraryBranchDAO();
	}
	
	@Bean
	public PublisherDAO pdao() {
		return new PublisherDAO();
	}
	
	@Bean
	public BorrowerDAO bordao() {
		return new BorrowerDAO();
	}
	
}
