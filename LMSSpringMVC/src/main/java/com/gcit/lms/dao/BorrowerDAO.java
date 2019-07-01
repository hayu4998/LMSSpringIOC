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

import com.gcit.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>>{

	public Integer add(Borrower borrower) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("insert into tbl_borrower (name,address,phone) values (?,?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, borrower.getBorrowerName());
			ps.setString(2, borrower.getBorrowerAddress());
			ps.setString(3, borrower.getBorrowerPhone());
			return ps;
		}, keyHolder);
		Number key = keyHolder.getKey();
		return key.intValue();
	}

	public void update(Borrower borrower) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update(
				"update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?", 
				new Object[]{
						borrower.getBorrowerName(),
						borrower.getBorrowerAddress(),
						borrower.getBorrowerPhone(),
						borrower.getCardId()
						}
		);
	}

	public void delete(Borrower borrower) throws ClassNotFoundException, SQLException {
		
		//check if exist book records
//		BookLoansDAO bdao = new BookLoansDAO(conn);
//		if(bdao.readBookLoansByID("CardNo",borrower.getCardId()).size() != 0) {
//			System.out.println("The Borrower has unreturned book, do you still want to delete it?[Y/n]");
//			
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}
		mysqlTemplate.update(
				"delete from tbl_borrower where cardNo = ?", 
				new Object[]{borrower.getCardId()}
		);
		
	}
	
	public List<Borrower> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_borrower", this);
	}
	
	public Borrower selectBorrowerByID(Integer cardNo) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_borrower where cardNo = ?", new Object[] {cardNo},this).get(0);
	}
	
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<Borrower>();
		while(rs.next()){
			Borrower borrower = new Borrower();
			
			borrower.setCardId(rs.getInt("cardNo"));
			borrower.setBorrowerName(rs.getString("name"));
			borrower.setBorrowerAddress(rs.getString("address"));
			borrower.setBorrowerPhone(rs.getString("phone"));
			
			//add to list
			borrowers.add(borrower);
		}
		return borrowers;
	}

	public List<Borrower> readBorrowerByNameLike(String name) {
		name = '%'+name+'%';
		return mysqlTemplate.query("select * from tbl_borrower where name like ?", new Object[] {name},this);
	}

}
