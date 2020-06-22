package prototype.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Course;
import prototype.project.test.utils.GenericJPAEntitySetup;

class GenericJPACourseRepositoryIT extends GenericJPAEntitySetup{

	private GenericJPAEntityRepository<Course> repository;
	private EntityManager spyEM;
	
	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);
		spyEM = spy(entityManager);
		repository = new GenericJPAEntityRepository<Course>(Course.class, spyEM);
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
		verify(spyEM).find(Course.class, 1L);
	}
	
	@Test
	public void test_findById_successful() {
		Course course = new Course("St4","test Description");
		Course course2 = new Course("St1","Another description");
		persistCourseToDB(course);
		persistCourseToDB(course2);
		
		Course found = repository.findById(course2.getId());
		
		assertThat(found).isSameAs(course2);
		verify(spyEM).find(Course.class, course2.getId());
	}

	
	@Test
	public void test_save_successful() {
		Course course = new Course("code1","Literature");
				
		Course saved = repository.save(course);

		assertThat(entityManager.contains(saved)).isTrue();
		verify(spyEM).persist(course);
	}

		
	@Test
	public void test_save_when_id_present_should_update() {
		Course course1 = new Course("C2","test 1");
		persistCourseToDB(course1);
		Course course2 = new Course("C2","modified name");
		course2.setId(course1.getId());
		
		Course persisted = repository.save(course2);
		
		assertThat(entityManager.contains(persisted)).isTrue();
		assertThat(course1).isEqualToComparingFieldByField(persisted);
		verify(spyEM).merge(course2);
	}
	
	@Test
	public void test_delete_when_Course_not_present_should_return_null() {
		Course course = new Course("C1", "test");
		persistCourseToDB(course);
	
		long fakeId = course.getId()+1;
		assertThat(repository.delete(fakeId)).isNull();
		verify(spyEM).find(Course.class, fakeId);
		verify(spyEM,never()).remove(any(Course.class));
	}
	
	@Test
	public void test_delete_should_return_deleted_entity_and_remove() {
		Course course = new Course("C1", "test");
		persistCourseToDB(course);
		
		Course deleted = repository.delete(course.getId());
		
		assertThat(deleted).isEqualToComparingFieldByField(course);
		verify(spyEM).find(Course.class, course.getId());
		verify(spyEM).remove(course);
		assertThat(entityManager.contains(course)).isFalse();
	}	

	private void persistCourseToDB(Course course) {
		entityManager.getTransaction().begin();
		entityManager.persist(course);
		entityManager.getTransaction().commit();
	}

}
