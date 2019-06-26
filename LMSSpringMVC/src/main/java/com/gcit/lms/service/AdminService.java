package com.gcit.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;

@RestController
public class AdminService {

	@Autowired
	BookDAO bdao;

	@Autowired
	AuthorDAO adao;

	@Transactional
	// @Qualifier
	public String addBook(Book book) {
		try {
			Integer bookId = bdao.addBookGetPK(book);
			// add authors
			for (Author a : book.getAuthors()) {
				bdao.addBookAuthors(bookId, a.getAuthorId());
			}
			// add genres

			// add branches
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return null;
	}
	
	@RequestMapping(value= "/readAllAuthors/{authorName}", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthors(@PathVariable String authorName) {
		List<Author> authors = new ArrayList<>();
		try {
			authors = adao.readAllAuthorsByName(authorName);
			for(Author a: authors){
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value= "/readAuthors", method=RequestMethod.GET, produces="application/json")
	public List<Author> readAuthorsByName(@RequestParam(value="authorName") String authorName) {
		List<Author> authors = new ArrayList<>();
		try {
			if(authorName!=null || authorName.length() > 0){
				authors = adao.readAllAuthorsByName(authorName);
			}else{
				authors = adao.readAllAuthors();
			}
			for(Author a: authors){
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value= "/saveAuthor", method=RequestMethod.POST, consumes="application/json")
	public String saveAuthor(@RequestBody Author author) {
		try{
			if(author.getAuthorId()!=null && author.getAuthorName() !=null){
				adao.updateAuthor(author);
			}else if(author.getAuthorId()!=null){
				adao.deleteAuthor(author);
			}else{
				adao.addAuthor(author);
				//add into tbl_book_authors
			}
			return "Author added sucessfully";
		}catch (Exception e){
			e.printStackTrace();
			return "Author add failed";
		}
	}

}
