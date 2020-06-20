package prototype.project.model;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentIT {

	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	
	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();
		
		entityManager.createQuery("from Student",Student.class).getResultStream()
			.forEach(e -> entityManager.remove(e));
	}
	
	@AfterEach
	public void tearDown() {
		entityManager.close();
		eManagerFactory.close();
	}
	
	@Test
	void test_duplicate_Code_should_throw() {
		Student student1 = new Student("AR1", "test1");
		Student student2 = new Student("AR1", "test2");
		entityManager.getTransaction().begin();
		entityManager.persist(student1);
		entityManager.getTransaction().commit();
		entityManager.getTransaction().begin();
		
		assertThatThrownBy(() -> entityManager.persist(student2))
			.isInstanceOf(PersistenceException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class).getResultList())
			.containsExactly(student1);
	}
	
	@Test
	void test_null_Code_should_throw() {
		Student student = new Student(null, "test1");
		entityManager.getTransaction().begin();

		assertThatThrownBy(() -> entityManager.persist(student))
			.isInstanceOf(PersistenceException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class).getResultList())
			.isEmpty();
	}
	
	@Test
	void test_update_Code_should_throw() {
		Student student = new Student("AR1", "test1");
		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.getTransaction().commit();
		
		student.setCode("BC2");
		
		entityManager.getTransaction().begin();
		assertThatThrownBy(() -> entityManager.getTransaction().commit())
			.isInstanceOf(PersistenceException.class);
	
		assertThat(entityManager.createQuery("from Student",Student.class).getResultList())
			.containsExactly(new Student("AR1","test1"));
	}

}
