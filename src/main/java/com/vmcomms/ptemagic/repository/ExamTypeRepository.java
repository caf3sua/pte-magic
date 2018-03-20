package com.vmcomms.ptemagic.repository;

import com.vmcomms.ptemagic.domain.ExamType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExamType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {
	List<ExamType> findAllByType(TestType type);
}
