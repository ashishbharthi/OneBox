package com.onebox.entity;

public class WorkflowObject extends OneInfyObject{
	private String workflowId;
	private String type;
	
	private String nextUserInputName;
	private String nextUserInputType;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public WorkflowObject(String workflowId, String type,
			String nextUserInputName, String nextUserInputType, String status,
			String statusMessage) {
		super();
		this.workflowId = workflowId;
		this.type = type;
		this.nextUserInputName = nextUserInputName;
		this.nextUserInputType = nextUserInputType;
		this.status = status;
		this.statusMessage = statusMessage;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	private String status;
	private String statusMessage;
	
	
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public WorkflowObject(String workflowId, String type,
			String nextUserInputName, String nextUserInputType) {
		super();
		this.workflowId = workflowId;
		this.type = type;
		this.nextUserInputName = nextUserInputName;
		this.nextUserInputType = nextUserInputType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNextUserInputName() {
		return nextUserInputName;
	}
	public void setNextUserInputName(String nextUserInputName) {
		this.nextUserInputName = nextUserInputName;
	}
	public String getNextUserInputType() {
		return nextUserInputType;
	}
	public void setNextUserInputType(String nextUserInputType) {
		this.nextUserInputType = nextUserInputType;
	}
}
