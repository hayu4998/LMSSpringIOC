/**
 * 
 */
package com.gcit.lms.entity;

import java.util.List;

public class Borrower {
	private Integer cardId;
	private String borrowerName;
	private String borrowerAddress;
	private String borrowerPhone;
	private List<BookLoans> bookLoans;

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

	/**
	 * @return the borrowerCardId
	 */
	public Integer getCardId() {
		return cardId;
	}

	/**
	 * @param borrowerCardId the borrowerCardId to set
	 */
	public void setCardId(Integer borrowerCardId) {
		this.cardId = borrowerCardId;
	}

	/**
	 * @return the borrowerName
	 */
	public String getBorrowerName() {
		return borrowerName;
	}

	/**
	 * @param borrowerName the borrowerName to set
	 */
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	/**
	 * @return the borrowerAddress
	 */
	public String getBorrowerAddress() {
		return borrowerAddress;
	}

	/**
	 * @param borrowerAddress the borrowerAddress to set
	 */
	public void setBorrowerAddress(String borrowerAddress) {
		this.borrowerAddress = borrowerAddress;
	}

	/**
	 * @return the borrowerPhone
	 */
	public String getBorrowerPhone() {
		return borrowerPhone;
	}

	/**
	 * @param borrowerPhone the borrowerPhone to set
	 */
	public void setBorrowerPhone(String borrowerPhone) {
		this.borrowerPhone = borrowerPhone;
	}

}
