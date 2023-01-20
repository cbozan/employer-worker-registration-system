package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import com.cbozan.exception.EntityException;
import com.cbozan.util.DBConst;

public final class Worker implements Serializable, Cloneable{

	private static final long serialVersionUID = -8976577868127567445L;
	
	private int id;
	private String fname;
	private String lname;
	private List<String> tel;
	private String iban;
	private String description;
	private Timestamp date;
	
	private Worker() {
		this.id = 0;
		this.fname = null;
		this.lname = null;
		this.tel = null;
		this.iban = null;
		this.description = null;
		this.date = null;
	}
	
	private Worker(Worker.WorkerBuilder builder) throws EntityException {
		super();
		setId(builder.id);
		setFname(builder.fname);
		setLname(builder.lname); 
		setTel(builder.tel);
		setIban(builder.iban);
		setDescription(builder.description);
		setDate(builder.date);
	}
	
	
	// Builder class
	public static class WorkerBuilder{
		
		private int id;
		private String fname;
		private String lname;
		private List<String> tel;
		private String iban;
		private String description;
		private Timestamp date;

		public WorkerBuilder() {}
		public WorkerBuilder(int id, String fname, String lname, List<String> tel, String iban, String description, Timestamp date) {
			super();
			this.id = id;
			this.fname = fname;
			this.lname = lname;
			this.tel = tel;
			this.iban = iban;
			this.description = description;
			this.date = date;
		}

		public WorkerBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public WorkerBuilder setFname(String fname) {
			this.fname = fname;
			return this;
		}

		public WorkerBuilder setLname(String lname) {
			this.lname = lname;
			return this;
		}

		public WorkerBuilder setTel(List<String> tel) {
			this.tel = tel;
			return this;
		}
		
		public WorkerBuilder setIban(String iban) {
			this.iban = iban;
			return this;
		}

		public WorkerBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public WorkerBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Worker build() throws EntityException {
			return new Worker(this);
		}		
		
	}

	
	private static class EmptyInstanceSingleton{
		private static final Worker instance = new Worker(); 
	}
	
	public static final Worker getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Worker ID negative or zero");
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) throws EntityException {
		if(fname.length() == 0 || fname.length() > DBConst.FNAME_LENGTH)
			throw new EntityException("Worker name empty or too long");
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) throws EntityException {
		if(lname.length() == 0 || lname.length() > DBConst.LNAME_LENGTH)
			throw new EntityException("Worker last name empty or too long");
		this.lname = lname;
	}

	public List<String> getTel() {
		return tel;
	}

	public void setTel(List<String> tel) {
		this.tel = tel;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return " " + getFname() + " " + getLname();
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, description, fname, iban, id, lname, tel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worker other = (Worker) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(fname, other.fname) && Objects.equals(iban, other.iban) && id == other.id
				&& Objects.equals(lname, other.lname) && Objects.equals(tel, other.tel);
	}
	
	@Override
	public Worker clone(){
		// TODO Auto-generated method stub
		try {
			return (Worker) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
