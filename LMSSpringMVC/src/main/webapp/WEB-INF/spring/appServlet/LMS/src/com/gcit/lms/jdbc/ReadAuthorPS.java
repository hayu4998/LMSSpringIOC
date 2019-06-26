package com.gcit.lms.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReadAuthorPS {
	
	public static String driverName = "com.mysql.cj.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/library?useSSL=true";
	public static String username = "root";
	public static String password = "root";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Scanner scan = new Scanner(System.in);
		String sql = null;
		System.out.println("Enter authorname to search: ");
		String authorName = scan.nextLine();
		//1> Register Driver.
		Class.forName(driverName);
		
		//2> Create Connection
		Connection conn = DriverManager.getConnection(url, username, password);
		
		//3> Create statement object
		PreparedStatement pstmt = conn.prepareStatement("select * from tbl_author where authorName LIKE ?");
		pstmt.setString(1, "%"+authorName+"%");
//		if(authorName!=null){
//			sql =  "select * from tbl_author where authorName LIKE '%"+authorName+"%'";
//		}else{
//		sql = "Select * from tbl_author";
//		}
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			System.out.println("Author ID: "+rs.getInt("authorId"));
			System.out.println("Author Name: "+rs.getString("authorName"));
			System.out.println("--------------");
		}
	}

}
