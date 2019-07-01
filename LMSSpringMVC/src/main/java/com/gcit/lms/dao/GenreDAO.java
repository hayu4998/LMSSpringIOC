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

import com.gcit.lms.entity.Genre;

public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>>{

	public Integer add(Genre genre) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("insert into tbl_genre (genre_name) values (?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, genre.getGenreName());
			return ps;
		}, keyHolder);
		Number key = keyHolder.getKey();
		return key.intValue();
	}

	public void update(Genre genre) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update(
				"update tbl_genre set genre_name = ? where genre_id = ?", 
				new Object[]{genre.getGenreName(), genre.getGenreId()}
		);
	}

	public void delete(Genre genre) throws ClassNotFoundException, SQLException {
		
		
//		if(genre.getBooks().size() != 0) {
//			System.out.println("The genre has coresponed book exist, do you still want to delete it?[Y/n]");
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}
		
		mysqlTemplate.update(
				"delete from tbl_genre where genre_id = ?", 
				new Object[]{genre.getGenreId()}
		);
	}
	
	public List<Genre> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_genre", this);
	}
	
	public List<Genre> readGenreByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_genre where genre_id in (select genre_id from tbl_book_genres where bookId = ?)", new Object[] {bookId},this);
	}
	
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		List<Genre> genres = new ArrayList<Genre>();
		while(rs.next()){
			Genre genre = new Genre();
			
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			//add to the list
			genres.add(genre);
		}
		return genres;
	}

	public List<Genre> readGenreByNameLike(String genreName) {
		genreName = '%' + genreName + '%';
		return mysqlTemplate.query("select * from tbl_genre where genre_name like ?", new Object[] {genreName}, this);
	}

	public List<Genre> readGenreById(String genreId) {
		return mysqlTemplate.query("select * from tble_genre where genre_id = ?", new Object[] {genreId},this);
	}

}
