package com.vmcomms.ptemagic.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.vmcomms.ptemagic.domain.enumeration.TestType;

/**
 * A DTO for the ExamType entity.
 */
public class ScoreItemDTO implements Serializable {

    private String questionType;
    
    private int score;
    
    private int totalScore;

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
    
    
}
