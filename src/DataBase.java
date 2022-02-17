import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DataBase {

	public static Connection getConnect() {
		
		Connection conn = null;
		
		try {
			
			// "jdbc:database://host:port/database-name", "user-name", "password"
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db",
					"postgres", "cbZ!");
			
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(null, "Database connection error", "Database", 
					JOptionPane.ERROR_MESSAGE);
			
		}
		
		return conn;
	}
	
	
	/**
	 * 
	 * @param username 
	 * @param password
	 * @return if the user exists, it returns the user id.
	 */
	public static int verifyLogin(String username, String password) {
		
		Connection conn = DataBase.getConnect();
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM admin");
			while(rs.next()) {
				if(rs.getString("username").equals(username) 
						&& rs.getString("password").equals(password))
					return rs.getInt("id");
			}
			
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Database statement error", "Database", 
					JOptionPane.ERROR_MESSAGE);
			
		}
		
		return -1;
		
	}
	
	public static void main(String[] args) {
		
		// MAIN METHOD FOR TESTING

	}

}
