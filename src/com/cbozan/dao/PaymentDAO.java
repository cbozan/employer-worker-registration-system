package com.cbozan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.cbozan.entity.Payment;
import com.cbozan.entity.Worker;
import com.cbozan.entity.Payment.PaymentBuilder;
import com.cbozan.exception.EntityException;

public class PaymentDAO {
	
	private final HashMap<Integer, Payment> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private PaymentDAO() {list();}
	
	// Read by id
	public Payment findById(int id) {
		
		if(usingCache == false)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
		
	}
	
	public void refresh() {
		setUsingCache(false);
		list();
		setUsingCache(true);
	}
	
	
	//Read All
	public List<Payment> list(){
		
		List<Payment> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Payment> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM payment;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PaymentBuilder builder;
			Payment payment;
			
			while(rs.next()) {
				
				builder = new PaymentBuilder();
				builder.setId(rs.getInt("id"));
				builder.setWorker_id(rs.getInt("worker_id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setPaytype_id(rs.getInt("paytype_id"));
				builder.setAmount(rs.getBigDecimal("amount"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					payment = builder.build();
					list.add(payment);
					cache.put(payment.getId(), payment);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
	}
	
	public List<Payment> list(Worker worker, String dateStrings){
		List<Payment> paymentList = new ArrayList<>();
		String query = "SELECT * FROM payment WHERE worker_id=" + worker.getId();
		String guiDatePattern = "dd/MM/yyyy";
		String dbDatePattern = "yyyy-MM-dd";
		
		StringTokenizer tokenizer = new StringTokenizer(dateStrings, "-");
		
		if(tokenizer.countTokens() == 1) {
			
			Date d1;
			try {
				d1 = new SimpleDateFormat(guiDatePattern).parse(tokenizer.nextToken());
				String date1 = new SimpleDateFormat(dbDatePattern).format(d1);
				d1.setTime(d1.getTime() + 86400000L);
				String date2 = new SimpleDateFormat(dbDatePattern).format(d1);
				query = "SELECT * FROM payment WHERE worker_id=" + worker.getId() + " AND date >= '" + date1 + "' AND date <= '" + date2 + "';";
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			
		} else if(tokenizer.countTokens() == 2) {
			Date d1;
			try {
				 d1 = new SimpleDateFormat(guiDatePattern).parse(tokenizer.nextToken());
				 String date1 = new SimpleDateFormat(dbDatePattern).format(d1);
				 d1 = new SimpleDateFormat(guiDatePattern).parse(tokenizer.nextToken());
				 d1.setTime(d1.getTime() + 86400000L);
				 String date2 = new SimpleDateFormat(dbDatePattern).format(d1);
				 query = "SELECT * FROM payment WHERE worker_id=" + worker.getId() + " AND date >= '" + date1 + "' AND date <= '" + date2 + "';";
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			//query = "SELECT * FROM work WHERE worker_id=" + worker.getId();
			return paymentList;
		}

		
		Connection conn;
		Statement st;
		ResultSet rs;
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PaymentBuilder builder = new PaymentBuilder();
			Payment payment;
			
			while(rs.next()) {
				
				payment = findById(rs.getInt("id"));
				if(payment != null) { // cache control
					paymentList.add(payment);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setPaytype_id(rs.getInt("paytype_id"));
					builder.setAmount(rs.getBigDecimal("amount"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						payment = builder.build();
						paymentList.add(payment);
						cache.put(payment.getId(), payment);
					} catch (EntityException e) {
						showEntityException(e, "işçi ödeme oluşturma hatası.\nTekrar dene");
					}
					
				}
			
			}
			
		} catch(SQLException sqle) {
			showSQLException(sqle);
		}
		
		return paymentList;
		
	}
	
	public List<Payment> list(Worker worker){
		
		List<Payment> paymentList = new ArrayList<>();
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM payment WHERE worker_id=" + worker.getId();
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PaymentBuilder builder = new PaymentBuilder();
			Payment payment;
			
			while(rs.next()) {
				
				payment = findById(rs.getInt("id"));
				if(payment != null) { // cache control
					paymentList.add(payment);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setPaytype_id(rs.getInt("paytype_id"));
					builder.setAmount(rs.getBigDecimal("amount"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						payment = builder.build();
						paymentList.add(payment);
						cache.put(payment.getId(), payment);
					} catch (EntityException e) {
						showEntityException(e, "işçi ödeme oluşturma hatası.\nTekrar dene");
					}
					
				}
			
			}
			
		} catch(SQLException sqle) {
			showSQLException(sqle);
		}
		
		return paymentList;
		
	}
	
	
	public List<Payment> list(String[] columnName, int[] id){
		
		List<Payment> list = new ArrayList<>();
		if(columnName.length != id.length)
			return list;
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM payment WHERE ";
		
		for(int i = 0; i < columnName.length; i++) {
			query += columnName[i] + "=" + id[i] + " AND ";
		}
		
		query = query.substring(0, query.length() - (" AND ".length()));
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PaymentBuilder builder;
			Payment payment;
			
			while(rs.next()) {
				
				builder = new PaymentBuilder();
				builder.setId(rs.getInt("id"));
				builder.setWorker_id(rs.getInt("worker_id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setPaytype_id(rs.getInt("paytype_id"));
				builder.setAmount(rs.getBigDecimal("amount"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					payment = builder.build();
					list.add(payment);
				} catch (EntityException e) {
					System.err.println(e.getMessage());
					System.out.println("!! Payment not added list !!");
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return list;
	}
	
	
	public boolean create(Payment payment) {
		
		if(createControl(payment) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO payment (worker_id,job_id,paytype_id,amount) VALUES (?,?,?,?);";
		String query2 = "SELECT * FROM payment ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, payment.getWorker().getId());
			pst.setInt(2, payment.getJob().getId());
			pst.setInt(3, payment.getPaytype().getId());
			pst.setBigDecimal(4, payment.getAmount());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					PaymentBuilder builder = new PaymentBuilder();
					builder.setId(rs.getInt("id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setPaytype_id(rs.getInt("paytype_id"));
					builder.setAmount(rs.getBigDecimal("amount"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Payment py = builder.build();
						cache.put(py.getId(), py);
					} catch (EntityException e) {
						showEntityException(e, "ID : " + rs.getInt("id"));
					}
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean createControl(Payment payment) {
//		for(Entry<Integer, Payment> obj : cache.entrySet()) {
//			// yeteri kadar �deme alabilir mi? kontrol etme kodu buraya gelecek
//		}
		return true;
	}
	
	
	public boolean update(Payment payment) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE payment SET worker_id=?,"
				+ "job_id=?, paytype_id=?, amount=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, payment.getWorker().getId());
			pst.setInt(2, payment.getJob().getId());
			pst.setInt(3, payment.getPaytype().getId());
			pst.setBigDecimal(4, payment.getAmount());
			pst.setInt(5, payment.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(payment.getId(), payment);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	
	public boolean delete(Payment payment) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM payment WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, payment.getId());
			
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(payment.getId());
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return result == 0 ? false : true;
		
	}
	

	public boolean isUsingCache() {
		return usingCache;
	}

	public void setUsingCache(boolean usingCache) {
		this.usingCache = usingCache;
	}
	
	private static class PaymentDAOHelper {
		private static final PaymentDAO instance = new PaymentDAO();
	}
	
	public static PaymentDAO getInstance() {
		return PaymentDAOHelper.instance;
	}
	
	private void showEntityException(EntityException e, String msg) {
		String message = msg + " not added" + 
				"\n" + e.getMessage() + "\n" + e.getLocalizedMessage() + e.getCause();
			JOptionPane.showMessageDialog(null, message);
	}
	
	private void showSQLException(SQLException e) {
		String message = e.getErrorCode() + "\n" + e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.getCause();
		JOptionPane.showMessageDialog(null, message);
	}

}
