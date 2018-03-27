package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.domain.Exam;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.repository.ExamRepository;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.mapper.ExamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Exam.
 */
@Service
@Transactional
public class ExamServiceImpl implements ExamService{

    private final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    public ExamServiceImpl(ExamRepository examRepository, ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
    }

    /**
     * Save a exam.
     *
     * @param examDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExamDTO save(ExamDTO examDTO) {
        log.debug("Request to save Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    /**
     *  Get all the exams.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable)
            .map(examMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAllByResult(Pageable pageable, ProgressType result) {
        log.debug("Request to get all Exams by result");
        return examRepository.findByResult(result, pageable)
            .map(examMapper::toDto);
    }

    /**
     *  Get one exam by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExamDTO findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        Exam exam = examRepository.findOne(id);
        return examMapper.toDto(exam);
    }

    /**
     *  Delete the  exam by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.delete(id);
    }
}
