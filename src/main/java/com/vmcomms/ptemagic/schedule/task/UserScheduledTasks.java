package com.vmcomms.ptemagic.schedule.task;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.repository.UserRepository;


@Component
public class UserScheduledTasks {
	private final Logger log = LoggerFactory.getLogger(UserScheduledTasks.class);
	
	@Autowired
	private UserRepository userRepository;
	
	// Every 1 minute /  cron <second> <minute> <hour> <day-of-month> <month> <day-of-week>
//	@Scheduled(fixedRate = 1*60*1000)
	@Scheduled(cron = "0 1 1 ? * *") // Fire at 1:1 AM every day
	@Transactional
    public void updateUserRemainingDays() {
    	log.debug("Update user remain days");
    	
    	// Get all user 
    	List<User> users =userRepository.findAll();
    	
    	// remain day - 1
    	for (User user : users) {
    		Integer remainday = user.getRemainDays();
			if (remainday != null && remainday > 0) {
				user.setRemainDays(remainday - 1);
			}
		}
    	
    	// Save user
    	userRepository.save(users);
    }
}
