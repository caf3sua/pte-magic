package com.vmcomms.ptemagic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmcomms.ptemagic.domain.Question;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByTypeIn(List<QuestionType> types);
	
	List<Question> findAllByType(QuestionType type);
	
	Page<Question> findByTypeIn(List<QuestionType> types, Pageable pageable);
	
	List<Question> findByIdIn(List<Long> ids);
}
