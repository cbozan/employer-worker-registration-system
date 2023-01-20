package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.dao.EmployerDAO;
import com.cbozan.dao.PriceDAO;
import com.cbozan.exception.EntityException;

public class Job implements Serializable, Cloneable{

	private static final long serialVersionUID = 9178806163995887915L;
	
	private int id;
	private Employer employer;
	private Price price;
	private String title;
	private String description;
	private Timestamp date;
	
	private Job() {
		this.id = 0;
		this.employer = null;
		this.price = null;
		this.title = null;
		this.description = null;
		this.date = null;
	}
	
	private Job(Job.JobBuilder builder) throws EntityException{
		
		this(builder.id,
				EmployerDAO.getInstance().findById(builder.employer_id),
				PriceDAO.getInstance().findById(builder.price_id),
				builder.title,
				builder.description,
				builder.date);
		
	}
	
	private Job(int id, Employer employer, Price price, String title, String description, Timestamp date) throws EntityException{
		super();
		setId(id);
		setEmployer(employer);
		setPrice(price);
		setTitle(title);
		setDescription(description);
		setDate(date);
	}
	
	
	public static class JobBuilder {
		
		private int id;
		private int employer_id;
		private int price_id;
		private String title;
		private String description;
		private Timestamp date;
		private Employer employer;
		private Price price;
		
		public JobBuilder() {}
		public JobBuilder(int id, int employer_id, int price_id, String title, String description, Timestamp date) {
			this.id = id;
			this.employer_id = employer_id;
			this.price_id = price_id;
			this.title = title;
			this.description = description;
			this.date = date;
		}
		
		public JobBuilder(int id, Employer employer, Price price, String title, String description, Timestamp date) {
			this(id, 0, 0, title, description, date);
			this.employer = employer;
			this.price = price;
		}
		
		public JobBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public JobBuilder setEmployer_id(int employer_id) {
			this.employer_id = employer_id;
			return this;
		}
		
		public JobBuilder setPrice_id(int price_id) {
			this.price_id = price_id;
			return this;
		}
		
		public JobBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public JobBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public JobBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public JobBuilder setEmployer(Employer employer) {
			this.employer = employer;
			return this;
		}
		
		public JobBuilder setPrice(Price price) {
			this.price = price;
			return this;
		}
		
		public Job build() throws EntityException{
			if(this.employer == null || this.price == null)
				return new Job(this);
			return new Job(this.id, this.employer, this.price, this.title, this.description, this.date);
		}
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Job instance = new Job(); 
	}
	
	public static final Job getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) throws EntityException {
		if(id <= 0)
			throw new EntityException("Job ID negative or zero");
		this.id = id;
	}


	public Employer getEmployer() {
		return employer;
	}


	public void setEmployer(Employer employer) throws EntityException {
		if(employer == null)
			throw new EntityException("Employer in Job is null");
		this.employer = employer;
	}


	public Price getPrice() {
		return price;
	}


	public void setPrice(Price price) throws EntityException {
		if(price == null)
			throw new EntityException("Price in Job is null");
		this.price = price;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) throws EntityException {
		if(title == null || title.length() == 0)
			throw new EntityException("Job title null or empty");
		this.title = title;
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
	
	public EmployerDAO getEmployerDAO() {
		return EmployerDAO.getInstance();
	}
	
	public PriceDAO getPriceDAO() {
		return PriceDAO.getInstance();
	}
	
	
	
	@Override
	public String toString() {
		return getTitle();
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, description, employer, id, price, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(employer, other.employer) && id == other.id && Objects.equals(price, other.price)
				&& Objects.equals(title, other.title);
	}
	
	@Override
	public Job clone(){
		// TODO Auto-generated method stub
		try {
			return (Job) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
