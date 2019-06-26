package com.gcit.lms.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.service.AdminService;

public class AdminMenu {
	
	public static AdminService adminService = new AdminService();
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		for(Author a: adminService.readAllAuthors()){
			System.out.println("Author ID: "+a.getAuthorId());
			System.out.println("Author Name: "+a.getAuthorName());
			System.out.println("Books Written By Author: "+a.getAuthorName());
			for(Book b: a.getBooks()){
				System.out.println(b.getTitle());
				System.out.println(" ");
			}
			System.out.println("--------------");
		}
//		Book book = new Book();
//		book.setTitle("New Book from Service Jun 19");
//		Author a = new Author();
//		a.setAuthorId(44);
//		Author a1 = new Author();
//		a1.setAuthorId(60);
//		Author a2 = new Author();
//		a2.setAuthorId(62);
//		List<Author> authors = new ArrayList<>();
//		authors.add(a);
//		authors.add(a1);
//		authors.add(a2);
//		book.setAuthors(authors);
//		adminService.addBook(book);
//		
//		System.out.println("Book Added");
	}

}
