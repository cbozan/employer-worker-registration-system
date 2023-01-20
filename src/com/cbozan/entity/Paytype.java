package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.exception.EntityException;

public class Paytype implements Serializable {

	private static final long serialVersionUID = 240445534493303939L;
	
	private int id;
	private String title;
	private Timestamp date;
	
	private Paytype() {
		this.id = 0;
		this.title = null;
		this.date = null;
	}
	
	private Paytype(Paytype.PaytypeBuilder builder) throws EntityException {
		setId(builder.id);
		setTitle(builder.title);
		setDate(builder.date);
	}
	
	
	public static class PaytypeBuilder {
		
		private int id;
		private String title;
		private Timestamp date;
		
		public PaytypeBuilder() {}
		public PaytypeBuilder(int id, String title, Timestamp date) {
			this.id = id;
			this.title = title;
			this.date = date;
		}
		
		public PaytypeBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public PaytypeBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public PaytypeBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Paytype build() throws EntityException {
			return new Paytype(this);
		}
		
	}

	
	private static class EmptyInstanceSingleton{
		private static final Paytype instance = new Paytype(); 
	}
	
	public static final Paytype getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Paytype ID negative or zero");
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws EntityException {
		if(title == null || title.length() == 0)
			throw new EntityException("Paytype title null or empty");
		this.title = title;
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
		return Objects.hash(date, id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paytype other = (Paytype) obj;
		return Objects.equals(date, other.date) && id == other.id && Objects.equals(title, other.title);
	}
	
	
	
}
