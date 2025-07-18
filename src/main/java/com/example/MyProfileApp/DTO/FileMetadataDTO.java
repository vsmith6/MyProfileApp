package com.example.MyProfileApp.DTO;



public class FileMetadataDTO {
	
	private String s3Key;
    private String title;
    private String description;
    private long size;
    
    
    public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getS3Key() {
		return s3Key;
	}
	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


    
    
}