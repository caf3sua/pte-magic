package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.vmcomms.ptemagic.domain.enumeration.TestType;

/**
 * A DTO for the ExamType entity.
 */
public class ExamTypeDTO implements Serializable {

	private static final long serialVersionUID = -2645519244808522357L;

	private Long id;

    @NotNull
    private String name;

    private TestType type;

    private Integer totalQuestion;

    private String description;

    private Integer numQuestion1;

    private Integer numQuestion2;
    
    private Integer numQuestion3;
    
    private Integer numQuestion4;
    
    private Integer numQuestion5;
    
    private Integer numQuestion6;
    
    private Integer numQuestion7;
    
    private Integer numQuestion8;
    
    private Integer numQuestion9;
    
    private Integer numQuestion10;
    
    private Integer numQuestion11;
    
    private Integer numQuestion12;
    
    private Integer numQuestion13;
    
    private Integer numQuestion14;
    
    private Integer numQuestion15;
    
    private Integer numQuestion16;
    
    private Integer numQuestion17;
    
    private Integer numQuestion18;
    
    private Integer numQuestion19;
    
    private Integer numQuestion20;
    
    private Long totalTime;
    
    private Integer limitTesSilver;
    
    private Integer limitTestGold;
    
    private Integer limitTestPlatinum;
    
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

	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
    
    private int remainTest;
    
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
            ", totalQuestion='" + getTotalQuestion() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

	public Integer getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(Integer totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public int getRemainTest() {
		return remainTest;
	}

	public void setRemainTest(int remainTest) {
		this.remainTest = remainTest;
	}
}
