package com.vmcomms.ptemagic.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.vmcomms.ptemagic.domain.enumeration.TestType;

/**
 * A DTO for the ExamType entity.
 */
public class ExamTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private TestType type;

    private Integer numberQuestionWriting;

    private Integer numberQuestionReading;

    private Integer numberQuestionListening;

    private Integer numberQuestionSpeaking;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public Integer getNumberQuestionWriting() {
        return numberQuestionWriting;
    }

    public void setNumberQuestionWriting(Integer numberQuestionWriting) {
        this.numberQuestionWriting = numberQuestionWriting;
    }

    public Integer getNumberQuestionReading() {
        return numberQuestionReading;
    }

    public void setNumberQuestionReading(Integer numberQuestionReading) {
        this.numberQuestionReading = numberQuestionReading;
    }

    public Integer getNumberQuestionListening() {
        return numberQuestionListening;
    }

    public void setNumberQuestionListening(Integer numberQuestionListening) {
        this.numberQuestionListening = numberQuestionListening;
    }

    public Integer getNumberQuestionSpeaking() {
        return numberQuestionSpeaking;
    }

    public void setNumberQuestionSpeaking(Integer numberQuestionSpeaking) {
        this.numberQuestionSpeaking = numberQuestionSpeaking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExamTypeDTO examTypeDTO = (ExamTypeDTO) o;
        if(examTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamTypeDTO{" +
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
