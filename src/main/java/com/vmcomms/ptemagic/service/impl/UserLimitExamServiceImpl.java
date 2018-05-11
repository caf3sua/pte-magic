package com.vmcomms.ptemagic.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vmcomms.ptemagic.domain.UserLimitExam;
import com.vmcomms.ptemagic.repository.UserLimitExamRepository;
import com.vmcomms.ptemagic.service.UserLimitExamService;
import com.vmcomms.ptemagic.service.mapper.UserLimitExamMapper;
import com.vmcomms.ptemagic.service.util.PteMagicConstants;


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


	@Override
	public int getRemainTest(Long userId, Long examTypeId) {
		UserLimitExam result = userLimitExamRepository.findOneByUserIdAndExamTypeId(userId, examTypeId);
		
		if (null == result) {
			// Insert one with default
			UserLimitExam e = new UserLimitExam();
			e.setExamTypeId(examTypeId);
			e.setUserId(userId);
			e.setRemainTest(PteMagicConstants.MOCK_TEST_REMAINING_DEFAULT);
			result = userLimitExamRepository.save(e);
		}
		
		return result.getRemainTest();
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
}
