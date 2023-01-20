package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.WorktypeDAO;
import com.cbozan.exception.EntityException;

public class Workgroup implements Serializable{

	private static final long serialVersionUID = 8492995365698481936L;
	
	
	private int id;
	private Job job;
	private Worktype worktype;
	private int workCount;
	private String description;
	private Timestamp date;
	
	private Workgroup() {
		this.id = 0;
		this.job = null;
		this.worktype = null;
		this.workCount = 0;
		this.description = null;
		this.date = null;
	}
	
	private Workgroup(Workgroup.WorkgroupBuilder builder)  throws EntityException {
		this(builder.id,
				JobDAO.getInstance().findById(builder.job_id),
				WorktypeDAO.getInstance().findById(builder.worktype_id),
				builder.workCount,
				builder.description,
				builder.date);
	}
	
	private Workgroup(int id, Job job, Worktype worktype, int workCount, String description, Timestamp date)  throws EntityException {
		setId(id);
		setJob(job);
		setWorktype(worktype);
		setWorkCount(workCount);
		setDescription(description);
		setDate(date);
	}
	
	
	public static class WorkgroupBuilder {
		
		private int id;
		private int job_id;
		private Job job;
		private int worktype_id;
		private Worktype worktype;
		private int workCount;
		private String description;
		private Timestamp date;
		
		public WorkgroupBuilder() {}
		public WorkgroupBuilder(int id, int job_id, int worktype_id, int workCount, String description, Timestamp date) {
			this.id = id;
			this.job_id = job_id;
			this.worktype_id = worktype_id;
			this.workCount = workCount;
			this.description = description;
			this.date = date;
		}
		
		public WorkgroupBuilder(int id, Job job, Worktype worktype, int workCount, String description, Timestamp date) {
			this(id, 0, 0, workCount, description, date);
			this.job = job;
			this.worktype = worktype;
		}
		
		public WorkgroupBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public WorkgroupBuilder setJob_id(int job_id) {
			this.job_id = job_id;
			return this;
		}
		
		public WorkgroupBuilder setJob(Job job) {
			this.job = job;
			return this;
		}
		
		public WorkgroupBuilder setWorktype_id(int worktype_id) {
			this.worktype_id = worktype_id;
			return this;
		}
		
		public WorkgroupBuilder setWorktype(Worktype worktype) {
			this.worktype = worktype;
			return this;
		}
		
		public WorkgroupBuilder setWorkCount(int workCount) {
			this.workCount = workCount;
			return this;
		}
		
		public WorkgroupBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public WorkgroupBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Workgroup build() throws EntityException {
			if(job == null || worktype == null)
				return new Workgroup(this);
			return new Workgroup(id, job, worktype, workCount, description, date);
		}
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Workgroup instance = new Workgroup(); 
	}
	
	public static final Workgroup getEmptyInstance() {
		return EmptyInstanceSingleton.instance;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) throws EntityException{
		if(id <= 0)
			throw new EntityException("Work ID negative or zero");
		this.id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) throws EntityException {
		if(job == null)
			throw new EntityException("Job in Work is null");
		this.job = job;
	}

	public Worktype getWorktype() {
		return worktype;
	}

	public void setWorktype(Worktype worktype) throws EntityException {
		if(worktype == null)
			throw new EntityException("Worktype in Work is null");
		this.worktype = worktype;
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

	public WorktypeDAO getWorktypeDAO() {
		return WorktypeDAO.getInstance();
	}
	
	public JobDAO getJobDAO() {
		return JobDAO.getInstance();
	}
	

	public int getWorkCount() {
		return workCount;
	}

	public void setWorkCount(int workCount) {
		this.workCount = workCount;
	}

	@Override
	public String toString() {
		return "Workgroup [id=" + id + ", job=" + job + ", worktype=" + worktype + ", workCount=" + workCount
				+ ", description=" + description + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, description, id, job, workCount, worktype);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Workgroup other = (Workgroup) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(job, other.job) && workCount == other.workCount
				&& Objects.equals(worktype, other.worktype);
	}
	
}

