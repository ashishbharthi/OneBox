package com.onebox.entity;

public class ShowObject extends OneInfyObject{
	private String type;
	private String url;
	private String name;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	public ShowObject(String type, String url) {
		super();
		this.type = type;
		this.url = "dummy/?system="+url;
		this.name = url;
	}
	public void setUrl(String url) {
		this.url = "dummy/?system="+url;
	}
	public String getName() {
		return name;
	}
}
