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

import com.cbozan.entity.Worktype;
import com.cbozan.entity.Worktype.WorktypeBuilder;
import com.cbozan.exception.EntityException;

public class WorktypeDAO {
	
	private final HashMap<Integer, Worktype> cache = new HashMap<>();
	private boolean usingCache = true;
	
	
	private WorktypeDAO() {list();}
	
	// Read by id
	public Worktype findById(int id) {
		
		if(usingCache == false)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
		
	}
	
	
	//Read All
	public List<Worktype> list(){
		
		List<Worktype> list = new ArrayList<>();
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Worktype> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM worktype;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorktypeBuilder builder;
			Worktype worktype;
			
			while(rs.next()) {
				
				builder = new WorktypeBuilder();
				builder.setId(rs.getInt("id"));
				builder.setTitle(rs.getString("title"));
				builder.setNo(rs.getInt("no"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					worktype = builder.build();
					list.add(worktype);
					cache.put(worktype.getId(), worktype);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id") + ", Title : " + rs.getString("title"));
				}
				
			}
			
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
		
	}
	
	
	public boolean create(Worktype worktype) {
		
		if(createControl(worktype) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO worktype (title,no) VALUES (?,?);";
		String query2 = "SELECT * FROM worktype ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, worktype.getTitle());
			pst.setInt(2, worktype.getNo());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					WorktypeBuilder builder = new WorktypeBuilder();
					builder = new WorktypeBuilder();
					builder.setId(rs.getInt("id"));
					builder.setTitle(rs.getString("title"));
					builder.setNo(rs.getInt("no"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Worktype wt = builder.build();
						cache.put(wt.getId(), wt);
					} catch (EntityException e) {
						showEntityException(e, "ID : " + rs.getInt("id") + ", Title : " + rs.getString("title"));
					}
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean createControl(Worktype worktype) {
		for(Entry<Integer, Worktype> obj : cache.entrySet()) {
			if(obj.getValue().getTitle().equals(worktype.getTitle())) {
				return false;
			}
		}
		return true;
	}
	
	
	public boolean update(Worktype worktype) {
		
		if(updateControl(worktype) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE worktype SET title=?,"
				+ "no=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, worktype.getTitle());
			pst.setInt(2, worktype.getNo());
			pst.setInt(3, worktype.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(worktype.getId(), worktype);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean updateControl(Worktype worktype) {
		for(Entry<Integer, Worktype> obj : cache.entrySet()) {
			if(obj.getValue().getTitle().equals(worktype.getTitle()) && obj.getValue().getId() != worktype.getId()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean delete(Worktype worktype) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM worktype WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, worktype.getId());

			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(worktype.getId());
			}

		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	
	private static class WorktypeDAOHelper {
		private static final WorktypeDAO instance = new WorktypeDAO();
	}
	
	public static WorktypeDAO getInstance() {
		return WorktypeDAOHelper.instance;
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
