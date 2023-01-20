package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.cbozan.exception.EntityException;
import com.cbozan.util.DBConst;

import java.util.Objects;

public final class Employer implements Serializable, Cloneable{
	
	private static final long serialVersionUID = -465227783930564456L;
	
	private int id;
	private String fname;
	private String lname;
	private List<String> tel;
	private String description;
	private Timestamp date;
	
	private Employer() {
		this.id = 0;
		this.fname = null;
		this.lname = null;
		this.tel = null;
		this.description = null;
		this.date = null;
	}
	
	private Employer(Employer.EmployerBuilder builder) throws EntityException {
		super();
		setId(builder.id);
		setFname(builder.fname);
		setLname(builder.lname); 
		setTel(builder.tel);
		setDescription(builder.description);
		setDate(builder.date);
	}
	
	
	// Builder class
	public static class EmployerBuilder{
		
		private int id;
		private String fname;
		private String lname;
		private List<String> tel;
		private String description;
		private Timestamp date;

		public EmployerBuilder() {}
		public EmployerBuilder(int id, String fname, String lname, List<String> tel, String description, Timestamp date) {
			super();
			this.id = id;
			this.fname = fname;
			this.lname = lname;
			this.tel = tel;
			this.description = description;
			this.date = date;
		}

		public EmployerBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public EmployerBuilder setFname(String fname) {
			this.fname = fname;
			return this;
		}

		public EmployerBuilder setLname(String lname) {
			this.lname = lname;
			return this;
		}

		public EmployerBuilder setTel(List<String> tel) {
			this.tel = tel;
			return this;
		}

		public EmployerBuilder setDescription(String description) {
			this.description = description;
			return this;
		}

		public EmployerBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Employer build() throws EntityException {
			return new Employer(this);
		}		
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Employer instance = new Employer(); 
	}
	
	public static final Employer getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Employer ID negative or zero");
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) throws EntityException {
		if(fname.length() == 0 || fname.length() > DBConst.FNAME_LENGTH)
			throw new EntityException("Employer name empty or too long");
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) throws EntityException {
		if(lname.length() == 0 || lname.length() > DBConst.LNAME_LENGTH)
			throw new EntityException("Employer last name empty or too long");
		this.lname = lname;
	}

	public List<String> getTel() {
		return tel;
	}

	public void setTel(List<String> tel) {
		this.tel = tel;
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
		return getFname() + " " + getLname();
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, description, fname, id, lname, tel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employer other = (Employer) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(fname, other.fname) && id == other.id && Objects.equals(lname, other.lname)
				&& Objects.equals(tel, other.tel);
	}
	
	@Override
	public Employer clone(){
		// TODO Auto-generated method stub
		try {
			return (Employer) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
