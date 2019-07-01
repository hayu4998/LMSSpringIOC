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

import com.gcit.lms.entity.Author;

public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {

	public Integer add(Author author) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("insert into tbl_author (authorName) values (?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, author.getAuthorName());
			return ps;
		}, keyHolder);
		Number key = keyHolder.getKey();
		return key.intValue();
	}

	public void update(Author author) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	public void delete(Author author) throws ClassNotFoundException, SQLException {

		// check if exist book
//		if(author.getBooks().size() != 0) {
//			System.out.println("The Author has coresponed book exist, do you still want to delete it?[Y/n]");
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}

		mysqlTemplate.update("delete from tbl_author where authorId = ?", new Object[] { author.getAuthorId() });
	}

	public List<Author> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_author", this);
	}

	public List<Author> readAuthorByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query(
				"select * from tbl_author where authorId in (select authorId from tbl_book_authors where bookId = ? )",
				new Object[] { bookId }, this);
	}

	public List<Author> readAuthorsByNameLike(String name) {
		name = '%' + name + '%';
		return mysqlTemplate.query("select * from tbl_author where authorName like ?", new Object[] { name }, this);
	}

	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<Author>();
		while (rs.next()) {
			Author author = new Author();
			author.setAuthorId(rs.getInt("authorId"));
			author.setAuthorName(rs.getString("authorName"));

			// add to the list
			authors.add(author);
		}
		return authors;
	}

	public List<Author> readAuthorById(Integer authorId) {
		return mysqlTemplate.query("select * from tbl_author where authorId = ?", new Object[] {authorId},this);
	}
}
