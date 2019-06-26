/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Book;

/**
 * @book ppradhan
 *
 */
public class BookDAO extends BaseDAO<Book> {
	
	
	public BookDAO(Connection conn) {
		super(conn);
	}

	public void addBook(Book book) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book (title) values (?)", new Object[] { book.getTitle() });
	}
	
	public Integer addBookGetPK(Book book) throws ClassNotFoundException, SQLException {
		return saveWithID("insert into tbl_book (title) values (?)", new Object[] { book.getTitle() });
	}
	
	public void addBookAuthors(Integer bookId, Integer authorId)  throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_authors values (?, ?)", new Object[] { bookId, authorId});
	}

	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		save("update tbl_book set bookName = ? where bookId = ?",
				new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAllBooks() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book", null);
	}

	public List<Book> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		AuthorDAO adao = new AuthorDAO(conn);
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			//populate all child entities (genres, authors, branches etc)
			book.setAuthors(adao.read("select * from tbl_author where authorId IN ( select authorId from tbl_book_authors where bookId = ?)", new Object[] {book.getBookId()}));
			books.add(book);
		}
		return books;
	}
	
	public List<Book> extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
		return books;
	}
}
