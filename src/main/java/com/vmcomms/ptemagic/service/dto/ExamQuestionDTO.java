package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the ExamQuestion entity.
 */
@Getter
@Setter
public class ExamQuestionDTO implements Serializable {

	private static final long serialVersionUID = 4886549106982034411L;

	private Long id;

    private Long examId;

    private Long questionId;
    
    private Integer order;
}
