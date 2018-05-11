package com.vmcomms.ptemagic.service;

/**
 * Service Interface for managing Question.
 */
public interface UserLimitExamService {

	public int getRemainTest(Long userId, Long examTypeId);
	
	public boolean updateCountRemainTest(Long userId, Long examTypeId);
}
