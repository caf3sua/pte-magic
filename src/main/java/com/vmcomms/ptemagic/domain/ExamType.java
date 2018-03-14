package com.vmcomms.ptemagic.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.vmcomms.ptemagic.domain.enumeration.TestType;

/**
 * The Employee entity.
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "exam_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private TestType type;

    @Column(name = "number_question_writing")
    private Integer numberQuestionWriting;

    @Column(name = "number_question_reading")
    private Integer numberQuestionReading;

    @Column(name = "number_question_listening")
    private Integer numberQuestionListening;

    @Column(name = "number_question_speaking")
    private Integer numberQuestionSpeaking;

    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ExamType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestType getType() {
        return type;
    }

    public ExamType type(TestType type) {
        this.type = type;
        return this;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public Integer getNumberQuestionWriting() {
        return numberQuestionWriting;
    }

    public ExamType numberQuestionWriting(Integer numberQuestionWriting) {
        this.numberQuestionWriting = numberQuestionWriting;
        return this;
    }

    public void setNumberQuestionWriting(Integer numberQuestionWriting) {
        this.numberQuestionWriting = numberQuestionWriting;
    }

    public Integer getNumberQuestionReading() {
        return numberQuestionReading;
    }

    public ExamType numberQuestionReading(Integer numberQuestionReading) {
        this.numberQuestionReading = numberQuestionReading;
        return this;
    }

    public void setNumberQuestionReading(Integer numberQuestionReading) {
        this.numberQuestionReading = numberQuestionReading;
    }

    public Integer getNumberQuestionListening() {
        return numberQuestionListening;
    }

    public ExamType numberQuestionListening(Integer numberQuestionListening) {
        this.numberQuestionListening = numberQuestionListening;
        return this;
    }

    public void setNumberQuestionListening(Integer numberQuestionListening) {
        this.numberQuestionListening = numberQuestionListening;
    }

    public Integer getNumberQuestionSpeaking() {
        return numberQuestionSpeaking;
    }

    public ExamType numberQuestionSpeaking(Integer numberQuestionSpeaking) {
        this.numberQuestionSpeaking = numberQuestionSpeaking;
        return this;
    }

    public void setNumberQuestionSpeaking(Integer numberQuestionSpeaking) {
        this.numberQuestionSpeaking = numberQuestionSpeaking;
    }

    public String getDescription() {
        return description;
    }

    public ExamType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        ExamType examType = (ExamType) o;
        if (examType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", numberQuestionWriting='" + getNumberQuestionWriting() + "'" +
            ", numberQuestionReading='" + getNumberQuestionReading() + "'" +
            ", numberQuestionListening='" + getNumberQuestionListening() + "'" +
            ", numberQuestionSpeaking='" + getNumberQuestionSpeaking() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
