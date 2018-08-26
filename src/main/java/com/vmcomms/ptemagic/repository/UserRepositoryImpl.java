package com.vmcomms.ptemagic.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.vmcomms.ptemagic.dto.QueryExamDTO;


/**
 * Spring Data JPA repository for the BrandkeyProduct entity.
 */
@SuppressWarnings("unused")
public class UserRepositoryImpl implements UserRepositoryExtend {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	@Transactional
	@Modifying
	public void deletePteUser(Long userId) {
		List<QueryExamDTO> result = new ArrayList<>();
		
		String expressionDeleteAuthoriy = "delete from jhi_user_authority " + 
				"where user_id = :pUserId";
		
		String expressionDeleteUser = "delete from jhi_user " + 
				"where id = :pUserId";
		
		Query queryDelAuth = entityManager.createNativeQuery(expressionDeleteAuthoriy);
		Query queryDelUser = entityManager.createNativeQuery(expressionDeleteUser);
		
		// Set query parameter
		queryDelAuth.setParameter("pUserId", userId);
		queryDelUser.setParameter("pUserId", userId);
		
		queryDelAuth.executeUpdate();
		queryDelUser.executeUpdate();
	}
	
	
}
