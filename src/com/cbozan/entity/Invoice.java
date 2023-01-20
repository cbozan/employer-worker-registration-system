package com.cbozan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.dao.JobDAO;
import com.cbozan.exception.EntityException;

public final class Invoice implements Serializable {
	
	private static final long serialVersionUID = -7032103783419199929L;
	
	private int id;
	private Job job;
	private BigDecimal amount;
	private Timestamp date;
	
	private Invoice() {
		this.id = 0;
		this.job = null;
		this.amount = null;
		this.date = null;
	}
	
	private Invoice(Invoice.InvoiceBuilder builder) throws EntityException {
		this(builder.id,
				JobDAO.getInstance().findById(builder.job_id),
				builder.amount,
				builder.date);
	}
	
	private Invoice(int id, Job job, BigDecimal amount, Timestamp date) throws EntityException {
		setId(id);
		setJob(job);
		setAmount(amount);
		setDate(date);
	}
	
	
	public static class InvoiceBuilder{
		
		private int id;
		private int job_id;
		private Job job;
		private BigDecimal amount;
		private Timestamp date;
		
		public InvoiceBuilder() {}
		public InvoiceBuilder(int id, int job_id, BigDecimal amount, Timestamp date) {
			this.id = id;
			this.job_id = job_id;
			this.amount = amount;
			this.date = date;
		}
		
		public InvoiceBuilder(int id, Job job, BigDecimal amount, Timestamp date) {
			this(id, 0, amount, date);
			this.job = job;
		}
		
		public InvoiceBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public InvoiceBuilder setJob_id(int job_id) {
			this.job_id = job_id;
			return this;
		}
		
		public InvoiceBuilder setJob(Job job) {
			this.job = job;
			return this;
		}
		
		public InvoiceBuilder setAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}
		
		public InvoiceBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Invoice build() throws EntityException {
			if(job == null)
				return new Invoice(this);
			return new Invoice(id, job, amount, date);
		}
		
	}

	private static class EmptyInstanceSingleton{
		private static final Invoice instance = new Invoice(); 
	}
	
	public static final Invoice getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Invoice ID negative or zero");
		this.id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) throws EntityException {
		if(job == null)
			throw new EntityException("Job in Invoice is null");
		this.job = job;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) throws EntityException {
		if(amount.compareTo(new BigDecimal("0.00")) <= 0)
			throw new EntityException("Invoice amount negative or zero");
		this.amount = amount;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	
	@Override
	public String toString() {
		return "Invoice [id=" + id + ", job=" + job + ", amount=" + amount + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, date, id, job);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(date, other.date) && id == other.id
				&& Objects.equals(job, other.job);
	}
	
	
}
