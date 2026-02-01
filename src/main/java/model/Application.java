package model;


import java.sql.Timestamp;

public class Application {
    private int appId;
    private int jobId;
    private int seekerId;
    private String status;
    private Timestamp applyDate;
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public int getSeekerId() {
		return seekerId;
	}
	public void setSeekerId(int seekerId) {
		this.seekerId = seekerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Timestamp applyDate) {
		this.applyDate = applyDate;
	}

    // Getters & Setters
    
    
    
}

