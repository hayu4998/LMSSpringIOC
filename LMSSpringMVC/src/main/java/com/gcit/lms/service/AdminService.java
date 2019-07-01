package com.gcit.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

@RestController

public class AdminService {

	@Autowired
	BookDAO bdao;

	@Autowired
	AuthorDAO adao;

	@Autowired
	BookCopiesDAO bcdao;

	@Autowired
	GenreDAO gdao;

	@Autowired
	BookLoansDAO bldao;

	@Autowired
	BorrowerDAO bordao;

	@Autowired
	LibraryBranchDAO lbdao;

	@Autowired
	PublisherDAO pdao;

	@Transactional
//	public String addBook(Book book) {
//		try {
//			Integer bookId = bdao.add(book);
//			// add authors
//			for (Author a : book.getAuthors()) {
//				bdao.addBookAuthors(bookId, a.getAuthorId());
//			}
//			// add genres
//			for (Genre g : book.getGenres()) {
//				bdao.addBookGenres(bookId, g.getGenreId());
//			}
//			// add book copy
//			for (BookCopies bc : book.getBookCopies()) {
//				bcdao.add(bc);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Something went wrong. Failed to get all authors");
//		}
//		return null;
//	}
	
	@RequestMapping(value = "/readAuthor", method = RequestMethod.GET, produces = "application/json")
	public List<Author> readAuthorsByName(
			@RequestParam(value = "authorName", required = false) String authorName,
			@RequestParam(value = "authorId", required = false) Integer authorId
			) 
	{
		List<Author> authors = new ArrayList<Author>();
		// System.out.println("Author: " + authorName);
		try {
			if(authorId != null) { // read author by authorId
				authors = adao.readAuthorById(authorId);
				
			}else if (authorName != null) {// read author w/ name like authorName
				authors = adao.readAuthorsByNameLike(authorName);
				
			} else { // read all authors
				authors = adao.readAll();
			}
			// populate booklist in author
			for (Author a : authors) {
				a.setBooks(bdao.readAllBooksByAuthorId(a.getAuthorId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			System.out.println("Something went wrong. Failed to get all authors");
		}
		return authors;
	}
	
	@RequestMapping(value = "/editAuthor", method = RequestMethod.POST, consumes = "application/json")
	public String saveAuthor(@RequestBody Author author) {
		try {
			if (author.getAuthorId() != null && author.getAuthorName() != null) {
				adao.update(author);
			} else if (author.getAuthorId() != null) {
				adao.delete(author);
			} else {
				adao.add(author);
				// add into tbl_book_authors if booklist is not null and book is null
//				if (author.getBooks() != null) {
//					for (Book book : author.getBooks()) {
//						bdao.add(book);
//					}
//				}
			}
			return "Author added successfully";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return "Author add failed";
		}
	}

	@RequestMapping(value = "/readGenre", method = RequestMethod.GET, produces = "application/json")
	public List<Genre> readGenre(
			@RequestParam(value = "genreName", required = false) String genreName,
			@RequestParam(value = "genreId", required = false) String genreId
			) 
	{
		List<Genre> genreList = new ArrayList<Genre>();
		try {
			if(genreId != null) { //read by genreId
				genreList = gdao.readGenreById(genreId);
			}else if(genreName != null) {// read by name like genreName
				genreList = gdao.readGenreByNameLike(genreName);
			}else { // read all
				genreList = gdao.readAll();
			}
			
			for (Genre g : genreList) {
				g.setBooks(bdao.readAllBooksByGenreId(g.getGenreId()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			System.out.println("Fail to read Genre");
		}
		return genreList;
	}
	
	@RequestMapping(value = "/editGenre", method = RequestMethod.POST, consumes = "application/json")
	public String editGenre(@RequestBody Genre genre) {
		try {
			if (genre.getGenreId() == null) { // adding
				gdao.add(genre);
				// add book if booklist is not null
//				if (genre.getBooks() != null) {
//					for (Book book : genre.getBooks()) {
//						bdao.add(book);
//					}
//				}
			} else if (genre.getGenreName() == null) { // delete
				gdao.delete(genre);
			} else { // update
				gdao.update(genre);
			}
			return "Operation successful";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return "Edition action failed";
		}
	}

	@RequestMapping(value = "/readBook", method = RequestMethod.GET, produces = "application/json")
	public List<Book> readBook(@RequestParam(value = "bookId", required = false) Integer bookId,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "pubId", required = false) Integer pubId) {
		try {
			List<Book> books = new ArrayList<Book>();
			if (bookId != null) { // read book by bookId

				Book book = bdao.readBookByBookId(bookId);
				book.setAuthors(adao.readAuthorByBookId(bookId));
				book.setGenres(gdao.readGenreByBookId(bookId));
				book.setBookLoans(bldao.readBookLoansByID("bookId", bookId));
				book.setBookCopies(bcdao.readBookCopiesByBookId(bookId));
				book.setPublisher(pdao.readPublisherByBookId(bookId));
				books.add(book);

			} else if (title != null) { // read book by title

				books = bdao.readBookLikeTitle(title);
				for (Book i : books) {
					bookId = i.getBookId();
					i.setAuthors(adao.readAuthorByBookId(bookId));
					i.setGenres(gdao.readGenreByBookId(bookId));
					i.setBookLoans(bldao.readBookLoansByID("bookId", bookId));
					i.setBookCopies(bcdao.readBookCopiesByBookId(bookId));
					i.setPublisher(pdao.readPublisherByBookId(bookId));
				}

			} else if (pubId != null) { // read book by publisher Id
				books = bdao.readBookbyPubId(pubId);
				for (Book i : books) {
					bookId = i.getBookId();
					i.setAuthors(adao.readAuthorByBookId(bookId));
					i.setGenres(gdao.readGenreByBookId(bookId));
					i.setBookLoans(bldao.readBookLoansByID("bookId", bookId));
					i.setBookCopies(bcdao.readBookCopiesByBookId(bookId));
					i.setPublisher(pdao.readPublisherByBookId(bookId));
				}
			} else {// read all books
				books = bdao.readAll();
				for (Book i : books) {
					bookId = i.getBookId();
					i.setAuthors(adao.readAuthorByBookId(bookId));
					i.setGenres(gdao.readGenreByBookId(bookId));
					i.setBookLoans(bldao.readBookLoansByID("bookId", bookId));
					i.setBookCopies(bcdao.readBookCopiesByBookId(bookId));

					i.setPublisher(pdao.readPublisherByBookId(bookId));
				}
			}
			System.out.println("read book operation success");
			return books;

		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			System.out.println("Read Book Operation fail");

		}
		return null;
	}
	
	@RequestMapping(value = "/editBook", method = RequestMethod.POST, consumes = "application/json")
	public String editBook(@RequestBody Book book) {
		try {
			if (book.getBookId() == null) { // add book

				// fix Publisher
				if(book.getPublisher() != null) {
					if (book.getPublisher().getPublisherId() == null) {
						// add new if not exist
						Publisher publisher = book.getPublisher();
						publisher.setPublisherId(pdao.add(book.getPublisher()));
						book.setPublisher(publisher);
					}
				}else {
					Publisher publisher = new Publisher();
					publisher.setPublisherId(null);
					book.setPublisher(publisher);
				}

				// set up id,title,pubId
				Integer bookId = bdao.add(book);

				// add author
				if (book.getAuthors() != null) {

					for (Author i : book.getAuthors()) {
						if (i.getAuthorId() != null) { // add exist ones
							bdao.addBookAuthors(bookId, i.getAuthorId());
						} else { // add new author
							bdao.addBookAuthors(bookId, adao.add(i));

						}
					}
				}
				// add Genre
				if (book.getGenres() != null) {

					for (Genre i : book.getGenres()) {
						if (i.getGenreId() != null) { // add exist one
							bdao.addBookGenres(bookId, i.getGenreId());
						} else { // add new genre
							bdao.addBookGenres(bookId, gdao.add(i));

						}
					}
				}

			} else if (book.getTitle() == null && book.getPublisher() == null) { // delete book

				bdao.delete(book);

			} else { // update book
				
				// fix Publisher
				if(book.getPublisher() != null) {
					if (book.getPublisher().getPublisherId() == null) {
						// add new if not exist
						Publisher publisher = book.getPublisher();
						publisher.setPublisherId(pdao.add(book.getPublisher()));
						book.setPublisher(publisher);
					}
				}else {
					Publisher publisher = new Publisher();
					publisher.setPublisherId(null);
					book.setPublisher(publisher);
				}
				// update book title, publisher
				bdao.update(book);
				// update author
				if (book.getAuthors() != null) {
					
					for (Author i : book.getAuthors()) {
						if (i.getAuthorId() != null && i.getAuthorName() == null) {//delete the author
							bdao.deleteBookAuthor(book.getBookId(), i.getAuthorId());
						}else if (i.getAuthorId() != null) { // add exist one
							bdao.addBookAuthors(book.getBookId(), i.getAuthorId());
						} else { // new author
							bdao.addBookAuthors(book.getBookId(), adao.add(i));
						}
					}
				}
				// update Genre
				if (book.getGenres() != null) {
					
					for (Genre i : book.getGenres()) {
						if(i.getGenreId()!=null && i.getGenreName() == null) { //delete the genre
							bdao.deleteBookGenre(book.getBookId(), i.getGenreId());
						}
						else if (i.getGenreId() != null) { // add exist one
							bdao.addBookGenres(book.getBookId(), i.getGenreId());
						} else { // add new genre
							bdao.addBookGenres(book.getBookId(), gdao.add(i));
						}
					}
				}
			}
			return "Edition successful";
		} catch (Exception e) {
			//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			
			return "Edition fail";
		}
	}
	
	
	// read book loans
	@RequestMapping(value = "/adminReadBookLoans", method = RequestMethod.GET, produces = "application/json")
	public List<BookLoans> readBookLoans(
			@RequestParam(value = "cardNo", required = false) Integer cardNo,
			@RequestParam(value = "bookId", required = false) Integer bookId,
			@RequestParam(value = "branchId", required = false) Integer branchId,
			@RequestParam(value = "mode") Integer mode) {
		try {
			if (mode == 1) { // read book loans of same borrower

				List<BookLoans> bookLoansList = bldao.readBookLoansByID("cardNo", cardNo);
				for (BookLoans i : bookLoansList) {
					i.setBook(bdao.readBookByBookId(i.getBookId()));
					i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
					i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
				}
				return bookLoansList;

			} else if (mode == 2) { // read book loans of same library branch, maybe useful in the future

				List<BookLoans> bookLoansList = bldao.readBookLoansByID("branchId", branchId);
				for (BookLoans i : bookLoansList) {
					i.setBook(bdao.readBookByBookId(i.getBookId()));
					i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
					i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
				}
				return bookLoansList;
			} else if (mode == 3) { // read book loans of same book, may be useful in the future

				List<BookLoans> bookLoansList = bldao.readBookLoansByID("bookId", bookId);
				for (BookLoans i : bookLoansList) {
					i.setBook(bdao.readBookByBookId(i.getBookId()));
					i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
					i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
				}
				return bookLoansList;
			} else if(mode == 4) { // mode = 4, read book loan of a book that was borrowed by same person at same
						// branch
				List<BookLoans> bookLoansList = bldao.readRepeatedBookLoans(bookId, branchId, cardNo);
				for (BookLoans i : bookLoansList) {
					i.setBook(bdao.readBookByBookId(i.getBookId()));
					i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
					i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
				}
				return bookLoansList;
				
			} else { // read all bookLoans
				List<BookLoans> bookLoansList = bldao.readAll();
				for (BookLoans i : bookLoansList) {
					i.setBook(bdao.readBookByBookId(i.getBookId()));
					i.setBorrower(bordao.selectBorrowerByID(i.getCardNo()));
					i.setBranch(lbdao.readLibraryBranchById(i.getBranchId()));
				}
				return bookLoansList;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			System.out.println("read book loans fail");
		}
		return null;
	}

	@RequestMapping(value = "/adminExtendBookLoans", method = RequestMethod.POST, consumes = "application/json")
	public String extendBookLoansDueDate(@RequestBody BookLoans bookLoans) {
		try {
			bldao.update(bookLoans, "dueDate");
			return "Extend successful";
		} catch (Exception e) {
			e.printStackTrace();
			return "Extend fail";
		}
	}
	
	@RequestMapping(value = "/readBorrower", method = RequestMethod.GET, produces = "application/json")
	public List<Borrower> readBorrowers(
			@RequestParam(value = "cardNo", required = false) Integer cardNo,
			@RequestParam(value = "borrowerName", required = false) String name
			) 
	{
		try {
			if (cardNo != null) { // read exact one borrower
				List<Borrower> bl = new ArrayList<Borrower>();
				Borrower borrower = bordao.selectBorrowerByID(cardNo);
				// set book loan
				borrower.setBookLoans(bldao.readBookLoansByID("cardNo", borrower.getCardId()));
				bl.add(borrower);
				return bl;
			} else if (name != null) { // read borrower by name like
				List<Borrower> borrowers = bordao.readBorrowerByNameLike(name);
				// populate book loan
				for (Borrower i : borrowers) {
					i.setBookLoans(bldao.readBookLoansByID("cardNo", i.getCardId()));
				}
				return borrowers;
			} else { // read all name
				List<Borrower> borrowers = bordao.readAll();
				// populate book loan
				for (Borrower i : borrowers) {
					i.setBookLoans(bldao.readBookLoansByID("cardNo", i.getCardId()));
				}
				return borrowers;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value = "/editBorrower", method = RequestMethod.POST, consumes = "application/json")
	public String editBorrower(@RequestBody Borrower borrower) {
		try {
			if (borrower.getCardId() == null) { // add new borrower
				bordao.add(borrower);
			} else if (borrower.getBorrowerName() != null) { // update borrower
				bordao.update(borrower);
			} else { // only cardNo, delete
				bordao.delete(borrower);
			}
			return "edit borrower successful";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return "edit borrower failed";
		}
	}
	
	@RequestMapping(value = "/readLibraryBranch", method = RequestMethod.GET, produces = "application/json")
	public List<LibraryBranch> readLibraryBranch(@RequestParam(value = "branchId", required = false) Integer branchId) {

		try {
			if (branchId == null) { // read all library branch
				List<LibraryBranch> libraryBranchList = lbdao.readAll();
				// populate BookCopies and BookLoans attribute
				for (LibraryBranch i : libraryBranchList) {
					i.setBookCopies(bcdao.readBookCopiesByBranchId(i.getLibraryBranchId()));
					i.setBookLoans(bldao.readBookLoansByID("branchId", i.getLibraryBranchId()));
				}
				return libraryBranchList;
			} else { // read specific library branch
				List<LibraryBranch> lbList = new ArrayList<LibraryBranch>();
				LibraryBranch libraryBranch = lbdao.readLibraryBranchById(branchId);
				libraryBranch.setBookCopies(bcdao.readBookCopiesByBranchId(branchId));
				libraryBranch.setBookLoans(bldao.readBookLoansByID("branchId", branchId));
				lbList.add(libraryBranch);
				return lbList;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
		return null;
	}
	//testing
	@RequestMapping(value = "/editLibraryBranch", method = RequestMethod.POST, consumes = "application/json")
	public String editLibraryBranch(@RequestBody LibraryBranch libraryBranch) {
		try {
			if (libraryBranch.getLibraryBranchId() == null) { // add new library branch
				lbdao.add(libraryBranch);
			} else if (libraryBranch.getLibraryBranchName() != null) {// update library branch
				lbdao.update(libraryBranch);
			} else { // delete library branch
				lbdao.delete(libraryBranch);
			}
			return "edition to library branch successful";
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return "edition to library branch fail";
		}
	}

	@RequestMapping(value = "/readPublisher", method = RequestMethod.GET, produces = "application/json")
	public List<Publisher> readPublisher(@RequestParam(value = "publisherId",required = false) Integer publisherId) {
		try {
			if (publisherId == null) { // read all publisher
				List<Publisher> publishers = pdao.readAll();
				// populate book
				for (Publisher i : publishers) {
					i.setBooks(bdao.readBookbyPubId(i.getPublisherId()));
				}
				return publishers;
			} else { // read exact publisher
				Publisher publisher = pdao.readPublisherById(publisherId);
				publisher.setBooks(bdao.readBookbyPubId(publisherId));
				List<Publisher> pubList = new ArrayList<Publisher>();
				pubList.add(publisher);
				return pubList;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			return null;
		}
	}
}