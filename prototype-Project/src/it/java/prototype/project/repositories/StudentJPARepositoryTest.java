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

class StudentJPARepositoryTest {

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
	void test_findAll_when_empty() {
		List<Student> students = repository.findAll();
		assertThat(students).isEmpty();
	}
	
	@Test
	void test_findAll_when_not_empty() {
		Student student = new Student(null, "Mario", new HashSet<Registration>());
		Course course = new Course(null, "Corso inutile", new HashSet<Registration>()); 
		Course course2 = new Course(null, "Corso quasi inutile", new HashSet<Registration>());
		Registration registration = new Registration(student, course, false);
		Registration registration2 = new Registration(student, course2, false);
//		Registration registration = new Registration(new RegistrationId(student.getId(), course.getId()),student, course, false);
		Set<Registration> registrations = new HashSet<Registration>();
		registrations.add(registration);
		student.setRegistrations(registrations);
//		course.setRegistrations(registrations);

		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.persist(course);
		entityManager.persist(registration);
		entityManager.persist(registration2);
		entityManager.getTransaction().commit();
		
		List<Student> students = repository.findAll();
		
		assertThat(students).containsExactly(student);
//		System.out.println(students);
		System.out.println(entityManager.createQuery("from Registration",Registration.class).getResultList());
		
//		System.out.println(
//				entityManager.createQuery(
//				"select distinct s from Student s join s.registrations"
//				,Student.class).getResultList());
		
		
//		System.out.println(students.get(0));
//		System.out.println(entityManager.createQuery("",Student.class).getResultList());
		
		
	}

}
