package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.model.id.RegistrationId;

public class RegistrationJPARepository implements RegistrationRepository{

	private EntityManager entityManager;

	public RegistrationJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	@Transactional
	public List<Registration> findAll() {
		return entityManager.createQuery("from Registration",Registration.class).getResultList();
	}

	@Override
	@Transactional
	public Registration findById(long studentId, long courseId) {
		return entityManager.find(Registration.class, new RegistrationId(studentId, courseId));
	}

//	@Override
//	public Registration save(Registration registration){
//		return entityManager.merge(registration);
//	}
//
//	@Override
//	public Registration delete(long studentId, long courseId) {
//		Registration registration = entityManager.find(Registration.class, new RegistrationId(studentId, courseId));
//		if (registration != null) {
//			entityManager.remove(registration);
//		}
//		return registration;
//		
//	}

}
