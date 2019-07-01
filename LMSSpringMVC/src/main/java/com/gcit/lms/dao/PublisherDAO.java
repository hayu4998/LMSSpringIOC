/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> implements ResultSetExtractor<List<Publisher>>{

	public Integer add(Publisher publisher) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update( connection->{
			PreparedStatement ps = connection.prepareStatement("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, publisher.getPublisherName());
			ps.setString(2, publisher.getPublisherAddress());
			ps.setString(3, publisher.getPublisherPhone());
			return ps;
		},keyHolder);
		Integer key	= keyHolder.getKey().intValue();	
		return key;
	}

	public void update(Publisher publisher) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update(
				"update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?", 
				new Object[]{
						publisher.getPublisherName(),
						publisher.getPublisherAddress(),
						publisher.getPublisherPhone(),
						publisher.getPublisherId()
				}
		);
	}

	public void delete(Publisher publisher) throws ClassNotFoundException, SQLException {
		
		//check if exist book
		
//		if(publisher.getBooks().size() != 0) {
//			System.out.println("The publisher has coresponed book exist, do you still want to delete it?[Y/n]");
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}
		
		mysqlTemplate.update(
				"delete from tbl_publisher where publisherId = ?", 
				new Object[]{ publisher.getPublisherId()}
		);
	}
	
	public List<Publisher> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_publisher", this);
	}
	
	public Publisher readPublisherByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		List<Publisher> pubList = mysqlTemplate.query("select * from tbl_publisher where publisherId = (select pubId from tbl_book where bookId = ?)", new Object[] {bookId}, this);
		
		if(pubList.size() == 0) {
			return null;
		}else {
			return pubList.get(0);
		}
	}
	
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<Publisher>();
		while(rs.next()){
			Publisher publisher = new Publisher();
			publisher.setPublisherId(rs.getInt("publisherId"));
			publisher.setPublisherName(rs.getString("publisherName"));
			publisher.setPublisherAddress(rs.getString("publisherAddress"));
			publisher.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(publisher);
		}
		return publishers;
	}

	public Publisher readPublisherById(Integer publisherId) {
		return mysqlTemplate.query("select * from tbl_publisher where publisherId = ?", new Object[] {publisherId}, this).get(0);
	}

}
