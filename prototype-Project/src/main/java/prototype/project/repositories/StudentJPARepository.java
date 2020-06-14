package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

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
	public Student findOne(String code) {
		try {
			return entityManager.createQuery("SELECT s FROM Student s WHERE s.code LIKE :sCode", Student.class)
					.setParameter("sCode", code).getSingleResult();
		} catch (NoResultException e) {
//			LOGGER.error("Student code to delete not found",e);
			LOGGER.info("Student code to delete not found");
			return null;
		}
	}

	@Override
	public boolean save(Student student) {
		try {
			entityManager.persist(student);
			return true;
		} catch (PersistenceException e) {
//			LOGGER.error("Error while saving Student",e);
			LOGGER.error("Error while saving Student");
			return false;
		}
	}

	@Override
	public Student delete(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
