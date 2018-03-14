package com.vmcomms.ptemagic.repository;

import com.vmcomms.ptemagic.domain.Answer;
import com.vmcomms.ptemagic.domain.ExamQuestion;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

}
