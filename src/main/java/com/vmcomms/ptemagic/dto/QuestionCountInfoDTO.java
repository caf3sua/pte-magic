package com.vmcomms.ptemagic.dto;

import com.vmcomms.ptemagic.domain.enumeration.QuestionType;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestionCountInfoDTO {

	private QuestionType type;
	
	private String name;

	private long total;
}
