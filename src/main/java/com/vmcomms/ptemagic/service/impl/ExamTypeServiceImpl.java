package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.domain.ExamType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.repository.ExamTypeRepository;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.mapper.ExamTypeMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ExamType.
 */
@Service
@Transactional
public class ExamTypeServiceImpl implements ExamTypeService{

    private final Logger log = LoggerFactory.getLogger(ExamTypeServiceImpl.class);

    private final ExamTypeRepository examTypeRepository;

    private final ExamTypeMapper examTypeMapper;

    public ExamTypeServiceImpl(ExamTypeRepository examTypeRepository, ExamTypeMapper examTypeMapper) {
        this.examTypeRepository = examTypeRepository;
        this.examTypeMapper = examTypeMapper;
    }

    /**
     * Save a examType.
     *
     * @param examTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExamTypeDTO save(ExamTypeDTO examTypeDTO) {
        log.debug("Request to save ExamType : {}", examTypeDTO);
        ExamType examType = examTypeMapper.toEntity(examTypeDTO);
        examType = examTypeRepository.save(examType);
        return examTypeMapper.toDto(examType);
    }

    /**
     *  Get all the examTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamTypes");
        return examTypeRepository.findAll(pageable)
            .map(examTypeMapper::toDto);
    }

    /**
     *  Get one examType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExamTypeDTO findOne(Long id) {
        log.debug("Request to get ExamType : {}", id);
        ExamType examType = examTypeRepository.findOne(id);
        return examTypeMapper.toDto(examType);
    }

    /**
     *  Delete the  examType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamType : {}", id);
        examTypeRepository.delete(id);
    }
    
    public List<ExamTypeDTO> findAllByType(String type) {
    	log.debug("Request to findAllByType : {}", type);
        List<ExamType> data = examTypeRepository.findAllByType(TestType.valueOf(type));
        return examTypeMapper.toDto(data);
    }
}
