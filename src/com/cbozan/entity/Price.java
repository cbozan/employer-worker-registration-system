package com.cbozan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.exception.EntityException;

public class Price implements Serializable{

	private static final long serialVersionUID = -3677724411405182082L;
	
	private int id;
	private BigDecimal fulltime;
	private BigDecimal halftime;
	private BigDecimal overtime;
	private Timestamp date;
	
	private Price() {
		this.id = 0;
		this.fulltime = null;
		this.halftime = null;
		this.overtime = null;
		this.date = null;
	}
	
	private Price(Price.PriceBuilder builder) throws EntityException {
		super();
		setId(builder.id);
		setFulltime(builder.fulltime);
		setHalftime(builder.halftime);
		setOvertime(builder.overtime);
		setDate(builder.date);
	}
	
	
	public static class PriceBuilder{
		
		private int id;
		private BigDecimal fulltime;
		private BigDecimal halftime;
		private BigDecimal overtime;
		private Timestamp date;
		
		public PriceBuilder() {}

		public PriceBuilder(int id, BigDecimal fulltime, BigDecimal halftime, BigDecimal overtime, Timestamp date) {
			super();
			this.id = id;
			this.fulltime = fulltime;
			this.halftime = halftime;
			this.overtime = overtime;
			this.date = date;
		}

		public PriceBuilder setId(int id) {
			this.id = id;
			return this;
		}

		public PriceBuilder setFulltime(BigDecimal fulltime) {
			this.fulltime = fulltime;
			return this;
		}

		public PriceBuilder setHalftime(BigDecimal halftime) {
			this.halftime = halftime;
			return this;
		}

		public PriceBuilder setOvertime(BigDecimal overtime) {
			this.overtime = overtime;
			return this;
		}

		public PriceBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Price build() throws EntityException{
			return new Price(this);
		}
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Price instance = new Price(); 
	}
	
	public static final Price getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Price ID negative or zero");
		this.id = id;
	}

	public BigDecimal getFulltime() {
		return fulltime;
	}

	public void setFulltime(BigDecimal fulltime) throws EntityException {
		if(fulltime == null || fulltime.compareTo(new BigDecimal("0.00")) <= 0)
			throw new EntityException("Price fulltime negative or null or zero");
		this.fulltime = fulltime;
	}

	public BigDecimal getHalftime() {
		return halftime;
	}

	public void setHalftime(BigDecimal halftime) throws EntityException {
		if(halftime == null || halftime.compareTo(new BigDecimal("0.00")) <= 0)
			throw new EntityException("Price halftime negative or null or zero");
		this.halftime = halftime;
	}

	public BigDecimal getOvertime() {
		return overtime;
	}

	public void setOvertime(BigDecimal overtime) throws EntityException {
		if(overtime == null || overtime.compareTo(new BigDecimal("0.00")) <= 0)
			throw new EntityException("Price overtime negative or null or zero");
		this.overtime = overtime;
	}

	public Timestamp getDate() {
		return date;
	}


	public void setDate(Timestamp date) {
		this.date = date;
	}
	

	@Override
	public String toString() {
		return "(" + getFulltime() + ")  " + "(" + getHalftime() + ")  " + "(" + getOvertime() + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, fulltime, halftime, id, overtime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Price other = (Price) obj;
		return Objects.equals(date, other.date) && Objects.equals(fulltime, other.fulltime)
				&& Objects.equals(halftime, other.halftime) && id == other.id
				&& Objects.equals(overtime, other.overtime);
	}
	
	
	
}
