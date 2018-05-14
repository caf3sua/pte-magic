package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;

/**
 * A DTO for the ExamType entity.
 */
public class MockExamDTO implements Serializable {

	private static final long serialVersionUID = -2645519244808522357L;

    private ExamTypeDTO examTypeDTO;

    private List<ConfigMockExamDTO> lstSpeaking = new ArrayList<>();
    
    private List<ConfigMockExamDTO> lstWriting = new ArrayList<>();
    
    private List<ConfigMockExamDTO> lstReading = new ArrayList<>();
    
    private List<ConfigMockExamDTO> lstListening = new ArrayList<>();

	public ExamTypeDTO getExamTypeDTO() {
		return examTypeDTO;
	}

	public void setExamTypeDTO(ExamTypeDTO examTypeDTO) {
		this.examTypeDTO = examTypeDTO;
	}

	public List<ConfigMockExamDTO> getLstSpeaking() {
		return lstSpeaking;
	}

	public void setLstSpeaking(List<ConfigMockExamDTO> lstSpeaking) {
		this.lstSpeaking = lstSpeaking;
	}

	public List<ConfigMockExamDTO> getLstWriting() {
		return lstWriting;
	}

	public void setLstWriting(List<ConfigMockExamDTO> lstWriting) {
		this.lstWriting = lstWriting;
	}

	public List<ConfigMockExamDTO> getLstReading() {
		return lstReading;
	}

	public void setLstReading(List<ConfigMockExamDTO> lstReading) {
		this.lstReading = lstReading;
	}

	public List<ConfigMockExamDTO> getLstListening() {
		return lstListening;
	}

	public void setLstListening(List<ConfigMockExamDTO> lstListening) {
		this.lstListening = lstListening;
	}
    
    
}
