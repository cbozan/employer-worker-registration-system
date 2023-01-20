package com.cbozan.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.cbozan.dao.JobDAO;
import com.cbozan.dao.WorkerDAO;
import com.cbozan.dao.WorkgroupDAO;
import com.cbozan.dao.WorktypeDAO;
import com.cbozan.exception.EntityException;

public class Work implements Serializable{

	private static final long serialVersionUID = 1466581631433254437L;
	
	private int id;
	private Job job;
	private Worker worker;
	private Worktype worktype;
	private Workgroup workgroup;
	private String description;
	private Timestamp date;
	
	private Work() {
		this.id = 0;
		this.job = null;
		this.worker = null;
		this.worktype = null;
		this.workgroup = null;
		this.description = null;
		this.date = null;
	}
	
	private Work(Work.WorkBuilder builder)  throws EntityException {
		this(builder.id,
				JobDAO.getInstance().findById(builder.job_id),
				WorkerDAO.getInstance().findById(builder.worker_id),
				WorktypeDAO.getInstance().findById(builder.worktype_id),
				WorkgroupDAO.getInstance().findById(builder.workgroup_id),
				builder.description,
				builder.date);
	}
	
	private Work(int id, Job job, Worker worker, Worktype worktype, Workgroup workgroup, String description, Timestamp date)  throws EntityException {
		setId(id);
		setJob(job);
		setWorker(worker);
		setWorktype(worktype);
		setWorkgroup(workgroup);
		setDescription(description);
		setDate(date);
	}

	public static class WorkBuilder {
		
		private int id;
		private int job_id;
		private Job job;
		private int worker_id;
		private Worker worker;
		private int worktype_id;
		private Worktype worktype;
		private int workgroup_id;
		private Workgroup workgroup;
		private String description;
		private Timestamp date;
		
		public WorkBuilder() {}
		public WorkBuilder(int id, int job_id, int worker_id, int worktype_id, int workgroup_id, String description, Timestamp date) {
			this.id = id;
			this.job_id = job_id;
			this.worker_id = worker_id;
			this.worktype_id = worktype_id;
			this.description = description;
			this.date = date;
		}
		
		public WorkBuilder(int id, Job job, Worker worker, Worktype worktype, Workgroup workgroup, String description, Timestamp date) {
			this(id, 0, 0, 0, 0, description, date);
			this.job = job;
			this.worker = worker;
			this.worktype = worktype;
			this.workgroup = workgroup;
		}
		
		public WorkBuilder setId(int id) {
			this.id = id;
			return this;
		}
		
		public WorkBuilder setJob_id(int job_id) {
			this.job_id = job_id;
			return this;
		}
		
		public WorkBuilder setJob(Job job) {
			this.job = job;
			return this;
		}
		
		public WorkBuilder setWorker_id(int worker_id) {
			this.worker_id = worker_id;
			return this;
		}
		
		public WorkBuilder setWorker(Worker worker) {
			this.worker = worker;
			return this;
		}
		
		public WorkBuilder setWorktype_id(int worktype_id) {
			this.worktype_id = worktype_id;
			return this;
		}
		
		public WorkBuilder setWorktype(Worktype worktype) {
			this.worktype = worktype;
			return this;
		}
		
		public WorkBuilder setWorkgroup_id(int workgroup_id) {
			this.workgroup_id = workgroup_id;
			return this;
		}
		
		public WorkBuilder setWorkgroup(Workgroup workgroup) {
			this.workgroup = workgroup;
			return this;
		}
		
		public WorkBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		public WorkBuilder setDate(Timestamp date) {
			this.date = date;
			return this;
		}
		
		public Work build() throws EntityException {
			if(job == null || worker == null || worktype == null || workgroup == null)
				return new Work(this);
			return new Work(id, job, worker, worktype, workgroup, description, date);
		}
		
	}
	
	private static class EmptyInstanceSingleton{
		private static final Work instance = new Work(); 
	}
	
	public static final Work getEmptyInstance() {
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

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) throws EntityException {
		if(worker == null)
			throw new EntityException("Worker in Work is null");
		this.worker = worker;
	}

	public Worktype getWorktype() {
		return worktype;
	}

	public void setWorktype(Worktype worktype) throws EntityException {
		if(worktype == null)
			throw new EntityException("Worktype in Work is null");
		this.worktype = worktype;
	}

	public void setWorkgroup(Workgroup workgroup) {
		this.workgroup = workgroup;
	}
	
	public Workgroup getWorkgroup() {
		return this.workgroup;
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
	
	public WorkerDAO getWorkerDAO() {
		return WorkerDAO.getInstance();
	}
	
	public JobDAO getJobDAO() {
		return JobDAO.getInstance();
	}
	

	@Override
	public String toString() {
		return "Work [id=" + id + ", job=" + job + ", worker=" + worker + ", worktype=" + worktype + ", description="
				+ description + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, description, id, job, worker, workgroup, worktype);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Work other = (Work) obj;
		return Objects.equals(date, other.date) && Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(job, other.job) && Objects.equals(worker, other.worker)
				&& Objects.equals(workgroup, other.workgroup) && Objects.equals(worktype, other.worktype);
	}
	
	
	
}
