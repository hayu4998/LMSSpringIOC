package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

public class AdminService {

	ConnectionUtil connUtil = new ConnectionUtil();

	public List<Author> readAllAuthors() throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			return adao.readAllAuthors();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public String addBook(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			Integer bookId = bdao.addBookGetPK(book);
			// add authors
			for (Author a : book.getAuthors()) {
				bdao.addBookAuthors(bookId, a.getAuthorId());
			}
			// add genres

			// add branches

			conn.commit();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

}
