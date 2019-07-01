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

import com.gcit.lms.entity.Book;

public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>> {

	public Integer add(Book book) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			if(book.getPublisher().getPublisherId() != null) {
				PreparedStatement ps = connection.prepareStatement("insert into tbl_book (title,pubId) values (?,?)",Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				ps.setInt(2, book.getPublisher().getPublisherId());
				return ps;
			}else {
				PreparedStatement ps = connection.prepareStatement("insert into tbl_book (title) values (?)",Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, book.getTitle());
				return ps;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();
		return key.intValue();
	}

//	public Integer addBookGetPK(Book book) throws ClassNotFoundException, SQLException {
//		return mysqlTemplate.update(
//				"insert into tbl_book (title) values (?)", 
//				new Object[] { book.getTitle() }
//		);
//	}

	public void addBookAuthors(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {

		mysqlTemplate.update("insert into tbl_book_authors(bookId,authorId) values (?, ?)",
				new Object[] { bookId, authorId });

	}

	public void deleteBookAuthor(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_book_authors where bookId = ? and authorId = ?",
				new Object[] { bookId, authorId });
	}

	public void addBookGenres(Integer bookId, Integer genresId) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_book_genres(bookId, genre_id) values (?, ?)",
				new Object[] { bookId, genresId });
	}

	public void deleteBookGenre(Integer bookId, Integer genresId) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("delete from tbl_book_genres where bookId = ? and genre_id = ?",
				new Object[] { bookId, genresId });
	}

	public void addBookLibraryBranchs(Integer newBookPK, Integer libraryBranchId, String noOfCopies)
			throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("insert into tbl_book_copies(bookId, branchId, noOfCopies) values (?, ?, ?)",
				new Object[] { newBookPK, libraryBranchId, noOfCopies });
	}

	public void update(Book book) throws ClassNotFoundException, SQLException {

		mysqlTemplate.update("update tbl_book set title = ?, pubId = ? where bookId = ?",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId(), book.getBookId() });
	}

	public void delete(Book book) throws ClassNotFoundException, SQLException {

		// check if exist book loan records
//		BookLoansDAO bldao = new BookLoansDAO(conn);
//		if(bldao.readBookLoansByID("bookId",book.getBookId()).size() != 0) {
//			System.out.println("The Book has borrowing record, do you still want to delete it?[Y/n]");
//			
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}

		mysqlTemplate.update("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book", this);
	}

	public Book readBookByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_book where bookId = ?", new Object[] { bookId }, this).get(0);
	}

	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<Book>();

		while (rs.next()) {
			Book book = new Book();

			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));

			// add to the list
			books.add(book);
		}
		return books;
	}

	public List<Book> readAllBooksByAuthorId(Integer authorId) {
		return mysqlTemplate.query(
				"select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)",
				new Object[] { authorId }, this);
	}

	public List<Book> readAllBooksByGenreId(Integer genreId) {
		return mysqlTemplate.query(
				"select * from tbl_book where bookId in (select bookId from tbl_book_genres where genre_id = ?)",
				new Object[] { genreId }, this);
	}

	public List<Book> readBookLikeTitle(String title) {
		title = '%'+title+'%';
		return mysqlTemplate.query("select * from tbl_book where title like ?", new Object[] { title }, this);
	}

	public List<Book> readBookbyPubId(Integer pubId) {
		return mysqlTemplate.query("select * from tbl_book where pubId = ?", new Object[] { pubId }, this);
	}

	public void deleteAllBookAuthor(Integer bookId) {
		mysqlTemplate.update("delete from tbl_book_authors where bookId = ? and authorId = authorId", new Object[] { bookId }, this);

	}

	public void deleteAllBookGenre(Integer bookId) {
		mysqlTemplate.update("delete from tbl_book_genres where bookId = ? and genre_id = genre_id", new Object[] { bookId }, this);

	}

	public List<Book> readBookByBranchId(Integer branchId) {
		return mysqlTemplate.query("select * from tbl_book where bookId in(select bookId from tbl_book_copies where branchId = ? and noOfCopies > 0)", new Object[] {branchId}, this);
	}

	public void updateBookPublisher(Integer pubId, Integer bookId) {
		mysqlTemplate.update("update tbl_book set pubId = ? where bookId = ?", new Object[] {pubId, bookId},this);
		
	}

}
