package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.Course;
import prototype.project.model.GenericEntity;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.model.id.RegistrationId;

public class RegistrationJPARepository implements GenericEntityRepository<Registration,RegistrationId>{

	private EntityManager entityManager;

	public RegistrationJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	public List<Registration> findAll() {
		return entityManager.createQuery("from Registration",Registration.class).getResultList();
	}

	@Transactional
	public Registration findById(RegistrationId id) {
		return entityManager.find(Registration.class, id);
	}

	public Registration save (Registration registration){
		if (registration.getStudent().getRegistrations().add(registration) &&
				registration.getCourse().getRegistrations().add(registration))
			return registration;
		return null;
	}

	public Registration delete (Registration registration) {
		if (registration.getStudent().getRegistrations().remove(registration) &&
				registration.getCourse().getRegistrations().remove(registration))
			return registration;
		return null;
	}

}
