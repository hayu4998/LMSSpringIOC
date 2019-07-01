/**
 * 
 */
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

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.LibraryBranch;

/**
 * @branch Jason
 *
 */
public class LibraryBranchDAO extends BaseDAO<LibraryBranch> implements ResultSetExtractor<List<LibraryBranch>>{

	public Integer add(LibraryBranch branch) throws ClassNotFoundException, SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		mysqlTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement("insert into tbl_library_branch (branchName,branchAddress) values (?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, branch.getLibraryBranchName());
			ps.setString(2, branch.getLibraryBranchAddress());
			return ps;
		}, keyHolder);
		Number key = keyHolder.getKey();
		return key.intValue();
		
	}

	public void update(LibraryBranch branch) throws ClassNotFoundException, SQLException {
		mysqlTemplate.update(
				"update tbl_library_branch set branchName = ?, LibraryAddress = ? where branchId = ?", 
				new Object[] {
						branch.getLibraryBranchName(),
						branch.getLibraryBranchAddress(),
						branch.getLibraryBranchId()
				}
		);
	}

	public void delete(LibraryBranch libraryBranch) throws ClassNotFoundException, SQLException {
		
		//check if exist book loan records
//		BookLoansDAO bdao = new BookLoansDAO(conn);
//		if(bdao.readBookLoansByID("branchId",libraryBranch.getLibraryBranchId()).size() != 0) {
//			System.out.println("The Branch has borrowing record, do you still want to delete it?[Y/n]");
//			
//			String input = in.nextLine();
//			if(input.toUpperCase().contentEquals("N")) {
//				return;
//			}
//		}
		mysqlTemplate.update(
				"delete from tbl_library_branch where branchId = ?", 
				new Object[]{libraryBranch.getLibraryBranchId()}
		);
	}
	
	public List<LibraryBranch> readAll() throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_library_branch", this);
	}
	
	public LibraryBranch readLibraryBranchById(Integer branchId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query("select * from tbl_library_branch where branchId = ?", new Object[] {branchId}, this).get(0);
	}
	
	public List<LibraryBranch> readLibraryBranchExcludsive(Book book) throws ClassNotFoundException, SQLException{
		return mysqlTemplate.query("select * from tbl_library_branch where branchId not in (select distinct branchId from tbl_book_copies where bookId = ?)", new Object[] {book.getBookId()}, this);
		
	}
	
	public List<LibraryBranch> extractData(ResultSet rs) throws SQLException {
		List<LibraryBranch> branchs = new ArrayList<LibraryBranch>();
		
		while(rs.next()){
			LibraryBranch branch = new LibraryBranch();
			
			branch.setLibraryBranchId(rs.getInt("branchId"));
			branch.setLibraryBranchName(rs.getString("branchName"));
			branch.setLibraryBranchAddress(rs.getString("branchAddress"));
			
			//add to the list
			branchs.add(branch);
		}
		return branchs;
	}
	
	
}
