package prototype.project.repositories;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;

class RegistrationJPARepositoryIT{
	
	private RegistrationJPARepository repository;
	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	
	private LinkedList<Student> students = new LinkedList<>();
	private LinkedList<Course> courses = new LinkedList<>();

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
		repository = new RegistrationJPARepository(entityManager);
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
	public void test_findAll_when_empty_should_return_empy_list() {
		assertThat(repository.findAll()).isEmpty();
	}

	@Test
	public void test_findAll() {
		Registration registration1 = new Registration(students.get(0), courses.get(0), true);
		Registration registration2 = new Registration(students.get(2), courses.get(0), false);
		addRegistrationToDB(registration1);
		addRegistrationToDB(registration2);
				
		assertThat(repository.findAll())
			.containsOnly(registration1,registration2);
	}
	
	@Test
	public void test_findById_when_not_found_should_return_null() {

		assertThat(repository.findById(2L,1L)).isNull();
	}
	
	@Test
	public void test_findById_successful() {
		Registration registration1 = new Registration(students.get(0), courses.get(0), true);
		Registration registration2 = new Registration(students.get(1), courses.get(2), false);
		addRegistrationToDB(registration1);
		addRegistrationToDB(registration2);
		
		assertThat(repository.findById(students.get(1).getId(),courses.get(2).getId()))
			.isSameAs(registration2);
	}
	
	@Test
	public void test_save_new_Registration() {
		Registration registration = new Registration(students.get(2), courses.get(1), true);
		
		entityManager.getTransaction().begin();
		repository.save(registration);
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
	}
	
	@Test
	public void test_save_duplicate_Registration_should_ignore_it_and_return_null() {
		Registration registration = new Registration(students.get(2), courses.get(1), true);
		addRegistrationToDB(registration);
		Registration registration2 = new Registration(students.get(2), courses.get(1), false);
		
		entityManager.getTransaction().begin();
		repository.save(registration2);
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
	}
	
	
	@Test
	public void test_delete_when_Registration_not_present_should_return_null() {
		entityManager.getTransaction().begin();
		assertThat(repository.delete(2L, 3L)).isNull();
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.isEmpty();
	}
	
	@Test
	public void test_delete_when_Registration_is_present() {
		Registration registration = new Registration(students.get(2), courses.get(1), false);
		addRegistrationToDB(registration);
		
		entityManager.getTransaction().begin();
		assertThat(repository.delete(students.get(2).getId(), courses.get(1).getId()))
			.isSameAs(registration);
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.isEmpty();
	}

	
	private void addRegistrationToDB(Registration registration) {
		 entityManager.getTransaction().begin();
		 registration.getStudent().getRegistrations().add(registration);
		 registration.getCourse().getRegistrations().add(registration);
		 entityManager.getTransaction().commit();
	}


}
