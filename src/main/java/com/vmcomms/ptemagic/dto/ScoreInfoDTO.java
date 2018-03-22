package com.vmcomms.ptemagic.dto;

import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.service.dto.UserDTO;


public class ScoreInfoDTO {

	private UserDTO user;
	
	/** Free sample */
	private int totalQuestion;
	private int score;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public int getTotalQuestion() {
		return totalQuestion;
	}

	public void setTotalQuestion(int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	
}
