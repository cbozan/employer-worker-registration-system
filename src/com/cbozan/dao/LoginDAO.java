package com.cbozan.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
	
	public boolean verifyLogin(String username, String pass) {
		
		Connection conn;
		Statement st;
		ResultSet rs;
	
		String query = "SELECT id FROM auth WHERE username='" + username + "' AND pass='" + pass + "';";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			if(rs.next())
				return true;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
