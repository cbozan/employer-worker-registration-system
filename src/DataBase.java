import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
			if(st.executeUpdate("INSERT INTO worker(name, surname, phone_number) VALUES "
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


	public static int addRecord(String employer_id, String date, String[] worker_array, String note) {
		
		Connection conn = DataBase.getConnect();
		PreparedStatement pst = null;
		int state = 0;
		
		try {
			pst = conn.prepareStatement("");
			pst = conn.prepareStatement("INSERT INTO employer_record (employer_id, date, note, number_worker, wage) "
					+ "VALUES (?, ?, ?, ?, ?)");
			
			pst.setInt(1, Integer.parseInt(employer_id));
			pst.setString(2, date);
			pst.setString(3, note);
			pst.setShort(4, (short) worker_array.length);
			pst.setShort(5, (short)AdminPanel.WAGE);
			
			if(pst.executeUpdate() > 0) {
				state = 1;
			} 
			
			
			if(state == 1) {
				
				pst = conn.prepareStatement("INSERT INTO worker_record (worker_id, employer_id, date, wage) VALUES (?, ?, ?, ?)");
				for(int i = 0; i < worker_array.length; i++) {
					
					pst.setInt(1, Integer.parseInt(worker_array[i]));
					pst.setInt(2, Integer.parseInt(employer_id));
					pst.setString(3, date);
					pst.setShort(4, AdminPanel.WAGE);
					
					if(pst.executeUpdate() <= 0) {
						pst.close();
						conn.close();
						return i;
					}
					
				}
				
			}
			
			pst.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return state;
	}
	
	
	/**
	 * 
	 * @param tableName 
	 * 		the name of the table whose data you want 
	 * 		to import, for example 'worker' or 'employer'
	 * 
	 * @return arrayList<String[column Size]>
	 */
	public static ArrayList<String[]> getData(String tableName) {
		
		return DataBase.getData(tableName, null);
		
	}
	
	
	public static ArrayList<String[]> getData(String tableName, String condition) {
		
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String[] arrayString;
		
		Connection conn = DataBase.getConnect();
		Statement st = null;
		ResultSet rs = null;
	
		if(condition == null)
			condition = "";
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM " + tableName + " " + condition);
			
			int columnSize = rs.getMetaData().getColumnCount();
			int i;
			while(rs.next()) {
				arrayString = new String[columnSize];
				for(i = 1; i < columnSize + 1; i++) {
					arrayString[i - 1] = rs.getString(i);
				}
				arrayList.add(arrayString);
			}
			
			System.out.println("arrayLength = " + arrayList.size());
			
			rs.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arrayList;
		
	}


	public static boolean workerPayment(String tableName, String worker_id, String employer_id, int paid) {
		Connection conn = DataBase.getConnect();
		PreparedStatement pst;
		int state = 0;
			
			try {
				
				LocalDate nowDate = LocalDate.now();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
				
				pst = conn.prepareStatement("INSERT INTO " + tableName + " (worker_id,employer_id,date,paid) VALUES (?, ?, ?, ?)");
				pst.setInt(1, Integer.parseInt(worker_id));
				pst.setInt(2, Integer.parseInt(employer_id));
				pst.setString(3, ""+nowDate.format(dtf));
				pst.setInt(4, paid);
				
				state = pst.executeUpdate();
				
				pst.close();
				conn.close();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				state = -1;
			}
			
			return state > 0;
		
	}
	
	
	public static boolean employerPayment(String tableName, String employer_id, int paid) {
		Connection conn = DataBase.getConnect();
		PreparedStatement pst;
		int state = 0;
			
			try {
				
				LocalDate nowDate = LocalDate.now();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
				
				pst = conn.prepareStatement("INSERT INTO " + tableName + " (employer_id,date,paid) VALUES (?, ?, ?)");
				pst.setInt(1, Integer.parseInt(employer_id));
				pst.setString(2, nowDate.format(dtf));
				pst.setInt(3, paid);
				
				state = pst.executeUpdate();
				
				pst.close();
				conn.close();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				state = -1;
			}
			
			return state > 0;
		
	}

}
