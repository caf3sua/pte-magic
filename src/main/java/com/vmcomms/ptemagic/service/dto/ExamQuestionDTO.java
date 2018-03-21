package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the ExamQuestion entity.
 */

public class ExamQuestionDTO implements Serializable {

	private static final long serialVersionUID = 4886549106982034411L;

	private Long id;

    private Long examId;

    private Long questionId;

    private Integer orderId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
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
