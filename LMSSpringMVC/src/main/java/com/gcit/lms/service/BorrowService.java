package com.gcit.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;

@RestController
public class BorrowService {

	@Autowired
	AdminService adminService;

	@Autowired
	BookLoansDAO bldao;

	@Autowired
	BookCopiesDAO bcdao;

	@Autowired
	BookDAO bdao;

	@Autowired
	PublisherDAO pdao;

	@Autowired
	GenreDAO gdao;

	@Autowired
	AuthorDAO adao;

	@Autowired
	BorrowerDAO bordao;

	@Autowired
	LibraryBranchDAO lbdao;

	@RequestMapping(value = "/checkoutBook", method = RequestMethod.GET)
	public String checkoutBook(@RequestParam(value = "bookId") Integer bookId,
			@RequestParam(value = "branchId") Integer branchId, @RequestParam(value = "cardNo") Integer cardNo) {
		try {
			if(bcdao.readBookCopiesByBookIdBranchId(bookId, branchId).get(0).getNoOfCopies()>0) {
				//checkout book
				bcdao.borrowReturnFunction("-", bookId, branchId);
				//add record
				bldao.borrowBook(new Object[] { bookId, branchId, cardNo });
				return "checkout successful";
			}else {
				return "no copy left for that book, checkout failed";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Checkout fail";
		}
	}

	// for more indepth information of each book
	@RequestMapping(value = "/readBookInLibraryBranch", method = RequestMethod.GET, produces = "application/json")
	public List<Book> readBookInLibraryBranch(@RequestParam(value = "branchId") Integer branchId) {
		try {
			List<Book> books = bdao.readBookByBranchId(branchId);
			for (Book i : books) {

				i.setAuthors(adao.readAuthorByBookId(i.getBookId()));
				i.setGenres(gdao.readGenreByBookId(i.getBookId()));
				i.setBookLoans(bldao.readBookLoansByID("bookId", i.getBookId()));
				i.setBookCopies(bcdao.readBookCopiesByBookId(i.getBookId()));
				i.setPublisher(pdao.readPublisherByBookId(i.getBookId()));
			}
			return books;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// readLibraryBranch is presented in AdminService



	// read book loans
	@RequestMapping(value = "/borrowerReadBookLoans", method = RequestMethod.GET, produces = "application/json")
	public List<BookLoans> readBookLoans(@RequestParam(value = "CardNo", required = false) Integer cardNo) {
		try {
			// read book loans of same borrower

			List<BookLoans> bookLoansList = bldao.readBookLoansByID("cardNo", cardNo);
			for (BookLoans i : bookLoansList) {
				i.setBook(bdao.readBookByBookId(i.getBookId()));
				i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
				i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
			}
			return bookLoansList;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("read book loans fail");
		}
		return null;
	}
	
	//return book
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST, consumes = "application/json")
	public String returnBook(@RequestBody BookLoans bookLoans) {
		try {
			bldao.update(bookLoans, "dateIn");
			return "return book operation successful";
		}catch(Exception e) {
			e.printStackTrace();
			return "return book operation fail";
		}
	}

}
