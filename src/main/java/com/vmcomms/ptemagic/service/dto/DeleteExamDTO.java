package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the Exam entity.
 */
public class DeleteExamDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

    
}
