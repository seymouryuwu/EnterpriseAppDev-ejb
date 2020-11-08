package repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import repository.entity.Customer;
import repository.entity.IndustryType;

@Stateless
public class IndustryTypeRepositoryImpl implements IndustryTypeRepository {
	@PersistenceContext(unitName = "Assignment-ejbPU")
    private EntityManager entityManager;
	
	@Override
	public List<IndustryType> findAllInsdustryTypes() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery(IndustryType.class);
		Root<IndustryType> i = query.from(IndustryType.class);
		query.select(i).orderBy(builder.desc(i.get("industryTypeId")));
		List<IndustryType> li = entityManager.createQuery(query).getResultList();
		return li;
	}

	@Override
	public void addIndustryType(IndustryType industryType) {
		List<IndustryType> industryTypes = findAllInsdustryTypes(); 
		if (industryTypes.isEmpty()) {
			industryType.setIndustryTypeId(1);
		} else {
			industryType.setIndustryTypeId(industryTypes.get(0).getIndustryTypeId() + 1);
		}
        
        
		entityManager.persist(industryType);
		
	}

	@Override
	public void removeIndustryType(String industryTypeName) {
		IndustryType industryType = this.findIndustryTypeByName(industryTypeName);
		if (industryType != null) {
			entityManager.remove(industryType);
		}
	}

	@Override
	public void updateIndustryType(IndustryType industryType) {
		try {
			entityManager.merge(industryType);
		} catch (Exception ex) {
			
		}
		
	}

	@Override
	public IndustryType findIndustryTypeByName(String industryTypeName) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery(IndustryType.class);
		Root<IndustryType> i = query.from(IndustryType.class);
		query.select(i).where(builder.equal(i.get("industryType").as(String.class), industryTypeName));
		IndustryType industryType = (IndustryType) entityManager.createQuery(query).getSingleResult();
		return industryType;
	}

	@Override
	public IndustryType findIndustryTypeById(int industryTypeId) {
		IndustryType industryType = entityManager.find(IndustryType.class, industryTypeId);
		return industryType;
		
	}

}
