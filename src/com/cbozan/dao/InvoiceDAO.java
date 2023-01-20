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

import com.cbozan.entity.Employer;
import com.cbozan.entity.Invoice;
import com.cbozan.entity.Invoice.InvoiceBuilder;
import com.cbozan.exception.EntityException;

public class InvoiceDAO {

	private final HashMap<Integer, Invoice> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private InvoiceDAO() {list();}
	
	// Read by id
	public Invoice findById(int id) {
		
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
	
	
	public List<Invoice> list(Employer employer){

		List<Invoice> invoiceList = new ArrayList<>();
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM invoice WHERE job_id IN (SELECT id FROM job WHERE employer_id=" + employer.getId() + ")";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			InvoiceBuilder builder = new InvoiceBuilder();
			Invoice invoice;
			
			while(rs.next()) {
				
				invoice = findById(rs.getInt("id"));
				if(invoice != null) { // cache control
					invoiceList.add(invoice);
				} else {
					
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("employer_id"));
					builder.setAmount(rs.getBigDecimal("amount"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						invoice = builder.build();
						invoiceList.add(invoice);
						cache.put(invoice.getId(), invoice);
					} catch (EntityException e) {
						showEntityException(e, "İŞVEREN ÖDEME EKLEME HATASI");
					}
					
				}
			
			}
			
		} catch(SQLException sqle) {
			showSQLException(sqle);
		}
		
		return invoiceList;
		
	}
	
	//Read All
	public List<Invoice> list(){

		List<Invoice> list = new ArrayList<>();
		
		if(cache.size() != 0 && usingCache) {
			for(Entry<Integer, Invoice> invoice : cache.entrySet()) {
				list.add(invoice.getValue());
			}
			return list;
		}
		
		cache.clear();
		
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM invoice;";
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			InvoiceBuilder builder;
			Invoice invoice;
			
			while(rs.next()) {
				
				builder = new InvoiceBuilder();
				builder.setId(rs.getInt("id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setAmount(rs.getBigDecimal("amount"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					invoice = builder.build();
					list.add(invoice);
					cache.put(invoice.getId(), invoice);
				} catch (EntityException e) {
					showEntityException(e, "ID : " + rs.getInt("id"));
				}
				
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return list;
	}
	
	
	public List<Invoice> list(String columnName, int id){
		
		List<Invoice> list = new ArrayList<>();
		Connection conn;
		Statement st;
		ResultSet rs;
		String query = "SELECT * FROM invoice WHERE " + columnName + "=" + id;
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			InvoiceBuilder builder;
			Invoice invoice;
			
			while(rs.next()) {
				
				builder = new InvoiceBuilder();
				builder.setId(rs.getInt("id"));
				builder.setJob_id(rs.getInt("job_id"));
				builder.setAmount(rs.getBigDecimal("amount"));
				builder.setDate(rs.getTimestamp("date"));
				
				try {
					invoice = builder.build();
					list.add(invoice);
				} catch (EntityException e) {
					System.err.println(e.getMessage());
					System.out.println("!! Invoice not added list !!");
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return list;
	}
	
	
//	public boolean createControl(Invoice invoice) {
//		
//		Connection conn;
//		Statement st;
//		ResultSet rs;
//		
//		String workgroupQuery = "SELECT * FROM workgroup WHERE job_id=" + invoice.getJob().getId();
//		List<Workgroup> workgroupList = WorkgroupDAO.getInstance().runQuery(workgroupQuery);
//		
//		return false;
//
//		
//	}
	
	
	public boolean create(Invoice invoice) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO invoice (job_id,amount) VALUES (?,?);";
		String query2 = "SELECT * FROM invoice ORDER BY id DESC LIMIT 1;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, invoice.getJob().getId());
			pst.setBigDecimal(2, invoice.getAmount());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				
				ResultSet rs = conn.createStatement().executeQuery(query2);
				while(rs.next()) {
					
					InvoiceBuilder builder = new InvoiceBuilder();
					builder.setId(rs.getInt("id"));
					builder.setJob_id(rs.getInt("job_id"));
					builder.setAmount(rs.getBigDecimal("amount"));
					builder.setDate(rs.getTimestamp("date"));
					
					try {
						Invoice inv = builder.build();
						cache.put(inv.getId(), inv);
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
	
	
	public boolean update(Invoice invoice) {
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE invoice SET job_id=?,"
				+ "amount=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setInt(1, invoice.getJob().getId());
			pst.setBigDecimal(2, invoice.getAmount());
			pst.setInt(5, invoice.getId());
			
			result = pst.executeUpdate();
			
			if(result != 0) {
				cache.put(invoice.getId(), invoice);
			}
			
		} catch (SQLException e) {
			showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	
	public boolean delete(Invoice invoice) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM invoice WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, invoice.getId());
			
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(invoice.getId());
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return result == 0 ? false : true;
		
	}
	
	public boolean isUsingCache() {
		return this.usingCache;
	}
	
	public void setUsingCache(boolean usingCache) {
		this.usingCache = usingCache;
	}
	
	
	private static class InvoiceDAOHelper {
		private static final InvoiceDAO instance = new InvoiceDAO();
	}
	
	public static InvoiceDAO getInstance() {
		return InvoiceDAOHelper.instance;
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
