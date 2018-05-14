package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.ConfigMockExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.domain.ExamType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.repository.ExamTypeRepository;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.MockExamDTO;
import com.vmcomms.ptemagic.service.mapper.ExamTypeMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    private ExamTypeMapper examTypeMapper;
    
    @Autowired
    private ConfigMockExamService configMockExamService;

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

	@Override
	public MockExamDTO saveMockTest(MockExamDTO mockExamDTO) {
		// calculate total question
		int totalQuestion = mockExamDTO.getLstSpeaking().size() + mockExamDTO.getLstWriting().size() 
				+ mockExamDTO.getLstReading().size() + mockExamDTO.getLstListening().size();
		mockExamDTO.getExamTypeDTO().setTotalQuestion(totalQuestion);
		
		// Save ExamTypeDTO
		ExamTypeDTO examTypeDTO = this.save(mockExamDTO.getExamTypeDTO());
		
		/** 1 - SPEAKING : cham diem 1 - 5 **/
	    /** 2 - WRITING : cham diem 6 - 7 **/
	    /** 3 - READING 8 - 12 **/
	    /** 4 - LISTENING 13 - 20 **/
		// Save ConfigMockExam SPEAKING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstSpeaking()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Save ConfigMockExam WRITING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstWriting()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		
		// Save ConfigMockExam READING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstReading()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Save ConfigMockExam LISTENING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstListening()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Update data
		mockExamDTO.setExamTypeDTO(examTypeDTO);
		
		return mockExamDTO;
	}
}
