package com.cbozan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.PaytypeDAO;
import com.cbozan.dao.WorkerDAO;
import com.cbozan.exception.EntityException;

public class Payment implements Serializable {
	
	private static final long serialVersionUID = -4658672540277967255L;
	
	private int id;
	private Worker worker;
	private Job job;
	private Paytype paytype;
	private BigDecimal amount;
	private Timestamp date;
	
	private Payment() {
		this.id = 0;
		this.worker = null;
		this.job = null;
		this.paytype = null;
		this.amount = null;
		this.date = null;
	}
	
	private Payment(Payment.PaymentBuilder builder) throws EntityException {
		this(builder.id,
				WorkerDAO.getInstance().findById(builder.worker_id),
				JobDAO.getInstance().findById(builder.job_id),
				PaytypeDAO.getInstance().findById(builder.paytype_id),
				builder.amount,
				builder.date);
	}
	
	private Payment(int id, Worker worker, Job job, Paytype paytype, BigDecimal amount, Timestamp date) throws EntityException {
		super();
		setId(id);
		setWorker(worker);
		setJob(job);
		setPaytype(paytype);
		setAmount(amount);
		setDate(date);
	}
	

	public static class PaymentBuilder {
		
		private int id;
		private Worker worker;
		private int worker_id;
		private Job job;
		private int job_id;
		private Paytype paytype;
		private int paytype_id;
		private BigDecimal amount;
		private Timestamp date;
		
		public PaymentBuilder() {}
		public PaymentBuilder(int id, int worker_id, int job_id, int paytype_id, BigDecimal amount, Timestamp date) {
			super();
			this.id = id;
			this.worker_id = worker_id;
			this.job_id = job_id;
			this.paytype_id = paytype_id;
			this.amount = amount;
			this.date = date;
		}
		
		public PaymentBuilder(int id, Worker worker, Job job, Paytype paytype, BigDecimal amount, Timestamp date) {
			this(id, 0, 0, 0, amount, date);
			this.worker = worker;
			this.job = job;
			this.paytype = paytype;
		}
		
		public PaymentBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public PaymentBuilder setWorker(Worker worker) {
			this.worker = worker;
			return this;
		}
		
		public PaymentBuilder setWorker_id(int worker_id) {
			this.worker_id = worker_id;
			return this;
		}
		
		public PaymentBuilder setJob(Job job) {
			this.job = job;
			return this;
		}
		
		public PaymentBuilder setJob_id(int job_id) {
			this.job_id = job_id;
			return this;
		}
		
		public PaymentBuilder setPaytype(Paytype paytype) {
			this.paytype = paytype;
			return this;
		}
		
		public PaymentBuilder setPaytype_id(int paytype_id) {
			this.paytype_id = paytype_id;
			return this;
		}
		
		public PaymentBuilder setAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}
		
		public PaymentBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Payment build() throws EntityException{
			if(worker == null || job == null || paytype == null)
				return new Payment(this);
			return new Payment(id, worker, job, paytype, amount, date);
		}
	}

	
	private static class EmptyInstanceSingleton{
		private static final Payment instance = new Payment(); 
	}
	
	public static final Payment getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Payment ID negative or zero");
		this.id = id;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) throws EntityException {
		if(worker == null)
			throw new EntityException("Worker in Payment is null");
		this.worker = worker;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) throws EntityException {
		if(job == null)
			throw new EntityException("Job in Payment is null");
		this.job = job;
	}

	public Paytype getPaytype() {
		return paytype;
	}

	public void setPaytype(Paytype paytype) throws EntityException {
		if(paytype == null)
			throw new EntityException("Paytype in Payment is null");
		this.paytype = paytype;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) throws EntityException {
		if(amount.compareTo(new BigDecimal("0.00")) <= 0)
			throw new EntityException("Payment amount negative or zero");
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
		return "Payment [id=" + id + ", worker=" + worker + ", job=" + job + ", paytype=" + paytype + ", amount="
				+ amount + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, date, id, job, paytype, worker);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(date, other.date) && id == other.id
				&& Objects.equals(job, other.job) && Objects.equals(paytype, other.paytype)
				&& Objects.equals(worker, other.worker);
	}	

}
