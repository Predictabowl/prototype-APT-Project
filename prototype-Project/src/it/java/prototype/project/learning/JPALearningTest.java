package prototype.project.learning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;

public class JPALearningTest {

	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	
	@BeforeEach
	public void setUp() {
		Map<String, String> properties = new HashMap<>();
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project",properties);
		entityManager = eManagerFactory.createEntityManager();
	}
	
	@AfterEach
	public void tearDown() {
		entityManager.close();
		eManagerFactory.close();
	}
	
	@Test
	public void test_ManytoMany_with_Attribute() {
		Student student = new Student("A1","Mario");
		Course course = new Course("C1","Corso inutile"); 
		Course course2 = new Course("C2","Corso quasi inutile");
		Registration registration = new Registration(student, course, false);
		Registration registration2 = new Registration(student, course2, false);

		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.persist(course);
		entityManager.persist(course2);
		entityManager.persist(registration);
		entityManager.persist(registration2);
		entityManager.getTransaction().commit();		

		student.getRegistrations().add(registration);
		student.getRegistrations().add(registration2);
		course.getRegistrations().add(registration);
		course2.getRegistrations().add(registration2);
		
		List<Student> students = entityManager.createQuery("from Student",Student.class).getResultList();
		List<Course> courses = entityManager.createQuery("from Course",Course.class).getResultList();
		List<Registration> registrations = entityManager.createQuery("from Registration",Registration.class).getResultList();
		
		assertThat(students).containsExactly(student);
		assertThat(students.get(0)).isSameAs(student);
		assertThat(students.get(0)).isEqualTo(new Student("A1", "Mario"));
		assertThat(students.get(0).getRegistrations()).containsExactly(registration,registration2);
		
		assertThat(courses).containsExactly(course,course2);
		assertThat(courses.get(0).getRegistrations()).containsExactly(registration);
		assertThat(courses.get(1).getRegistrations()).containsExactly(registration2);
		
		assertThat(registrations).containsExactly(registration,registration2);
		
		
		// Delete tests
		registration2.getStudent().getRegistrations().remove(registration2);
		registration2.getCourse().getRegistrations().remove(registration2);
		entityManager.getTransaction().begin();
		entityManager.remove(registration2);
		entityManager.getTransaction().commit();

		
		students = entityManager.createQuery("from Student",Student.class).getResultList();
		courses = entityManager.createQuery("from Course",Course.class).getResultList();
		registrations = entityManager.createQuery("from Registration",Registration.class).getResultList();
		
		assertThat(students.get(0).getRegistrations()).containsExactly(registration);
		assertThat(courses.get(1).getRegistrations()).isEmpty();
		assertThat(registrations).containsExactly(registration);
	}
	
	@Test
	public void test_bidirectional_Mapping() {
		
		
	}
}
