package com.vmcomms.ptemagic.repository;

import java.util.List;

import javax.persistence.Column;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vmcomms.ptemagic.domain.Question;
import com.vmcomms.ptemagic.domain.UserLimitExam;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLimitExamRepository extends JpaRepository<UserLimitExam, Long> {
	UserLimitExam findOneByUserIdAndExamTypeId(Long userId, Long examTypeId);
}
