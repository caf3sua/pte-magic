package com.vmcomms.ptemagic.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import com.vmcomms.ptemagic.domain.Exam;
import com.vmcomms.ptemagic.domain.ExamQuestion;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.dto.QueryExamDTO;


/**
 * Spring Data JPA repository for the BrandkeyProduct entity.
 */
@SuppressWarnings("unused")
public class ExamRepositoryImpl implements ExamRepositoryExtend {

	@PersistenceContext
    private EntityManager entityManager;
	
	private JpaEntityInformation<ExamQuestion, ?> entityInformation;
	
	@PostConstruct
    public void postConstruct() {
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(ExamQuestion.class, entityManager);
    }

	@Override
	public List<QueryExamDTO> findByResultCustom(ProgressType type) {
		List<QueryExamDTO> result = new ArrayList<>();
		
		String expression = "select ex.id, ex.user_id, ex.result, ex.exam_type_id, ju.email, et.name from exam ex " + 
				"inner join jhi_user ju on ju.id = ex.user_id " + 
				"inner join exam_type et on et.id = ex.exam_type_id " + 
				"where ex.result = :pResult";
		
		Query query = entityManager.createNativeQuery(expression);
		
		// Set query parameter
		query.setParameter("pResult", type.toString());
		
		List<Object[]> data = query.getResultList();
		
		convertToQueryExamDTO(result, data);
					
		return result;
	}
	
	private void convertToQueryExamDTO(List<QueryExamDTO> result, List<Object[]> data) {
		for (Object[] objects : data) {
			QueryExamDTO item = new QueryExamDTO();
			item.setId(((BigInteger)objects[0]).longValue());
			item.setUserId(((BigInteger)objects[1]).longValue());
		    item.setResult((String)objects[2]);
		    item.setExamTypeId(((BigInteger)objects[3]).longValue());
		    item.setEmail((String)objects[4]);
		    item.setExamTypeName((String)objects[4]);
		    
			result.add(item);
		}
	}
}
