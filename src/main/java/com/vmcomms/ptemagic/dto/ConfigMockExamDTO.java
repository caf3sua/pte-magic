package com.vmcomms.ptemagic.dto;

import java.io.Serializable;

/**
 * A ConfigMockExam.
 */
public class ConfigMockExamDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long examTypeId;

    private Long questionId;

    private Integer orderId;

    private String questionGroup;
    
    private String questionType;

	public String getQuestionGroup() {
		return questionGroup;
	}

	public void setQuestionGroup(String questionGroup) {
		this.questionGroup = questionGroup;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExamTypeId() {
		return examTypeId;
	}

	public void setExamTypeId(Long examTypeId) {
		this.examTypeId = examTypeId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

    
}
