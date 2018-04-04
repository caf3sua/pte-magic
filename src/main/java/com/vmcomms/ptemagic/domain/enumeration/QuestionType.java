package com.vmcomms.ptemagic.domain.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * The QuestionType enumeration.
 */
public enum QuestionType {
	// SINGLE_CHOICE, MULTI_CHOICE, LISTENING, READING
    
    /** 1 - SPEAKING : cham diem **/
    SPEAKING_READ_ALOUD
    , SPEAKING_REPEAT_SENTENCE
    , SPEAKING_DESCRIBE_IMAGE
    , SPEAKING_RETELL_LECTURE
    , SPEAKING_ANSWER_SHORT_QUESTION
    /** 2 - WRITING : cham diem **/
    , WRITING_SUMMARIZE_WRITTEN_TEXT
    , WRITING_ESSAY
    /** 3 - READING **/
    , READING_FIB_R_W
    , READING_FIB_R
    , READING_RE_ORDER_PARAGRAPH
    , READING_MCQ_R_SINGLE_ANSWER
    , READING_MCQ_R_MULTIPLE_ANSWER
    /** 4 - LISTENING **/
    , LISTENING_SUMMARIZE_SPOKEN_TEXT
    , LISTENING_FIB_L
    , LISTENING_MCQ_L_SINGLE_ANSWER
    , LISTENING_MCQ_L_MULTIPLE_ANSWER
    , LISTENING_HIGHLIGHT_CORRECT_SUMMARY
    , LISTENING_SELECT_MISSING_WORD
    , LISTENING_HIGHLIGHT_INCORRECT_WORD
    , LISTENING_DICTATION
    , TIME_BREAK;
    
    public static List<QuestionType> getBySkill(String skill) {
    	List<QuestionType> lstQuestionType = new ArrayList<>();
        for (QuestionType b : QuestionType.values()) {
        	if (b.name().contains(skill)) {
        		lstQuestionType.add(b);
        	}
        }
        return lstQuestionType;
      }
}
