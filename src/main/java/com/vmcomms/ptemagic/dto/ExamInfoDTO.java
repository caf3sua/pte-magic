package com.vmcomms.ptemagic.dto;

import java.util.List;

import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamInfoDTO {

	private ExamDTO examDTO;
	
	private List<QuestionDTO> questions;
}
