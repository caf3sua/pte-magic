package com.vmcomms.ptemagic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.vmcomms.ptemagic.domain.enumeration.QuestionType;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private QuestionType type;

    @Column(name = "text")
    private String text;

    @Column(name = "audio_link")
    private String audioLink;

    @Column(name = "description")
    private String description;

    @Column(name = "answer_a")
    private String answerA;

    @Column(name = "answer_b")
    private String answerB;

    @Column(name = "answer_c")
    private String answerC;

    @Column(name = "answer_d")
    private String answerD;

    @Column(name = "answer_e")
    private String answerE;

    @Column(name = "expect_answer")
    private String expectAnswer;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public Question type(QuestionType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public Question text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public Question audioLink(String audioLink) {
        this.audioLink = audioLink;
        return this;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getDescription() {
        return description;
    }

    public Question description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswerA() {
        return answerA;
    }

    public Question answerA(String answerA) {
        this.answerA = answerA;
        return this;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public Question answerB(String answerB) {
        this.answerB = answerB;
        return this;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public Question answerC(String answerC) {
        this.answerC = answerC;
        return this;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public Question answerD(String answerD) {
        this.answerD = answerD;
        return this;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getAnswerE() {
        return answerE;
    }

    public Question answerE(String answerE) {
        this.answerE = answerE;
        return this;
    }

    public void setAnswerE(String answerE) {
        this.answerE = answerE;
    }

    public String getExpectAnswer() {
        return expectAnswer;
    }

    public Question expectAnswer(String expectAnswer) {
        this.expectAnswer = expectAnswer;
        return this;
    }

    public void setExpectAnswer(String expectAnswer) {
        this.expectAnswer = expectAnswer;
    }

    public Boolean isActive() {
        return active;
    }

    public Question active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Exam getExam() {
        return exam;
    }

    public Question exam(Exam exam) {
        this.exam = exam;
        return this;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
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
