package com.vmcomms.ptemagic.repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import com.vmcomms.ptemagic.domain.ExamQuestion;


/**
 * Spring Data JPA repository for the BrandkeyProduct entity.
 */
@SuppressWarnings("unused")
public class ExamQuestionRepositoryImpl implements ExamQuestionRepositoryExtend {

	@PersistenceContext
    private EntityManager entityManager;
	
	private JpaEntityInformation<ExamQuestion, ?> entityInformation;
	
	@PostConstruct
    public void postConstruct() {
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(ExamQuestion.class, entityManager);
    }

//	@Override
//	public Page<FeedItem> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable) {
//		Query queryTotal = entityManager.createNativeQuery
//			    ("select count(*) from feed_item "
//				+ " where id in (select item_id from status_item where user_id = :user_id and status = :status)");
//		queryTotal.setParameter("user_id", userId);
//		queryTotal.setParameter("status", status);
//		BigInteger countResultBig = (BigInteger) queryTotal.getSingleResult();
//		long countResult = countResultBig.longValue();
//			
//		Query query = entityManager.createNativeQuery("select * from feed_item "
//				+ " where id in (select item_id from status_item where user_id = :user_id and status = :status) "
//				+ " order by id desc", FeedItem.class);
//		query.setParameter("user_id", userId);
//		query.setParameter("status", status);
//		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()); 
//		query.setMaxResults(pageable.getPageSize());
//		
//        List<FeedItem> data = query.getResultList();
//        
//        Page<FeedItem> page = new PageImpl<>(data, pageable, countResult);
//        return page;
//	}

}
