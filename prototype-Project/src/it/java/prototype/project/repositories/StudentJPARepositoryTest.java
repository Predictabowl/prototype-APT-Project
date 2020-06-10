package prototype.project.repositories;


import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Student;

class StudentJPARepositoryTest {

	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	
	private StudentJPARepository repository;

	
	@BeforeEach
	public void setUp() {
		Map<String, String> properties = new HashMap<>();
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project",properties);
		entityManager = eManagerFactory.createEntityManager();
		repository = new StudentJPARepository(entityManager);
		
		entityManager.createQuery("from Student",Student.class).getResultStream()
			.forEach(e -> entityManager.remove(e));
	}
	
	@AfterEach
	public void tearDown() {
		entityManager.close();
		eManagerFactory.close();
	}
	
	
	@Test
	void test_findAll_when_empty() {
		List<Student> students = repository.findAll();
		assertThat(students).isEmpty();
	}

}
