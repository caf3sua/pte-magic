package com.vmcomms.ptemagic.web.rest.vm;


import java.io.Serializable;

import com.vmcomms.ptemagic.domain.enumeration.ProgressType;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the Exam entity.
 */
public class ExamVM implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private Long userId;

    private ProgressType result;

    private Integer scoreWriting;

    private Integer scoreListening;

    private Integer scoreReading;

    private Integer scoreSpeaking;

    private Long examTypeId;

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

    public ProgressType getResult() {
        return result;
    }

    public void setResult(ProgressType result) {
        this.result = result;
    }

    public Integer getScoreWriting() {
        return scoreWriting;
    }

    public void setScoreWriting(Integer scoreWriting) {
        this.scoreWriting = scoreWriting;
    }

    public Integer getScoreListening() {
        return scoreListening;
    }

    public void setScoreListening(Integer scoreListening) {
        this.scoreListening = scoreListening;
    }

    public Integer getScoreReading() {
        return scoreReading;
    }

    public void setScoreReading(Integer scoreReading) {
        this.scoreReading = scoreReading;
    }

    public Integer getScoreSpeaking() {
        return scoreSpeaking;
    }

    public void setScoreSpeaking(Integer scoreSpeaking) {
        this.scoreSpeaking = scoreSpeaking;
    }

    public Long getExamTypeId() {
        return examTypeId;
    }

    public void setExamTypeId(Long examTypeId) {
        this.examTypeId = examTypeId;
    }
}
