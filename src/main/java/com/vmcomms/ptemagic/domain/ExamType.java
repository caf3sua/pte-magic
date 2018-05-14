package com.vmcomms.ptemagic.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.vmcomms.ptemagic.domain.enumeration.TestType;

import io.swagger.annotations.ApiModel;

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

    @Column(name = "total_question")
    private Integer totalQuestion;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "num_question_1")
    private Integer numQuestion1;

    @Column(name = "num_question_2")
    private Integer numQuestion2;
    
    @Column(name = "num_question_3")
    private Integer numQuestion3;
    
    @Column(name = "num_question_4")
    private Integer numQuestion4;
    
    @Column(name = "num_question_5")
    private Integer numQuestion5;
    
    @Column(name = "num_question_6")
    private Integer numQuestion6;
    
    @Column(name = "num_question_7")
    private Integer numQuestion7;
    
    @Column(name = "num_question_8")
    private Integer numQuestion8;
    
    @Column(name = "num_question_9")
    private Integer numQuestion9;
    
    @Column(name = "num_question_10")
    private Integer numQuestion10;
    
    @Column(name = "num_question_11")
    private Integer numQuestion11;
    
    @Column(name = "num_question_12")
    private Integer numQuestion12;
    
    @Column(name = "num_question_13")
    private Integer numQuestion13;
    
    @Column(name = "num_question_14")
    private Integer numQuestion14;
    
    @Column(name = "num_question_15")
    private Integer numQuestion15;
    
    @Column(name = "num_question_16")
    private Integer numQuestion16;
    
    @Column(name = "num_question_17")
    private Integer numQuestion17;
    
    @Column(name = "num_question_18")
    private Integer numQuestion18;
    
    @Column(name = "num_question_19")
    private Integer numQuestion19;
    
    @Column(name = "num_question_20")
    private Integer numQuestion20;
    
    @Column(name = "total_time")
    private Long totalTime;
    
    @Column(name = "limit_test_silver")
    private Integer limitTesSilver;
    
    @Column(name = "limit_test_gold")
    private Integer limitTestGold;
    
    @Column(name = "limit_test_platinum")
    private Integer limitTestPlatinum;
    
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    
    public Long getTotalTime() {
		return totalTime;
	}

	public Integer getLimitTesSilver() {
		return limitTesSilver;
	}

	public void setLimitTesSilver(Integer limitTesSilver) {
		this.limitTesSilver = limitTesSilver;
	}

	public Integer getLimitTestGold() {
		return limitTestGold;
	}

	public void setLimitTestGold(Integer limitTestGold) {
		this.limitTestGold = limitTestGold;
	}

	public Integer getLimitTestPlatinum() {
		return limitTestPlatinum;
	}

	public void setLimitTestPlatinum(Integer limitTestPlatinum) {
		this.limitTestPlatinum = limitTestPlatinum;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public Long getId() {
        return id;
    }

    public Integer getNumQuestion1() {
		return numQuestion1;
	}

	public void setNumQuestion1(Integer numQuestion1) {
		this.numQuestion1 = numQuestion1;
	}

	public Integer getNumQuestion2() {
		return numQuestion2;
	}

	public void setNumQuestion2(Integer numQuestion2) {
		this.numQuestion2 = numQuestion2;
	}

	public Integer getNumQuestion3() {
		return numQuestion3;
	}

	public void setNumQuestion3(Integer numQuestion3) {
		this.numQuestion3 = numQuestion3;
	}

	public Integer getNumQuestion4() {
		return numQuestion4;
	}

	public void setNumQuestion4(Integer numQuestion4) {
		this.numQuestion4 = numQuestion4;
	}

	public Integer getNumQuestion5() {
		return numQuestion5;
	}

	public void setNumQuestion5(Integer numQuestion5) {
		this.numQuestion5 = numQuestion5;
	}

	public Integer getNumQuestion6() {
		return numQuestion6;
	}

	public void setNumQuestion6(Integer numQuestion6) {
		this.numQuestion6 = numQuestion6;
	}

	public Integer getNumQuestion7() {
		return numQuestion7;
	}

	public void setNumQuestion7(Integer numQuestion7) {
		this.numQuestion7 = numQuestion7;
	}

	public Integer getNumQuestion8() {
		return numQuestion8;
	}

	public void setNumQuestion8(Integer numQuestion8) {
		this.numQuestion8 = numQuestion8;
	}

	public Integer getNumQuestion9() {
		return numQuestion9;
	}

	public void setNumQuestion9(Integer numQuestion9) {
		this.numQuestion9 = numQuestion9;
	}

	public Integer getNumQuestion10() {
		return numQuestion10;
	}

	public void setNumQuestion10(Integer numQuestion10) {
		this.numQuestion10 = numQuestion10;
	}

	public Integer getNumQuestion11() {
		return numQuestion11;
	}

	public void setNumQuestion11(Integer numQuestion11) {
		this.numQuestion11 = numQuestion11;
	}

	public Integer getNumQuestion12() {
		return numQuestion12;
	}

	public void setNumQuestion12(Integer numQuestion12) {
		this.numQuestion12 = numQuestion12;
	}

	public Integer getNumQuestion13() {
		return numQuestion13;
	}

	public void setNumQuestion13(Integer numQuestion13) {
		this.numQuestion13 = numQuestion13;
	}

	public Integer getNumQuestion14() {
		return numQuestion14;
	}

	public void setNumQuestion14(Integer numQuestion14) {
		this.numQuestion14 = numQuestion14;
	}

	public Integer getNumQuestion15() {
		return numQuestion15;
	}

	public void setNumQuestion15(Integer numQuestion15) {
		this.numQuestion15 = numQuestion15;
	}

	public Integer getNumQuestion16() {
		return numQuestion16;
	}

	public void setNumQuestion16(Integer numQuestion16) {
		this.numQuestion16 = numQuestion16;
	}

	public Integer getNumQuestion17() {
		return numQuestion17;
	}

	public void setNumQuestion17(Integer numQuestion17) {
		this.numQuestion17 = numQuestion17;
	}

	public Integer getNumQuestion18() {
		return numQuestion18;
	}

	public void setNumQuestion18(Integer numQuestion18) {
		this.numQuestion18 = numQuestion18;
	}

	public Integer getNumQuestion19() {
		return numQuestion19;
	}

	public void setNumQuestion19(Integer numQuestion19) {
		this.numQuestion19 = numQuestion19;
	}

	public Integer getNumQuestion20() {
		return numQuestion20;
	}

	public void setNumQuestion20(Integer numQuestion20) {
		this.numQuestion20 = numQuestion20;
	}

	public Integer getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(Integer totalQuestion) {
		this.totalQuestion = totalQuestion;
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

//    public Integer getNumberQuestionWriting() {
//        return numberQuestionWriting;
//    }
//
//    public ExamType numberQuestionWriting(Integer numberQuestionWriting) {
//        this.numberQuestionWriting = numberQuestionWriting;
//        return this;
//    }
//
//    public void setNumberQuestionWriting(Integer numberQuestionWriting) {
//        this.numberQuestionWriting = numberQuestionWriting;
//    }
//
//    public Integer getNumberQuestionReading() {
//        return numberQuestionReading;
//    }
//
//    public ExamType numberQuestionReading(Integer numberQuestionReading) {
//        this.numberQuestionReading = numberQuestionReading;
//        return this;
//    }
//
//    public void setNumberQuestionReading(Integer numberQuestionReading) {
//        this.numberQuestionReading = numberQuestionReading;
//    }
//
//    public Integer getNumberQuestionListening() {
//        return numberQuestionListening;
//    }
//
//    public ExamType numberQuestionListening(Integer numberQuestionListening) {
//        this.numberQuestionListening = numberQuestionListening;
//        return this;
//    }
//
//    public void setNumberQuestionListening(Integer numberQuestionListening) {
//        this.numberQuestionListening = numberQuestionListening;
//    }
//
//    public Integer getNumberQuestionSpeaking() {
//        return numberQuestionSpeaking;
//    }
//
//    public ExamType numberQuestionSpeaking(Integer numberQuestionSpeaking) {
//        this.numberQuestionSpeaking = numberQuestionSpeaking;
//        return this;
//    }
//
//    public void setNumberQuestionSpeaking(Integer numberQuestionSpeaking) {
//        this.numberQuestionSpeaking = numberQuestionSpeaking;
//    }

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
//            ", numberQuestionWriting='" + getNumberQuestionWriting() + "'" +
//            ", numberQuestionReading='" + getNumberQuestionReading() + "'" +
//            ", numberQuestionListening='" + getNumberQuestionListening() + "'" +
//            ", numberQuestionSpeaking='" + getNumberQuestionSpeaking() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
