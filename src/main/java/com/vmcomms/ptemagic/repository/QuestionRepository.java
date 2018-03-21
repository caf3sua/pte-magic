package com.vmcomms.ptemagic.repository;

import com.vmcomms.ptemagic.domain.Question;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByTypeIn(List<QuestionType> types);
}
