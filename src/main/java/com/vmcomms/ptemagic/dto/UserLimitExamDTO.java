package com.vmcomms.ptemagic.dto;

import java.io.Serializable;

/**
 * A Exam.
 */
public class UserLimitExamDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long examTypeId;

    private Integer remainTest;

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

	public Long getExamTypeId() {
		return examTypeId;
	}

	public void setExamTypeId(Long examTypeId) {
		this.examTypeId = examTypeId;
	}

	public Integer getRemainTest() {
		return remainTest;
	}

	public void setRemainTest(Integer remainTest) {
		this.remainTest = remainTest;
	}

    
}
