package prototype.project.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JoinRegistrationIT {

	private LinkedList<Student> students = new LinkedList<>();
	private LinkedList<Course> courses = new LinkedList<>();

	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;

	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.createQuery("from Student", Student.class).getResultStream()
			.forEach(e -> entityManager.remove(e));
		entityManager.createQuery("from Course", Course.class).getResultStream()
			.forEach(e -> entityManager.remove(e));
		entityManager.createQuery("from Registration", Registration.class).getResultStream()
			.forEach(e -> entityManager.remove(e));
		entityManager.getTransaction().commit();
		
		populateDB();
	}

	private void populateDB() {
		students.add(new Student("ST1", "Student 1"));
		students.add(new Student("ST2", "Student 2"));
		students.add(new Student("ST3", "Student 3"));
		courses.add(new Course("CS1", "Course 1"));
		courses.add(new Course("CS2", "Course 2"));
		courses.add(new Course("CS3", "Course 3"));

		entityManager.getTransaction().begin();
		students.stream().forEach(s -> entityManager.persist(s));
		courses.stream().forEach(c -> entityManager.persist(c));
		entityManager.getTransaction().commit();
	}

	@Test
	public void test_many_to_many_mapping_and_cascade() {
		Registration registration = new Registration(students.get(0), courses.get(0), false);
		Registration registration2 = new Registration(students.get(1), courses.get(0), true);
		Registration registration3 = new Registration(students.get(2), courses.get(2), true);

		entityManager.getTransaction().begin();
		students.get(0).getRegistrations().add(registration);
		students.get(1).getRegistrations().add(registration2);
		students.get(2).getRegistrations().add(registration3);
		courses.get(0).getRegistrations().add(registration);
		courses.get(0).getRegistrations().add(registration2);
		courses.get(2).getRegistrations().add(registration3);
		entityManager.getTransaction().commit();

		assertThat(entityManager.createQuery("from Registration", Registration.class).getResultList())
			.containsOnly(registration,registration2,registration3);
		
		entityManager.getTransaction().begin();
		registration.getStudent().getRegistrations().remove(registration);
		registration.getCourse().getRegistrations().remove(registration);
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration", Registration.class).getResultList())
			.containsOnly(registration2,registration3);
	}
	
	@Test
	public void test_modify_Registration_should_cascade_to_entities() {
		Registration registration = new Registration(students.get(0), courses.get(0), false);
		entityManager.getTransaction().begin();
		registration.getCourse().getRegistrations().add(registration);
		registration.getStudent().getRegistrations().add(registration);
		entityManager.getTransaction().commit();
		
		registration.setPaid(true);
		
		Student student = entityManager.find(Student.class, registration.getStudent().getId());
		Course course = entityManager.find(Course.class, registration.getCourse().getId());
		
		assertThat(student.getRegistrations().iterator().next().isPaid()).isTrue();
		assertThat(course.getRegistrations().iterator().next().isPaid()).isTrue();
	}
	
	@Test
	public void test_modify_Registration_from_one_entity_should_cascade_to_the_other() {
		Registration registration = new Registration(students.get(0), courses.get(0), false);
		entityManager.getTransaction().begin();
		registration.getCourse().getRegistrations().add(registration);
		registration.getStudent().getRegistrations().add(registration);
		entityManager.getTransaction().commit();
		
		students.get(0).getRegistrations().iterator().next().setPaid(true);
		Course course = entityManager.find(Course.class, courses.get(0).getId());
		
		assertThat(course.getRegistrations().iterator().next().isPaid()).isTrue();
	}
		
	@Test
	public void learning_test_bidirectional_mapping() {
		Student student = entityManager.createQuery("From Student",Student.class).getResultList().iterator().next();
		Course course= entityManager.createQuery("From Course",Course.class).getResultList().iterator().next();
		
		Registration registration = new Registration(student, course, false);
		entityManager.getTransaction().begin();
		entityManager.persist(registration);
		entityManager.getTransaction().commit();
		
		assertThat(student.getRegistrations().iterator().next()).isNotNull();
	}
}
