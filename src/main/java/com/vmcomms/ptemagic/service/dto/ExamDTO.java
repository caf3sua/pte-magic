package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;

/**
 * A DTO for the Exam entity.
 */
public class ExamDTO implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if(examDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", examTypeId='" + getExamTypeId() + "'" +
            ", result='" + getResult() + "'" +
            ", scoreWriting='" + getScoreWriting() + "'" +
            ", scoreListening='" + getScoreListening() + "'" +
            ", scoreReading='" + getScoreReading() + "'" +
            ", scoreSpeaking='" + getScoreSpeaking() + "'" +
            "}";
    }
}
