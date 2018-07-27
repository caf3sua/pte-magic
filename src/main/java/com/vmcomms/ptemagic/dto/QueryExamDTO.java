package com.vmcomms.ptemagic.dto;

public class QueryExamDTO {

	private static final long serialVersionUID = 1L;

	private Long id;

    private Long userId;

    private String result;

    private Long examTypeId;

    private String email;
    
    private String examTypeName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getExamTypeId() {
		return examTypeId;
	}

	public void setExamTypeId(Long examTypeId) {
		this.examTypeId = examTypeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExamTypeName() {
		return examTypeName;
	}

	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}
}
