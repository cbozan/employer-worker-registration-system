package com.cbozan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.cbozan.entity.Paytype;
import com.cbozan.entity.Paytype.PaytypeBuilder;
import com.cbozan.exception.EntityException;

public class PaytypeDAO {
	
	private final HashMap<Integer, Paytype> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private PaytypeDAO() {list();}
	
	// Read by id
	public Paytype findById(int id) {
		
		if(usingCache == false)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
		
	}
	
	
	//Read All
	public List<Paytype> list(){
		
		List<Paytype> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Paytype> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM paytype;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PaytypeBuilder builder;
			Paytype paytype;
			
			while(rs.next()) {
				
				builder = new PaytypeBuilder();
				builder.setId(rs.getInt("id"));
				builder.setTitle(rs.getString("title"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					paytype = builder.build();
					list.add(paytype);
					cache.put(paytype.getId(), paytype);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id") + " Title : " + rs.getString("title"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
	}
	
	
	public boolean create(Paytype paytype) {
		
		if(createControl(paytype) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO paytype (title) VALUES (?);";
		String query2 = "SELECT * FROM paytype ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, paytype.getTitle());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					PaytypeBuilder builder = new PaytypeBuilder();
					builder.setId(rs.getInt("id"));
					builder.setTitle(rs.getString("title"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Paytype pt = builder.build();
						cache.put(pt.getId(), pt);
					} catch (EntityException e) {
						showEntityException(e, "ID : " + rs.getInt("id") + " Title : " + rs.getString("title"));
					}
					
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean createControl(Paytype paytype) {
		for(Entry<Integer, Paytype> obj : cache.entrySet()) {
			if(obj.getValue().getTitle().equals(paytype.getTitle())) {
				DB.ERROR_MESSAGE = obj.getValue().getTitle() + " kaydı zaten mevcut.";
				return false;
			}
		}
		return true;
	}
	
	public boolean update(Paytype paytype) {
		
		if(updateControl(paytype) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE paytype SET title=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, paytype.getTitle());
			pst.setInt(2, paytype.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(paytype.getId(), paytype);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean updateControl(Paytype paytype) {
		for(Entry<Integer, Paytype> obj : cache.entrySet()) {
			if(obj.getValue().getTitle().equals(paytype.getTitle()) && obj.getValue().getId() != paytype.getId()) {
				DB.ERROR_MESSAGE = obj.getValue().getTitle() + " kaydı zaten mevcut.";
				return false;
			}
		}
		return true;
	}
	
	
	public boolean delete(Paytype paytype) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM paytype WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, paytype.getId());
			
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(paytype.getId());
			}

		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	
	private static class PaytypeDAOHelper {
		private static final PaytypeDAO instance = new PaytypeDAO();
	}
	
	public static PaytypeDAO getInstance() {
		return PaytypeDAOHelper.instance;
	}

	public boolean isUsingCache() {
		return usingCache;
	}

	public void setUsingCache(boolean usingCache) {
		this.usingCache = usingCache;
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
