/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.BookCopies;

/**
 * @BookCopies Jason
 *
 */
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>> {

	public void add(Integer bookId, Integer branchId, Integer noOfCopies) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?,?,?)",
				new Object[] { bookId, branchId, noOfCopies});
	}

	public void update(Integer bookId, Integer branchId, Integer noOfCopies) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[] { noOfCopies, bookId, branchId});
	}

	public void borrowReturnFunction(String sign, Integer bookId, Integer branchId)
			throws ClassNotFoundException, SQLException {
		mysqlTemplate.update(
				"update tbl_book_copies set noOfCopies = noOfCopies " + sign + " 1 where bookId=? and branchId = ?",
				new Object[] { bookId, branchId });
	}

	public void delete(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_book_copies where bookId = ? branchId = ?",
				new Object[] { bookCopies.getBook().getBookId(), bookCopies.getBranch().getLibraryBranchId() });
	}

	public List<BookCopies> readBookCopiesByBranchId(Integer branchId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_copies where branchId = ? and noOfCopies >0 ",
				new Object[] { branchId }, this);
	}

	public List<BookCopies> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_copies", this);
	}

//	public BookCopies readBookCopiesFromID(String pk) throws ClassNotFoundException, SQLException {
//		return mysqlTemplate.query("select * from tbl_book_copies where concat(bookId,branchId) = ?", new Object[] {pk}).get(0);
//	}

	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> BookCopiess = new ArrayList<BookCopies>();

		while (rs.next()) {
			BookCopies BookCopies = new BookCopies();
			// set # of Copies
			BookCopies.setNoOfCopies(rs.getInt("noOfCopies"));
			BookCopies.setBookId(rs.getInt("bookId"));
			BookCopies.setBranchId(rs.getInt("branchId"));
			BookCopiess.add(BookCopies);
		}
		return BookCopiess;
	}

//	public BookCopies librarianReadAllBookCopiesFromBranch(Integer bookId) throws ClassNotFoundException, SQLException {
//		List<BookCopies> bc = mysqlTemplate.query("select * from tbl_book_copies where branchId = ?", new Object[] {bookId}, this);
//		if(bc.size() == 0) {
//			return null;
//		}else {
//			return bc.get(0);
//		}
//	}

	public List<BookCopies> readBookCopiesByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_copies where bookId = ?", new Object[] { bookId }, this);
	}

	public List<BookCopies> readBookCopiesByBookIdBranchId(Integer bookId, Integer branchId)
			throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[] { bookId, branchId }, this);
	}

}
