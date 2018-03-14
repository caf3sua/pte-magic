package com.vmcomms.ptemagic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.vmcomms.ptemagic.domain.enumeration.ProgressType;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

//    @Column(name = "exam_type_id")
//    private Long examTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private ProgressType result;

    @Column(name = "score_writing")
    private Integer scoreWriting;

    @Column(name = "score_listening")
    private Integer scoreListening;

    @Column(name = "score_reading")
    private Integer scoreReading;

    @Column(name = "score_speaking")
    private Integer scoreSpeaking;

    @OneToOne
    @JoinColumn(unique = true)
    private ExamType examType;

    @OneToMany(mappedBy = "exam")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Question> questions = new HashSet<>();

//    @OneToMany(mappedBy = "exam")
//    @JsonIgnore
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    private Set<Answer> answes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Exam userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    public Long getExamTypeId() {
//        return examTypeId;
//    }
//
//    public Exam examTypeId(Long examTypeId) {
//        this.examTypeId = examTypeId;
//        return this;
//    }
//
//    public void setExamTypeId(Long examTypeId) {
//        this.examTypeId = examTypeId;
//    }

    public ProgressType getResult() {
        return result;
    }

    public Exam result(ProgressType result) {
        this.result = result;
        return this;
    }

    public void setResult(ProgressType result) {
        this.result = result;
    }

    public Integer getScoreWriting() {
        return scoreWriting;
    }

    public Exam scoreWriting(Integer scoreWriting) {
        this.scoreWriting = scoreWriting;
        return this;
    }

    public void setScoreWriting(Integer scoreWriting) {
        this.scoreWriting = scoreWriting;
    }

    public Integer getScoreListening() {
        return scoreListening;
    }

    public Exam scoreListening(Integer scoreListening) {
        this.scoreListening = scoreListening;
        return this;
    }

    public void setScoreListening(Integer scoreListening) {
        this.scoreListening = scoreListening;
    }

    public Integer getScoreReading() {
        return scoreReading;
    }

    public Exam scoreReading(Integer scoreReading) {
        this.scoreReading = scoreReading;
        return this;
    }

    public void setScoreReading(Integer scoreReading) {
        this.scoreReading = scoreReading;
    }

    public Integer getScoreSpeaking() {
        return scoreSpeaking;
    }

    public Exam scoreSpeaking(Integer scoreSpeaking) {
        this.scoreSpeaking = scoreSpeaking;
        return this;
    }

    public void setScoreSpeaking(Integer scoreSpeaking) {
        this.scoreSpeaking = scoreSpeaking;
    }

    public ExamType getExamType() {
        return examType;
    }

    public Exam examType(ExamType examType) {
        this.examType = examType;
        return this;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Exam questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Exam addQuestions(Question question) {
        this.questions.add(question);
        question.setExam(this);
        return this;
    }

    public Exam removeQuestions(Question question) {
        this.questions.remove(question);
        question.setExam(null);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

//    public Set<Answer> getAnswes() {
//        return answes;
//    }
//
//    public Exam answes(Set<Answer> answers) {
//        this.answes = answers;
//        return this;
//    }
//
//    public Exam addAnswes(Answer answer) {
//        this.answes.add(answer);
////        answer.setExam(this);
//        return this;
//    }
//
//    public Exam removeAnswes(Answer answer) {
//        this.answes.remove(answer);
////        answer.setExam(null);
//        return this;
//    }
//
//    public void setAnswes(Set<Answer> answers) {
//        this.answes = answers;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Exam exam = (Exam) o;
        if (exam.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exam.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
//            ", examTypeId='" + getExamTypeId() + "'" +
            ", result='" + getResult() + "'" +
            ", scoreWriting='" + getScoreWriting() + "'" +
            ", scoreListening='" + getScoreListening() + "'" +
            ", scoreReading='" + getScoreReading() + "'" +
            ", scoreSpeaking='" + getScoreSpeaking() + "'" +
            "}";
    }
}
