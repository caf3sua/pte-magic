package com.vmcomms.ptemagic.dto;

import java.util.List;

import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;


public class ExamInfoDTO {

	private ExamDTO examDTO;
	
	private ExamTypeDTO examTypeDTO;

	private List<QuestionDTO> questions;
	
	private List<AnswerDTO> answers;
	
	private List<AnswerQuestionDTO> answerQuestions;
	
	private int numberQuestionReading;
	
	private int numberQuestionWriting;
	
	private int numberQuestionListening;
	
	private int numberQuestionSpeaking;

    public ExamDTO getExamDTO() {
        return examDTO;
    }

    public void setExamDTO(ExamDTO examDTO) {
        this.examDTO = examDTO;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

	public ExamTypeDTO getExamTypeDTO() {
		return examTypeDTO;
	}

	public void setExamTypeDTO(ExamTypeDTO examTypeDTO) {
		this.examTypeDTO = examTypeDTO;
	}

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}

	public List<AnswerQuestionDTO> getAnswerQuestions() {
		return answerQuestions;
	}

	public void setAnswerQuestions(List<AnswerQuestionDTO> answerQuestions) {
		this.answerQuestions = answerQuestions;
	}

	public int getNumberQuestionReading() {
		return numberQuestionReading;
	}

	public void setNumberQuestionReading(int numberQuestionReading) {
		this.numberQuestionReading = numberQuestionReading;
	}

	public int getNumberQuestionWriting() {
		return numberQuestionWriting;
	}

	public void setNumberQuestionWriting(int numberQuestionWriting) {
		this.numberQuestionWriting = numberQuestionWriting;
	}

	public int getNumberQuestionListening() {
		return numberQuestionListening;
	}

	public void setNumberQuestionListening(int numberQuestionListening) {
		this.numberQuestionListening = numberQuestionListening;
	}

	public int getNumberQuestionSpeaking() {
		return numberQuestionSpeaking;
	}

	public void setNumberQuestionSpeaking(int numberQuestionSpeaking) {
		this.numberQuestionSpeaking = numberQuestionSpeaking;
	}
}
