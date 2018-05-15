package com.vmcomms.ptemagic.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.domain.UserLimitExam;
import com.vmcomms.ptemagic.repository.UserLimitExamRepository;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.UserLimitExamService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.UserDTO;
import com.vmcomms.ptemagic.service.mapper.UserLimitExamMapper;
import com.vmcomms.ptemagic.service.util.PteMagicConstants;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;


/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class UserLimitExamServiceImpl implements UserLimitExamService {

    private final Logger log = LoggerFactory.getLogger(UserLimitExamServiceImpl.class);

    @Autowired
    private UserLimitExamRepository userLimitExamRepository;

    @Autowired
    private UserLimitExamMapper userLimitExamMapper;

    @Autowired
    private ExamTypeService examTypeService;
    
    
    @Autowired
    private UserService userService;

	@Override
	public int getRemainTest(Long userId, Long examTypeId) {
		UserLimitExam result = userLimitExamRepository.findOneByUserIdAndExamTypeId(userId, examTypeId);
		
		if (null == result) {
			// Insert one with default
			result = insert(userId, examTypeId);
		}
		
		return result.getRemainTest();
	}
	
	private UserLimitExam insert(Long userId, Long examTypeId) {
		// Insert one with default
		UserLimitExam e = new UserLimitExam();
		e.setExamTypeId(examTypeId);
		e.setUserId(userId);
		e.setRemainTest(getRemainTestByRole(userId, examTypeId));
		return userLimitExamRepository.save(e);
	}
	
	private int getRemainTestByRole(Long userId, Long examTypeId) {
		Authentication authetication = SecurityContextHolder.getContext().getAuthentication();
		
		// Get examType object to find user limit
		ExamTypeDTO examTypeDTO = examTypeService.findOne(examTypeId);
		
		SimpleGrantedAuthority silverRole = new SimpleGrantedAuthority("ROLE_SILVER");
		SimpleGrantedAuthority goldRole = new SimpleGrantedAuthority("ROLE_GOLD");
		SimpleGrantedAuthority platinumRole = new SimpleGrantedAuthority("ROLE_PLATINUM");
    	
    	if (authetication.getAuthorities().contains(silverRole)) {
    		return examTypeDTO.getLimitTestSilver();
    	} else if (authetication.getAuthorities().contains(goldRole)) {
    		return examTypeDTO.getLimitTestGold();
		} else if (authetication.getAuthorities().contains(platinumRole)) {
			return examTypeDTO.getLimitTestPlatinum();
		} else {
			return PteMagicConstants.MOCK_TEST_REMAINING_DEFAULT;
		}
	}


	@Override
	public boolean updateCountRemainTest(Long userId, Long examTypeId) {
		UserLimitExam result = userLimitExamRepository.findOneByUserIdAndExamTypeId(userId, examTypeId);
		
		if (null == result) {
			// Insert one with default
			return false;
		}
		
		if (result.getRemainTest() > 0) {
			result.setRemainTest(result.getRemainTest() - 1);
		}
		
		// Update 
		userLimitExamRepository.save(result);
		
		return true;
	}

	@Override
	public boolean resetRemainTest(Long userId) {
		// Delete all by userId
		userLimitExamRepository.deleteByUserId(userId);
		
		// Insert by all examType : MOCK_TEST_A, MOCK_TEST_B, MOCK_TEST_FULL
		List<ExamTypeDTO> mockA = examTypeService.findAllByType("MOCK_TEST_A");
		for (ExamTypeDTO examTypeDTO : mockA) {
			insert(userId, examTypeDTO.getId());
		}
		
		List<ExamTypeDTO> mockB = examTypeService.findAllByType("MOCK_TEST_B");
		for (ExamTypeDTO examTypeDTO : mockB) {
			insert(userId, examTypeDTO.getId());
		}
		
		List<ExamTypeDTO> mockFull = examTypeService.findAllByType("MOCK_TEST_FULL");
		for (ExamTypeDTO examTypeDTO : mockFull) {
			insert(userId, examTypeDTO.getId());
		}
		
		return false;
	}
}
