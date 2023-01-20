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

import com.cbozan.entity.Price;
import com.cbozan.entity.Price.PriceBuilder;
import com.cbozan.exception.EntityException;

public class PriceDAO {

	private final HashMap<Integer, Price> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private PriceDAO() {list();}
	
	// Read by id
	public Price findById(int id) {
		
		if(usingCache == false)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
		
	}
	
	
	//Read All
	public List<Price> list(){
		
		List<Price> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Price> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM price;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			PriceBuilder builder;
			Price price;
			
			while(rs.next()) {
				
				builder = new PriceBuilder();
				builder.setId(rs.getInt("id"));
				builder.setFulltime(rs.getBigDecimal("fulltime"));
				builder.setHalftime(rs.getBigDecimal("halftime"));
				builder.setOvertime(rs.getBigDecimal("overtime"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					price = builder.build();
					list.add(price);
					cache.put(price.getId(), price);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
		
	}
	
	
	public boolean create(Price price) {
		
		if(createControl(price) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO price (fulltime,halftime,overtime) VALUES (?,?,?);";
		String query2 = "SELECT * FROM price ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			
			pst.setBigDecimal(1, price.getFulltime());
			pst.setBigDecimal(2, price.getHalftime());
			pst.setBigDecimal(3, price.getOvertime());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					PriceBuilder builder = new PriceBuilder();
					builder.setId(rs.getInt("id"));
					builder.setFulltime(rs.getBigDecimal("fulltime"));
					builder.setHalftime(rs.getBigDecimal("halftime"));
					builder.setOvertime(rs.getBigDecimal("overtime"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Price prc = builder.build();
						cache.put(prc.getId(), prc);
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
	
	private boolean createControl(Price price) {
		for(Entry<Integer, Price> obj : cache.entrySet()) {
			if(obj.getValue().getFulltime().compareTo(price.getFulltime()) == 0
					&& obj.getValue().getHalftime().compareTo(price.getHalftime()) == 0
					&& obj.getValue().getOvertime().compareTo(price.getOvertime()) == 0) {
				
				DB.ERROR_MESSAGE = "Bu fiyat bilgisi kaydı zaten mevcut.";
				return false;
			}
		}
		
		return true;
	}
	
	
	public boolean update(Price price) {
		
		if(updateControl(price) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE price SET fulltime=?,"
				+ "halftime=?, overtime=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setBigDecimal(1, price.getFulltime());
			pst.setBigDecimal(2, price.getHalftime());
			pst.setBigDecimal(3, price.getOvertime());
			pst.setInt(4, price.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(price.getId(), price);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean updateControl(Price price) {
		for(Entry<Integer, Price> obj : cache.entrySet()) {
			if(obj.getValue().getFulltime().compareTo(price.getFulltime()) == 0
					&& obj.getValue().getHalftime().compareTo(price.getHalftime()) == 0
					&& obj.getValue().getOvertime().compareTo(price.getOvertime()) == 0
					&& obj.getValue().getId() != price.getId()) {
				
				DB.ERROR_MESSAGE = "Bu fiyat bilgisi kaydı zaten mevcut.";
				return false;
			}
		}
		
		return true;
	}
	
	public boolean delete(Price price) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM price WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, price.getId());
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(price.getId());
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	
	private static class PriceDAOHelper {
		private static final PriceDAO instance = new PriceDAO();
	}
	
	public static PriceDAO getInstance() {
		return PriceDAOHelper.instance;
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
