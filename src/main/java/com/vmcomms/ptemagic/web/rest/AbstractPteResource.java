package com.vmcomms.ptemagic.web.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.repository.UserRepository;
import com.vmcomms.ptemagic.security.SecurityUtils;
import com.vmcomms.ptemagic.web.rest.errors.InternalServerErrorException;

public class AbstractPteResource {

    private final Logger log = LoggerFactory.getLogger(AbstractPteResource.class);

    @Autowired
    private UserRepository userRepository;

    protected Long getCurrentUserId() {
    	final String userLogin = SecurityUtils.getCurrentUserLogin();
    	Optional<User> user = userRepository.findOneByLogin(userLogin);
    	if (!user.isPresent()) {
    		throw new InternalServerErrorException("User could not be found");
    	}
      
    	Long userId = user.get().getId();
    	return userId;
    }
}
