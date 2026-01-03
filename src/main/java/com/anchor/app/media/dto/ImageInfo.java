package com.anchor.app.media.dto;

public class ImageInfo {
	
	private String name;
	private String extn;
	private long size;
	private int width;
	private int height;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtn() {
		return extn;
	}
	
	
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public void setExtn(String extn) {
		this.extn = extn;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
