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
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.cbozan.entity.Work;
import com.cbozan.entity.Work.WorkBuilder;
import com.cbozan.entity.Worker;
import com.cbozan.exception.EntityException;

public class WorkDAO {
	
	private final HashMap<Integer, Work> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private WorkDAO() {list();}
	
	// Read by id
	public Work findById(int id) {
		
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
	
	public List<Work> list(Worker worker, String dateStrings){
		List<Work> workList = new ArrayList<>();
		String query = "SELECT * FROM work WHERE worker_id=" + worker.getId();
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
				query = "SELECT * FROM work WHERE worker_id=" + worker.getId() + " AND date >= '" + date1 + "' AND date <= '" + date2 + "';";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
				 query = "SELECT * FROM work WHERE worker_id=" + worker.getId() + " AND date >= '" + date1 + "' AND date <= '" + date2 + "';";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//query = "SELECT * FROM work WHERE worker_id=" + worker.getId();
			return workList;
		}
		
		System.out.println("query : " + query);
		Connection conn;
		Statement st;
		ResultSet rs;
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorkBuilder builder = new WorkBuilder();
			Work work;
			
			while(rs.next()) {
				
				work = findById(rs.getInt("id"));
				if(work != null) { // cache control
					workList.add(work);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setWorktype_id(rs.getInt("worktype_id"));
					builder.setWorkgroup_id(rs.getInt("workgroup_id"));
					builder.setDescription(rs.getString("description"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						work = builder.build();
						workList.add(work);
						cache.put(work.getId(), work);
					} catch (EntityException e) {
						e.printStackTrace();
					}
					
				}
			
			}
			
		} catch(SQLException sqle) {
			
		}
		
		return workList;
		
	}
	
	public List<Work> list(Worker worker){
		
		List<Work> workList = new ArrayList<>();
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM work WHERE worker_id=" + worker.getId();
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorkBuilder builder = new WorkBuilder();
			Work work;
			
			while(rs.next()) {
				
				work = findById(rs.getInt("id"));
				if(work != null) { // cache control
					workList.add(work);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setWorktype_id(rs.getInt("worktype_id"));
					builder.setWorkgroup_id(rs.getInt("workgroup_id"));
					builder.setDescription(rs.getString("description"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						work = builder.build();
						workList.add(work);
						cache.put(work.getId(), work);
					} catch (EntityException e) {
						e.printStackTrace();
					}
					
				}
			
			}
			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return workList;
		
	}
	
	
	
	//Read All
	public List<Work> list(){
		
		List<Work> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Work> obj : cache.entrySet()) {
				list.add(obj.getValue());
			}
			
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM work;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			WorkBuilder builder;
			Work work;
			
			while(rs.next()) {
				
				builder = new WorkBuilder();
				builder.setId(rs.getInt("id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setWorker_id(rs.getInt("worker_id"));
				builder.setWorktype_id(rs.getInt("worktype_id"));
				builder.setWorkgroup_id(rs.getInt("workgroup_id"));
				builder.setDescription(rs.getString("description"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					work = builder.build();
					list.add(work);
					cache.put(work.getId(), work);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
	}
	
	
	public boolean create(Work work) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO work (job_id,worker_id,worktype_id,workgroup_id,description) VALUES (?,?,?,?,?);";
		String query2 = "SELECT * FROM work ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, work.getJob().getId());
			pst.setInt(2, work.getWorker().getId());
			pst.setInt(3, work.getWorktype().getId());
			pst.setInt(4, work.getWorkgroup().getId());
			pst.setString(5, work.getDescription());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					WorkBuilder builder = new WorkBuilder();
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setWorker_id(rs.getInt("worker_id"));
					builder.setWorktype_id(rs.getInt("worktype_id"));
					builder.setWorkgroup_id(rs.getInt("workgroup_id"));
					builder.setDescription(rs.getString("description"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Work w = builder.build();
						cache.put(w.getId(), w);
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
	
	
	public boolean update(Work work) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE work SET job_id=?,"
				+ "worker_id=?, worktype_id=?, workgroup_id=?, description=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, work.getJob().getId());
			pst.setInt(2, work.getWorker().getId());
			pst.setInt(3, work.getWorktype().getId());
			pst.setInt(4, work.getWorkgroup().getId());
			pst.setString(5, work.getDescription());
			pst.setInt(6, work.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(work.getId(), work);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	
	public boolean delete(Work work) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM work WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, work.getId());
			
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(work.getId());
			}

		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	
	private static class WorkDAOHelper {
		private static final WorkDAO instance = new WorkDAO();
	}
	
	public static WorkDAO getInstance() {
		return WorkDAOHelper.instance;
	}

	public boolean isUsingCache() { // isUsingCache??
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
