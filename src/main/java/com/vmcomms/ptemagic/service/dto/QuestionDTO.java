package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;

/**
 * A DTO for the Question entity.
 */
public class QuestionDTO implements Serializable {

	private static final long serialVersionUID = -6155814844464620154L;

	private Long id;

    private QuestionType type;

    private String text;

    private String audioLink;

    private String description;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    private String answerE;

    private String expectAnswer;

    private Boolean active;

    private Long examId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getAnswerE() {
        return answerE;
    }

    public void setAnswerE(String answerE) {
        this.answerE = answerE;
    }

    public String getExpectAnswer() {
        return expectAnswer;
    }

    public void setExpectAnswer(String expectAnswer) {
        this.expectAnswer = expectAnswer;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if(questionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", audioLink='" + getAudioLink() + "'" +
            ", description='" + getDescription() + "'" +
            ", answerA='" + getAnswerA() + "'" +
            ", answerB='" + getAnswerB() + "'" +
            ", answerC='" + getAnswerC() + "'" +
            ", answerD='" + getAnswerD() + "'" +
            ", answerE='" + getAnswerE() + "'" +
            ", expectAnswer='" + getExpectAnswer() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}

//class SortQuestion implements Comparator<QuestionDTO>
//{
//    // Used for sorting in ascending order of
//    // roll number
//    public int compare(QuestionDTO a, QuestionDTO b)
//    {
//        return a.get
//    }
//}