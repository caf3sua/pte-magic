package com.vmcomms.ptemagic.repository;

import com.vmcomms.ptemagic.domain.Answer;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

	Answer findOneByExamIdAndQuestionId(Long examId, Long questionId);
	
	List<Answer> findByExamId(Long examId);
}
