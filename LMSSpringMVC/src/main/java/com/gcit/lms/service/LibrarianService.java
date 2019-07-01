package com.gcit.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.entity.BookCopies;

@RestController
public class LibrarianService {

	@Autowired
	AdminService admin;

	@Autowired
	BookCopiesDAO bcdao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	LibraryBranchDAO lbdao;
	
	// Library read all library Branch can be handled by
	// admin.readLibraryBranch(null)
	// @RequestMapping(value = "/librarianReadLibraryBranchs", method =
	// RequestMethod.GET, produces = "application/json")

	// Library update can be handled by admin.editLibraryBranch(Librarian Branch)

	// show librarian list of book will use admin,readBooks(null,null,null) to show
	// all books

	// read book noOfCopies
	@RequestMapping(value = "librarianReadNoOfCopies", method = RequestMethod.GET, produces = "application/json")
	public BookCopies librarianReadBookCopies(
			@RequestParam(value = "bookId") Integer bookId,
			@RequestParam(value = "branchId") Integer branchId
			) 
	{
		try {
			BookCopies bookCopy = new BookCopies();
			if (bcdao.readBookCopiesByBookIdBranchId(bookId, branchId).size() != 0) { // already exist
				bookCopy = bcdao.readBookCopiesByBookIdBranchId(bookId, branchId).get(0);
				//populate book and branch
				bookCopy.setBook(bdao.readBookByBookId(bookId));
				bookCopy.setBranch(lbdao.readLibraryBranchById(branchId));
			}else {
				bookCopy.setNoOfCopies(0);
				bookCopy.setBook(bdao.readBookByBookId(bookId));
				bookCopy.setBranch(lbdao.readLibraryBranchById(branchId));
			}
			return bookCopy;
		}catch(Exception e) {
			e.getSuppressed();
			return null;
		}
	}
	
//  //for future usage 
//	@RequestMapping(value = "/editBookCopies", method = RequestMethod.POST, consumes = "application/json")
//	public String editBookCopies(@RequestBody BookCopies bookCopies,
//			@RequestParam(value = "mode", required = false) Integer mode) {
//		try {
//			if (mode == 1) { // add
//				bcdao.add(bookCopies);
//			} else if (mode == 2) { // delete
//				bcdao.delete(bookCopies);
//			} else { // update
//				bcdao.update(bookCopies);
//			}
//			return "operation successful";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "operation failed";
//		}
//
//	}

	// librarian update bookcopie
	@RequestMapping(value = "/librarianUpdateNoOfCopies", method = RequestMethod.GET)
	public String librarianUpdateNoOfCopies(@RequestParam(value = "bookId") Integer bookId,
			@RequestParam(value = "branchId") Integer branchId,
			@RequestParam(value = "noOfCopies") Integer noOfCopies) {
		try {
			if (bcdao.readBookCopiesByBookIdBranchId(bookId, branchId).size() != 0) { // already exist
				bcdao.update(bookId, branchId, noOfCopies);
				;
			} else {
				bcdao.add(bookId, branchId, noOfCopies);
			}
			return "update book copy operation success";
		} catch (Exception e) {
			e.printStackTrace();
			return "update book copy operation failed";
		}
	}

}
