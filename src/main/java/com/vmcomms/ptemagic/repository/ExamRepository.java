package com.vmcomms.ptemagic.repository;

import com.vmcomms.ptemagic.domain.Exam;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.service.dto.ExamDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>, ExamRepositoryExtend {
	Page<Exam> findByResult(ProgressType result, Pageable pageable);
	
	List<Exam> findByResult(ProgressType result);
}
