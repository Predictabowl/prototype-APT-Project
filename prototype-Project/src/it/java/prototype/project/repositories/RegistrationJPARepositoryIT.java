package prototype.project.repositories;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Registration;
import prototype.project.test.utils.GenericJPAEntitySetup;

class RegistrationJPARepositoryIT extends GenericJPAEntitySetup{
	
	private RegistrationJPARepository repository;

	@BeforeEach
	public void setUp() {
		setUpEntity(Registration.class);
	
		
		repository = new RegistrationJPARepository(entityManager);
	}

	@Test
	void test_findAll() {
//		Registration registration = new Registration(student, course, paid)
		fail("automatic fail");
	}
	
	private void populateDB() {
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}

}
