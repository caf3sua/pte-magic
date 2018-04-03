package com.vmcomms.ptemagic.dto;

import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.dto.UserDTO;


public class AnswerQuestionDTO {

	private AnswerDTO answer;
	private QuestionDTO question;
	public AnswerDTO getAnswer() {
		return answer;
	}
	public void setAnswer(AnswerDTO answer) {
		this.answer = answer;
	}
	public QuestionDTO getQuestion() {
		return question;
	}
	public void setQuestion(QuestionDTO question) {
		this.question = question;
	}
	
}
