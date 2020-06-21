package prototype.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Course;
import prototype.project.test.utils.GenericJPAEntitySetup;

class GenericJPACourseRepositoryIT extends GenericJPAEntitySetup{

	private GenericJPAEntityRepository<Course> repository;
	
	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);		
		repository = new GenericJPAEntityRepository<Course>(Course.class, entityManager);
	}
	
	@Test
	public void test_findAll_when_empty() {
		List<Course> courses = repository.findAll();
		assertThat(courses).isEmpty();
	}
	
	@Test
	public void test_findAll_when_not_empty() {
		Course course = new Course("A1", "English course");
		persistCourseToDB(course);
		
		List<Course> courses= repository.findAll();
		
		assertThat(courses).containsExactly(course);
	}
	
	@Test
	public void test_findByCode_successful() {
		Course course = new Course("Cr4","test description");
		Course course2 = new Course("Cr1","Another Description");
		persistCourseToDB(course);
		persistCourseToDB(course2);
		
		Course found = repository.findByCode("Cr1");
		
		assertThat(found).isSameAs(course2);
		
	}
	
	@Test
	public void test_findByCode_when_not_present() {
		Course notFound = repository.findByCode("code1");
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_findById_when_not_present() {
		Course notFound = repository.findById(1L);
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_findById_successful() {
		Course course = new Course("St4","test Description");
		Course course2 = new Course("St1","Another description");
		persistCourseToDB(course);
		persistCourseToDB(course2);
		
		Course found = repository.findById(course2.getId());
		
		assertThat(found).isSameAs(course2);
	}

	
	@Test
	public void test_save_successful() {
		Course course = new Course("code1","Literature");
				
		assertThat(catchThrowable(() -> {
			entityManager.getTransaction().begin();
			Course saved = repository.save(course);
			entityManager.getTransaction().commit();
			
			assertThat(saved).isSameAs(entityManager
					.createQuery("from Course",Course.class)
					.getSingleResult());
		})).isNull();
		
	}

	
	@Test
	public void test_save_when_PersistanceException_occur_should_throw_new_Exception() {
		Course course1 = new Course("CS1", "History");
		persistCourseToDB(course1);
		Course course2 = new Course("CS1", "Logic");

		entityManager.getTransaction().begin();
		
		assertThatThrownBy(() -> repository.save(course2))
			.isInstanceOf(SchoolDatabaseException.class)
			.hasMessage("Error while saving entity: "+course2)
			.getCause().isInstanceOf(PersistenceException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class)
				.getSingleResult()).isEqualToComparingFieldByField(course1);

	}
	
	@Test
	public void test_save_when_id_present_should_update() {
		Course course1 = new Course("C2","test 1");
		persistCourseToDB(course1);
		Course course2 = new Course("C2","modified name");
		course2.setId(course1.getId());
		
		entityManager.getTransaction().begin();		
		assertThat(catchThrowable(() -> repository.save(course2))).isNull();
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class)
			.getResultList()).containsExactly(course2);
		assertThat(course1).isEqualToComparingFieldByField(course2);
	}
	
	@Test
	public void test_delete_when_Course_not_present_should_return_null() {
		Course course = new Course("C1", "test");
		persistCourseToDB(course);
		
		entityManager.getTransaction().begin();
		assertThat(repository.delete(course.getId()+1)).isNull();
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class).getResultList())
			.containsExactly(course);
	}
	
	@Test
	public void test_delete_Course_successful() {
		Course course = new Course("C1", "test");
		persistCourseToDB(course);
		
		entityManager.getTransaction().begin();
		
		assertThat(repository.delete(course.getId())).isEqualToComparingFieldByField(course);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Course",Course.class).getResultList())
			.isEmpty();
	}	

	private void persistCourseToDB(Course course) {
		entityManager.getTransaction().begin();
		entityManager.persist(course);
		entityManager.getTransaction().commit();
	}

}
