package com.changelog.mdevz.model;

import com.google.gson.annotations.SerializedName;

public class Changelog {
	@SerializedName("version")
	private String version;
	
	@SerializedName("date")
	private String date;
	
	@SerializedName("message")
	private String message;
	
	@SerializedName("developer")
	private String developer;
	
	public Changelog(String version, String date, String message, String developer) {
		this.version = version;
		this.date = date;
		this.message = message;
		this.developer = developer;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDeveloper() {
		return developer;
	}
}