import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	
	/**
	 * 
	 * @param name
	 * @param surname
	 * @param phoneNumber
	 * @return added: true, not added: false
	 */
	
	public static boolean addWorker(String name, String surname, String phoneNumber) {
		
		Connection conn = DataBase.getConnect();
		Statement st = null;
		boolean state = false;
		
		try {
			st = conn.createStatement();
			if(st.executeUpdate("INSERT INTO worker(name, surname, phonenumber) VALUES "
					+ "('"+name+"','"+surname+"','"+phoneNumber+"')") > 0) {
				state = true;
			}
			
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return state;
		
	}
	
	
	/**
	 * 
	 * @param name
	 * @param surname
	 * @param business
	 * @param phoneNumber
	 * @return added: true, not added: false
	 */
	
	public static boolean addEmployer(String name, String surname, String business, String phoneNumber) {
		
		Connection conn = DataBase.getConnect();
		PreparedStatement pst = null;
		boolean state = false;
		
		try {
			pst = conn.prepareStatement("INSERT INTO employer (name, surname, business, phonenumber) VALUES (?, ?, ?, ?)");
			pst.setString(1, name);
			pst.setString(2, surname);
			pst.setString(3, business);
			pst.setString(4, phoneNumber);
			
			if(pst.executeUpdate() > 0) {
				state = true;
			}
			
			pst.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return state;
	}
	
	public static void main(String[] args) {
		
		// MAIN METHOD FOR TESTING

	}

}
