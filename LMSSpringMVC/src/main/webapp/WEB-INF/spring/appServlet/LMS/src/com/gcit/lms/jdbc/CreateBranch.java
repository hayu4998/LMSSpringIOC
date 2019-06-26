package com.gcit.lms.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CreateBranch {
	
	public static String driverName = "com.mysql.cj.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/library?useSSL=true";
	public static String username = "root";
	public static String password = "root";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Scanner scan = new Scanner(System.in);
		String sql = null;
		System.out.println("Enter pub Name: ");
		String authorName = scan.nextLine();
		System.out.println("Enter pub Address: ");
		String address= scan.nextLine();
		System.out.println("Enter pub Phone: ");
		String phone = scan.nextLine();
		//1> Register Driver.
		Class.forName(driverName);
		
		//2> Create Connection
		Connection conn = DriverManager.getConnection(url, username, password);
		
		//3> Create statement object
		PreparedStatement pstmt = conn.prepareStatement("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)");
		pstmt.setString(1, authorName);
		pstmt.setString(2, address);
		pstmt.setString(3, phone);
		
		pstmt.execute();
		
	}

}
