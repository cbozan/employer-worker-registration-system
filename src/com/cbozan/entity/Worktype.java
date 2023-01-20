package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.exception.EntityException;

public class Worktype implements Serializable{
	
	private static final long serialVersionUID = 5584866637778593619L;
	
	private int id;
	private String title;
	private int no;
	private Timestamp date;
	
	private Worktype() {
		this.id = 0;
		this.title = null;
		this.no = 0;
		this.date = null;
	}
	
	private Worktype(Worktype.WorktypeBuilder builder) throws EntityException {
		super();
		setId(builder.id);
		setTitle(builder.title);
		setNo(builder.no);
		setDate(builder.date);
	}
	
	public static class WorktypeBuilder {
		private int id;
		private String title;
		private int no;
		private Timestamp date;
		
		public WorktypeBuilder() {}
		public WorktypeBuilder(int id, String title, int no, Timestamp date) {
			this.id = id;
			this.title = title;
			this.no = no;
			this.date = date;
		}
		
		public WorktypeBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public WorktypeBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public WorktypeBuilder setNo(int no) {
			this.no = no;
			return this;
		}
		
		public WorktypeBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Worktype build() throws EntityException {
			return new Worktype(this);
		}
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Worktype instance = new Worktype(); 
	}
	
	public static final Worktype getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Worktype ID negative or zero");
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws EntityException {
		if(title == null || title.length() == 0)
			throw new EntityException("Worktype title null or empty");
		this.title = title;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) throws EntityException {
		if(no <= 0)
			throw new EntityException("Worktype no negative or zero");
		this.no = no;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	
	@Override
	public String toString() {
		return getTitle();
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, no, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worktype other = (Worktype) obj;
		return Objects.equals(date, other.date) && id == other.id && no == other.no
				&& Objects.equals(title, other.title);
	}
	
	

}
