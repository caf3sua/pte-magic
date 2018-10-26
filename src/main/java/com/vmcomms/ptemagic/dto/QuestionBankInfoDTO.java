package com.vmcomms.ptemagic.dto;

import java.util.List;

import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionBankInfoDTO {
	private QuestionCountInfoDTO speakingRA;
	private QuestionCountInfoDTO speakingRS;
	private QuestionCountInfoDTO speakingDI;
	private QuestionCountInfoDTO speakingRL;
	private QuestionCountInfoDTO speakingASQ;
	
	private QuestionCountInfoDTO writingSWT;
	private QuestionCountInfoDTO writingE;
	
	private QuestionCountInfoDTO readingFRW;
	private QuestionCountInfoDTO readingFR;
	private QuestionCountInfoDTO readingROP;
	private QuestionCountInfoDTO readingMRSA;
	private QuestionCountInfoDTO readingMRMA;
	
	private QuestionCountInfoDTO listeningSST;
	private QuestionCountInfoDTO listeningFL;
	private QuestionCountInfoDTO listeningMLSA;
	private QuestionCountInfoDTO listeningMLMA;
	private QuestionCountInfoDTO listeningHCS;
	private QuestionCountInfoDTO listeningSMW;
	private QuestionCountInfoDTO listeningHIW;
	private QuestionCountInfoDTO listeningD;
	
	private long totalQuestionReading;
	
	private long totalQuestionWriting;
	
	private long totalQuestionListening;
	
	private long totalQuestionSpeaking;

}
