package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.BookLoans;

/**
 * @author Jason
 *
 */
public class BookLoansDAO extends BaseDAO<BookLoans> implements ResultSetExtractor<List<BookLoans>> {

	public void add(BookLoans bookloan) throws ClassNotFoundException, SQLException {

		mysqlTemplate.update(
				"insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?,?,?,now(),now() + interval 7 day)",
				new Object[] { bookloan.getBook().getBookId(), bookloan.getBranch().getLibraryBranchId(),
						bookloan.getBorrower().getCardId() });
	}

	public void borrowBook(Object[] objlist) throws ClassNotFoundException, SQLException {

		mysqlTemplate.update(
				"insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values (?,?,?,now(),now() + interval 7 day)",
				objlist);
	}

	public void update(BookLoans bookLoans, String field) throws ClassNotFoundException, SQLException {

		Object[] fieldlist = null;
		String sql = null;

		if (field.contentEquals("dateIn")) {
			sql = "update tbl_book_loans set dateIn = now() where bookId =? and branchId = ? and CardNo = ? and dateOut = ? ";
			fieldlist = new Object[] {

					bookLoans.getBook().getBookId(), bookLoans.getBranch().getLibraryBranchId(),
					bookLoans.getBorrower().getCardId(), bookLoans.getLoanedDateOut() };
		} else if (field.contentEquals("dueDate")) {
			sql = "update tbl_book_loans set dueDate = dueDate + interval 7 day where bookId = ? and branchId = ? and CardNo = ? and dateOut = ?";
			fieldlist = new Object[] { bookLoans.getBook().getBookId(), bookLoans.getBranch().getLibraryBranchId(),
					bookLoans.getBorrower().getCardId(), bookLoans.getLoanedDateOut() };
		}

		mysqlTemplate.update(sql, fieldlist);
	}

//	public void delete(BookLoans bookLoans) throws ClassNotFoundException, SQLException {
//		
//		mysqlTemplate.update("delete from tbl_book_loans where bookId =? and branchId = ? and dateOut = ?", 
//			new Object[]{
//					bookLoans.getBook().getBookId(),
//					bookLoans.getBranch().getLibraryBranchId(),
//					bookLoans.getBorrower().getCardId()
//			}	
//		);
//	}

	public List<BookLoans> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_loans", this);
	}

	public List<BookLoans> readBookLoansByID(String field, Integer pk) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_loans where " + field + " = ? and dateIn is null",
				new Object[] { pk }, this);
	}

	public List<BookLoans> extractData(ResultSet rs) throws SQLException {
		List<BookLoans> BookLoanss = new ArrayList<BookLoans>();

		while (rs.next()) {
			BookLoans BookLoans = new BookLoans();
			// set dates
			BookLoans.setBookId(rs.getInt("bookId"));
			BookLoans.setCardNo(rs.getInt("cardNo"));
			BookLoans.setBranchId(rs.getInt("branchId"));
			BookLoans.setLoanedDateIn(rs.getString("dateIn"));
			BookLoans.setLoanedDateOut(rs.getString("dateOut"));
			BookLoans.setLoanedDueDate(rs.getString("dueDate"));
			// add to the list
			BookLoanss.add(BookLoans);
		}
		return BookLoanss;
	}

	public List<BookLoans> readRepeatedBookLoans(Integer bookId, Integer branchId, Integer cardNo) {
		return mysqlTemplate.query("select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookId, branchId, cardNo }, this);
	}
}
