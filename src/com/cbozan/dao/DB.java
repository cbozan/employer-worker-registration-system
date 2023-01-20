package com.cbozan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB {

	public static String ERROR_MESSAGE = "";
	
	private DB() {}
	private Connection conn = null;
	
	private static class DBHelper{
		private static final DB CONNECTION = new DB();
	}
	
	public static Connection getConnection() {
		return DBHelper.CONNECTION.connect();
	}
	
	public static void destroyConnection() {
		DBHelper.CONNECTION.disconnect();
	}

	private Connection connect() {
		
		try {
			if(conn == null || conn.isClosed()) {
				try {
					conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Hesap-eProject", "Hesap-eProject", ".hesap-eProject.");
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	private void disconnect() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	
}
