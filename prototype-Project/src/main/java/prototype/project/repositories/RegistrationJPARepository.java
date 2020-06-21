package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Registration;

public class RegistrationJPARepository implements RegistrationRepository{

	private EntityManager entityManager;

	public RegistrationJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Registration> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Registration findById(long courseId, long studentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Registration save(Registration registration) throws SchoolDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Registration delete(long courseId, long studentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
