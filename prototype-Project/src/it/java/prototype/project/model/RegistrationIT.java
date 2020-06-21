package prototype.project.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationIT {

	private static LinkedList<Student> students = new LinkedList<>();
	private static LinkedList<Course> courses = new LinkedList<>();

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
	public void test_many_to_many_mapping() {
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
	}

}
