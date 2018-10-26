package com.vmcomms.ptemagic.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import com.vmcomms.ptemagic.domain.ExamQuestion;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.dto.QueryExamDTO;
import com.vmcomms.ptemagic.dto.QuestionBankInfoDTO;
import com.vmcomms.ptemagic.dto.QuestionCountInfoDTO;


/**
 * Spring Data JPA repository for the BrandkeyProduct entity.
 */
@SuppressWarnings("unused")
public class QuestionRepositoryImpl implements QuestionRepositoryExtend {

	@PersistenceContext
    private EntityManager entityManager;
	
	private JpaEntityInformation<ExamQuestion, ?> entityInformation;
	
	@PostConstruct
    public void postConstruct() {
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(ExamQuestion.class, entityManager);
    }
	@Override
	public QuestionBankInfoDTO countQuestionByType() {
		QuestionBankInfoDTO result = new QuestionBankInfoDTO();
		
		String expression = "SELECT count(*), jhi_type  from question GROUP BY jhi_type"
							+ " UNION"
							+ " SELECT count(*), 'total_speaking' from question where jhi_type like 'SPEAKING_%'"
							+ " UNION"
							+ " SELECT count(*), 'total_writing' from question where jhi_type like 'WRITING_%'"
							+ " UNION"
							+ " SELECT count(*), 'total_reading' from question where jhi_type like 'READING_%'"
							+ " UNION"
							+ " SELECT count(*), 'total_listening' from question where jhi_type like 'LISTENING_%'"
							;
		
		Query query = entityManager.createNativeQuery(expression);
		
		List<Object[]> data = query.getResultList();
		
		convertToQuestionCountInfoDTO(result, data);
					
		return result;
	}
	
	private void convertToQuestionCountInfoDTO(QuestionBankInfoDTO result, List<Object[]> data) {
		for (Object[] objects : data) {
			QuestionCountInfoDTO item = new QuestionCountInfoDTO();
			String questionType = (String)objects[1];
			long count = ((BigInteger)objects[0]).longValue();
			switch (questionType) {
				// SPEAKING
				case "SPEAKING_READ_ALOUD":
					item.setName("Read aloud [RA]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("SPEAKING_READ_ALOUD"));
					result.setSpeakingRA(item);
					break;
				case "SPEAKING_REPEAT_SENTENCE":
					item.setName("Repeat scentence [RS]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("SPEAKING_REPEAT_SENTENCE"));
					result.setSpeakingRS(item);
					break;
				case "SPEAKING_DESCRIBE_IMAGE":
					item.setName("Describe image [DI]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("SPEAKING_DESCRIBE_IMAGE"));
					result.setSpeakingDI(item);
					break;
				case "SPEAKING_RETELL_LECTURE":
					item.setName("Re-tell lecture [RL]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("SPEAKING_RETELL_LECTURE"));
					result.setSpeakingRL(item);
					break;
				case "SPEAKING_ANSWER_SHORT_QUESTION":
					item.setName("Answer short question [ASQ]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("SPEAKING_ANSWER_SHORT_QUESTION"));
					result.setSpeakingASQ(item);
					break;
				// writing
				case "WRITING_SUMMARIZE_WRITTEN_TEXT":
					item.setName("Summarize witten text [SWT]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("WRITING_SUMMARIZE_WRITTEN_TEXT"));
					result.setWritingSWT(item);
					break;
				case "WRITING_ESSAY":
					item.setName("Write essay [WE]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("WRITING_ESSAY"));
					result.setWritingE(item);
					break;
				// READING
				case "READING_FIB_R_W":
					item.setName("R&W: Fill in the blanks [RWFIB]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("READING_FIB_R_W"));
					result.setReadingFRW(item);
					break;
				case "READING_FIB_R":
					item.setName("R: Fill in the blanks [RFIB]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("READING_FIB_R"));
					result.setReadingFR(item);
					break;
				case "READING_RE_ORDER_PARAGRAPH":
					item.setName("Re-order paragraphs");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("READING_RE_ORDER_PARAGRAPH"));
					result.setReadingROP(item);
					break;
				case "READING_MCQ_R_SINGLE_ANSWER":
					item.setName("MC, choose single answer");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("READING_MCQ_R_SINGLE_ANSWER"));
					result.setReadingMRSA(item);
					break;
				case "READING_MCQ_R_MULTIPLE_ANSWER":
					item.setName("MC, choose multiple answers");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("READING_MCQ_R_MULTIPLE_ANSWER"));
					result.setReadingMRMA(item);
					break;
				// LISTENING
				case "LISTENING_SUMMARIZE_SPOKEN_TEXT":
					item.setName("Summarize spoken text");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_SUMMARIZE_SPOKEN_TEXT"));
					result.setListeningSST(item);
					break;
				case "LISTENING_FIB_L":
					item.setName("Fill in the blanks [LFIB]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_FIB_L"));
					result.setListeningFL(item);
					break;
				case "LISTENING_MCQ_L_SINGLE_ANSWER":
					item.setName("MC, choose single answer");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_MCQ_L_SINGLE_ANSWER"));
					result.setListeningMLSA(item);
					break;
				case "LISTENING_MCQ_L_MULTIPLE_ANSWER":
					item.setName("MC, choose multiple answers");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_MCQ_L_MULTIPLE_ANSWER"));
					result.setListeningMLMA(item);
					break;
				case "LISTENING_HIGHLIGHT_CORRECT_SUMMARY":
					item.setName("Highlight correct summary");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_HIGHLIGHT_CORRECT_SUMMARY"));
					result.setListeningHCS(item);
					break;
				case "LISTENING_SELECT_MISSING_WORD":
					item.setName("Select missing words");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_SELECT_MISSING_WORD"));
					result.setListeningSMW(item);
					break;
				case "LISTENING_HIGHLIGHT_INCORRECT_WORD":
					item.setName("Highlight incorect words");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_HIGHLIGHT_INCORRECT_WORD"));
					result.setListeningHIW(item);
					break;
				case "LISTENING_DICTATION":
					item.setName("Write from dictation [WFD]");
					item.setTotal(count);
					item.setType(QuestionType.valueOf("LISTENING_DICTATION"));
					result.setListeningD(item);
					break;
				// total
				case "total_speaking":
					result.setTotalQuestionSpeaking(count);
					break;
				case "total_writing":
					result.setTotalQuestionWriting(count);
					break;
				case "total_reading":
					result.setTotalQuestionReading(count);
					break;
				case "total_listening":
					result.setTotalQuestionListening(count);
					break;
	
				default:
					break;
			}
		}
	}
}
