
package com.gcit.lms.entity;

import java.util.List;

public class LibraryBranch {
	private Integer libraryBranchId;
	private String libraryBranchName;
	private String libraryBranchAddress;
	private List<BookLoans> bookLoans;
	private List<BookCopies> bookCopies;
	
	
	/**
	 * @return the libraryBranchId
	 */
	public Integer getLibraryBranchId() {
		return libraryBranchId;
	}
	/**
	 * @param libraryBranchId the libraryBranchId to set
	 */
	public void setLibraryBranchId(Integer libraryBranchId) {
		this.libraryBranchId = libraryBranchId;
	}
	/**
	 * @return the libraryBranchName
	 */
	public String getLibraryBranchName() {
		return libraryBranchName;
	}
	/**
	 * @param libraryBranchName the libraryBranchName to set
	 */
	public void setLibraryBranchName(String libraryBranchName) {
		this.libraryBranchName = libraryBranchName;
	}
	/**
	 * @return the libraryBranchAddress
	 */
	public String getLibraryBranchAddress() {
		return libraryBranchAddress;
	}
	/**
	 * @param libraryBranchAddress the libraryBranchAddress to set
	 */
	public void setLibraryBranchAddress(String libraryBranchAddress) {
		this.libraryBranchAddress = libraryBranchAddress;
	}
	
	/**
	 * @return the bookCopies
	 */
	public List<BookCopies> getBookCopies() {
		return bookCopies;
	}
	/**
	 * @param bookCopies the bookCopies to set
	 */
	public void setBookCopies(List<BookCopies> bookCopies) {
		this.bookCopies = bookCopies;
	}
	/**
	 * @return the bookLoans
	 */
	public List<BookLoans> getBookLoans() {
		return bookLoans;
	}
	/**
	 * @param bookLoans the bookLoans to set
	 */
	public void setBookLoans(List<BookLoans> bookLoans) {
		this.bookLoans = bookLoans;
	}
	
}
