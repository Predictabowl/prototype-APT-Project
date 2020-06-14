package prototype.project.repositories;


import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.model.id.RegistrationId;

class StudentJPARepositoryIT {

	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	
	private StudentJPARepository repository;

	
	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
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
	public void test_findAll_when_empty() {
		List<Student> students = repository.findAll();
		assertThat(students).isEmpty();
	}
	
	@Test
	public void test_findAll_when_not_empty() {
		Student student = new Student("A1", "Mario");
		addStudentToDB(student);
		
		List<Student> students = repository.findAll();
		
		assertThat(students).containsExactly(student);
	}
	
	@Test
	public void test_findOne_successful() {
		Student student = new Student("St4","testName");
		Student student2 = new Student("St1","Another Name");
		addStudentToDB(student);
		addStudentToDB(student2);
		
		Student found = repository.findOne("St1");
		
		assertThat(found).isSameAs(student2);
		
	}
	
	@Test
	public void test_findOne_when_not_present() {
		Student notFound = repository.findOne("code1");
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_save_successful() {
		Student student = new Student("AR1", "Carlo");
		
		repository.save(student);		
		
		Student saved = entityManager.createQuery("from Student",Student.class).getSingleResult();
		assertThat(saved).isSameAs(student);
	}
	
	private void addStudentToDB(Student student) {
		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.getTransaction().commit();
	}

}
