package com.vmcomms.ptemagic.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.dto.QueryExamDTO;


/**
 * Spring Data JPA repository for the FeedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepositoryExtend {
	List<QueryExamDTO> findByResultCustom(ProgressType result);
}
