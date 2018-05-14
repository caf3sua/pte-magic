package com.vmcomms.ptemagic.service;

import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.MockExamDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ExamType.
 */
public interface ExamTypeService {

	MockExamDTO saveMockTest(MockExamDTO mockExamDTO);
	
    /**
     * Save a examType.
     *
     * @param examTypeDTO the entity to save
     * @return the persisted entity
     */
    ExamTypeDTO save(ExamTypeDTO examTypeDTO);

    /**
     *  Get all the examTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExamTypeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" examType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExamTypeDTO findOne(Long id);

    /**
     *  Delete the "id" examType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    List<ExamTypeDTO> findAllByType(String type);
}
