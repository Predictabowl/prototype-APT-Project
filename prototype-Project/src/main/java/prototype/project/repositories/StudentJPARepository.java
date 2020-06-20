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

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Student;

public class StudentJPARepository implements StudentRepository {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private EntityManager entityManager;

	public StudentJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Student> findAll() {
		return entityManager.createQuery("from Student", Student.class).getResultList();
	}

	@Override
	public Student findByCode(String code) {
		try {
//			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//			CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
//			Root<Student> studentRoot = criteriaQuery.from(Student.class);
//			criteriaQuery.where(criteriaBuilder.equal(studentRoot.get("code"), code));
//			return entityManager.createQuery(criteriaQuery).getSingleResult();
			
			Session session = entityManager.unwrap(Session.class);
			return session.byNaturalId(Student.class).using("code", code).getReference();
			
//			return entityManager.createQuery("SELECT s FROM Student s WHERE s.code LIKE :sCode", Student.class)
//					.setParameter("sCode", code).getSingleResult();
		} catch (NoResultException e) {
//			LOGGER.error("Student not found",e);
			LOGGER.info("Student not found with code: "+code);
		}
		return null;
	}

	@Override
	public Student save(Student student) throws SchoolDatabaseException{
		try {
			return entityManager.merge(student);
		} catch (PersistenceException e) {
			throw new SchoolDatabaseException("Error while saving Student: "+student,e);
		}
	}

	@Override
	public Student delete(long id) {
		Student student = entityManager.find(Student.class, id);
		if (student != null)
			entityManager.remove(student);
		return student;
	}

	@Override
	public Student findById(long id) {
		return entityManager.find(Student.class, id);
	}

}
