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

import com.cbozan.entity.Invoice;
import com.cbozan.entity.Invoice.InvoiceBuilder;
import com.cbozan.entity.Job;
import com.cbozan.entity.Workgroup;
import com.cbozan.entity.Workgroup.WorkgroupBuilder;
import com.cbozan.exception.EntityException;

public class WorkgroupDAO {

	private final HashMap<Integer, Workgroup> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private WorkgroupDAO() {list();}
	
	// Read by id
	public Workgroup findById(int id) {
		
		if(usingCache == false)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
		
	}
	
	public Workgroup getLastAdded() {
		
		Workgroup workgroup = null;
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM workgroup ORDER BY id DESC LIMIT 1";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			WorkgroupBuilder builder; 
			
			while(rs.next()) {
				
				builder = new WorkgroupBuilder();
				builder.setId(rs.getInt("id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setWorktype_id(rs.getInt("worktype_id"));
				builder.setWorkCount(rs.getInt("workcount"));
				builder.setDescription(rs.getString("description"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					workgroup = builder.build();
				} catch (EntityException e) {
					System.err.println(e.getMessage());
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return workgroup;
	}
	
//	public List<Workgroup> runQuery(String query){
//		
//		List<Workgroup> list = new ArrayList<>();
//		Connection conn;
//		Statement st;
//		ResultSet rs;
//		
//		try {
//			conn = DB.getConnection();
//			st = conn.createStatement();
//			rs = st.executeQuery(query);
//			
//			WorkgroupBuilder builder;
//			Workgroup workgroup;
//			
//			while(rs.next()) {
//				
//				builder = new WorkgroupBuilder();
//				builder.setId(rs.getInt("id"));
//				builder.setJob_id(rs.getInt("job_id"));
//				builder.setWorktype_id(rs.getInt("worktype_id"));
//				builder.setWorkCount(rs.getInt("workcount"));
//				builder.setDescription(rs.getString("description"));
//				builder.setDate(rs.getTimestamp("date"));
//				
//				try {
//					workgroup = builder.build();
//					list.add(workgroup);
//				} catch (EntityException e) {
//					System.err.println(e.getMessage());
//					System.out.println("!! Workgroup not added list !!");
//				}
//				
//			}
//			
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		}
//		
//		return list;
//		
//	}
//	
	
	
	public List<Workgroup> list(Job job){
		
		List<Workgroup> workgroupList = new ArrayList<>();
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM workgroup WHERE job_id=" + job.getId();
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorkgroupBuilder builder = new WorkgroupBuilder();
			Workgroup workgroup;
			
			while(rs.next()) {
				
				workgroup = findById(rs.getInt("id"));
				if(workgroup != null) {// cache contrl
					workgroupList.add(workgroup);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setWorktype_id(rs.getInt("worktype_id"));
					builder.setWorkCount(rs.getInt("workcount"));
					builder.setDescription(rs.getString("description"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						workgroup = builder.build();
						workgroupList.add(workgroup);
						cache.put(workgroup.getId(), workgroup);
					} catch (EntityException e) {
						showEntityException(e, "İş grubu eklenemedi.");
					}
					
					
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return workgroupList;
	}
	
	
	//Read All
	public List<Workgroup> list(){
		
		List<Workgroup> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Workgroup> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM workgroup;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorkgroupBuilder builder;
			Workgroup workgroup;
			
			while(rs.next()) {
				
				builder = new WorkgroupBuilder();
				builder.setId(rs.getInt("id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setWorktype_id(rs.getInt("worktype_id"));
				builder.setWorkCount(rs.getInt("workcount"));
				builder.setDescription(rs.getString("description"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					workgroup = builder.build();
					list.add(workgroup);
					cache.put(workgroup.getId(), workgroup);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
	}
	 
	
	public boolean create(Workgroup workgroup) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO workgroup (job_id,worktype_id,workcount,description) VALUES (?,?,?,?);";
		String query2 = "SELECT * FROM workgroup ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, workgroup.getJob().getId());
			pst.setInt(2, workgroup.getWorktype().getId());
			pst.setInt(3, workgroup.getWorkCount());
			pst.setString(4, workgroup.getDescription());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					WorkgroupBuilder builder = new WorkgroupBuilder();
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setWorktype_id(rs.getInt("worktype_id"));
					builder.setWorkCount(rs.getInt("workcount"));
					builder.setDescription(rs.getString("description"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Workgroup wg = builder.build();
						cache.put(wg.getId(), wg);
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
	
	
	public boolean update(Workgroup workgroup) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE workgroup SET job_id=?,"
				+ "worktype_id=?, workcount=?, description=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, workgroup.getJob().getId());
			pst.setInt(2, workgroup.getWorktype().getId());
			pst.setInt(3, workgroup.getWorkCount());
			pst.setString(4, workgroup.getDescription());
			pst.setInt(5, workgroup.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(workgroup.getId(), workgroup);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	
	public boolean delete(Workgroup workgroup) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM workgroup WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, workgroup.getId());
			
			result = ps.executeUpdate();

			if(result != 0) {
				cache.remove(workgroup.getId());
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	// Billpugh singleton
	private static class WorkgroupDAOHelper {
		private static final WorkgroupDAO instance = new WorkgroupDAO();
	}
	
	public static WorkgroupDAO getInstance() {
		return WorkgroupDAOHelper.instance;
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
