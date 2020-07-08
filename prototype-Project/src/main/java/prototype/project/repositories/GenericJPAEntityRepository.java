package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.GenericEntity;
import prototype.project.model.Student;

public class GenericJPAEntityRepository<E extends GenericEntity> implements GenericEntityRepository<E,Long> {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private EntityManager entityManager;
	private final Class<E> classType;

	public GenericJPAEntityRepository(Class<E> classType, EntityManager entityManager) {
		this.classType = classType;
		this.entityManager = entityManager;
	}

	@Override
	public List<E> findAll() {
		return entityManager.createQuery("from "+classType.getName(), classType).getResultList();
	}

	public E findByCode(String code) {
		try {
//			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//			CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
//			Root<Student> studentRoot = criteriaQuery.from(Student.class);
//			criteriaQuery.where(criteriaBuilder.equal(studentRoot.get("code"), code));
//			return entityManager.createQuery(criteriaQuery).getSingleResult();
			
			return entityManager.unwrap(Session.class)
					.byNaturalId(classType).using("code", code).getReference();
			
//			return entityManager.createQuery("SELECT s FROM Student s WHERE s.code LIKE :sCode", Student.class)
//					.setParameter("sCode", code).getSingleResult();
		} catch (NoResultException e) {
//			LOGGER.error("Student not found",e);
			LOGGER.info(classType.getName()+" not found with code: "+code);
		}
		return null;
	}

	@Override
	public E save(E entity){
		if (entity.getId() != null) {
			return entityManager.merge(entity);
		} else {
			entityManager.persist(entity);
			return entity;
		}
	}

	@Override
	public E delete(E entity) {
		E entityToDelete = entityManager.find(classType, entity.getId());
		if (entityToDelete != null)
			entityManager.remove(entityToDelete);
		return entityToDelete;
	}

	@Override
	public E findById(Long id) {
		return entityManager.find(classType, id);
	}


}
