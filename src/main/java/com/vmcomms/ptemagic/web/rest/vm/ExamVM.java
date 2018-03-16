package com.vmcomms.ptemagic.web.rest.vm;


import java.io.Serializable;

import com.vmcomms.ptemagic.domain.enumeration.ProgressType;

import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the Exam entity.
 */
@Getter
@Setter
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
}
