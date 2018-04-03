package com.vmcomms.ptemagic.dto;

import lombok.Getter;
import lombok.Setter;


public class FileDTO {

	private String filename;
	
	private String contentType;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}
