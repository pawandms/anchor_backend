package com.anchor.app.media.model;

public class DeviceInfo {
	
	private String uuid;
	private String model;
	private String platform;
	private String os;
	private String osVersion;
	private boolean isVirtual;
	private String webViewVersion;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	public boolean isVirtual() {
		return isVirtual;
	}
	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getWebViewVersion() {
		return webViewVersion;
	}
	public void setWebViewVersion(String webViewVersion) {
		this.webViewVersion = webViewVersion;
	}
	
	

}
