package com.gcit.lms.entity;

public class BookLoans{
	private Book book;
	private LibraryBranch branch;
	private Borrower borrower;
	private Integer bookId;
	private Integer branchId;
	private Integer cardNo;
	private String loanedDateOut;
	private String loanedDateIn;
	private String loanedDueDate;
	
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public Integer getCardNo() {
		return cardNo;
	}
	public void setCardNo(Integer cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}
	/**
	 * @return the branch
	 */
	public LibraryBranch getBranch() {
		return branch;
	}
	/**
	 * @param branch the branch to set
	 */
	public void setBranch(LibraryBranch branch) {
		this.branch = branch;
	}
	/**
	 * @return the borrower
	 */
	public Borrower getBorrower() {
		return borrower;
	}
	/**
	 * @param borrower the borrower to set
	 */
	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
	/**
	 * @return the loanedDateOut
	 */
	public String getLoanedDateOut() {
		return loanedDateOut.substring(0,19);
	}
	/**
	 * @param loanedDateOut the loanedDateOut to set
	 */
	public void setLoanedDateOut(String loanedDateOut) {
		this.loanedDateOut = loanedDateOut;
	}
	/**
	 * @return the loanedDateIn
	 */
	public String getLoanedDateIn() {
		return loanedDateIn;
	}
	/**
	 * @param loanedDateIn the loanedDateIn to set
	 */
	public void setLoanedDateIn(String loanedDateIn) {
		this.loanedDateIn = loanedDateIn;
	}
	/**
	 * @return the loanedDueDate
	 */
	public String getLoanedDueDate() {
		return loanedDueDate;
	}
	/**
	 * @param loanedDueDate the loanedDueDate to set
	 */
	public void setLoanedDueDate(String loanedDueDate) {
		this.loanedDueDate = loanedDueDate;
	}
}
