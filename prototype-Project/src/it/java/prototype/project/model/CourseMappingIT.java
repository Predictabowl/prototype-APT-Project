package prototype.project.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.PersistenceException;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.utils.JPAEntityManagerSetup;

class CourseMappingIT extends JPAEntityManagerSetup{

	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);
	}
		
	@Test
	void test_duplicate_Code_should_throw_exception() {
		Course course1 = new Course("CS1", "test1");
		Course course2 = new Course("CS1", "test2");
		entityManager.getTransaction().begin();
		entityManager.persist(course1);
		entityManager.getTransaction().commit();
		entityManager.getTransaction().begin();
		
		assertThatThrownBy(() -> entityManager.persist(course2))
			.isInstanceOf(PersistenceException.class)
			.getCause().isExactlyInstanceOf(ConstraintViolationException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class).getResultList())
			.containsExactly(course1);
	}
	
	@Test
	void test_null_Code_should_throw_exception() {
		Course course = new Course(null, "test1");
		entityManager.getTransaction().begin();

		assertThatThrownBy(() -> entityManager.persist(course))
			.isInstanceOf(PersistenceException.class)
			.getCause().isExactlyInstanceOf(PropertyValueException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class).getResultList())
			.isEmpty();
	}
	
	@Test
	void test_update_Code_should_throw_exception() {
		Course course = new Course("AR1", "test1");
		entityManager.getTransaction().begin();
		entityManager.persist(course);
		entityManager.getTransaction().commit();
		
		course.setCode("BC2");
		
		entityManager.getTransaction().begin();
		assertThatThrownBy(() -> entityManager.getTransaction().commit())
			.isInstanceOf(PersistenceException.class);
		
		assertThat(entityManager.createQuery("from Course",Course.class).getResultList())
			.containsExactly(new Course("AR1","test1"));
	}

}
