package prototype.project.repositories;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.exceptions.CodeConstraintViolationException;
import prototype.project.model.Student;

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
		persistStudentToDB(student);
		
		List<Student> students = repository.findAll();
		
		assertThat(students).containsExactly(student);
	}
	
	@Test
	public void test_findByCode_successful() {
		Student student = new Student("St4","testName");
		Student student2 = new Student("St1","Another Name");
		persistStudentToDB(student);
		persistStudentToDB(student2);
		
		Student found = repository.findByCode("St1");
		
		assertThat(found).isSameAs(student2);
		
	}
	
	@Test
	public void test_findById_when_not_present() {
		Student notFound = repository.findById(1L);
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_findById_successful() {
		Student student = new Student("St4","testName");
		Student student2 = new Student("St1","Another Name");
		persistStudentToDB(student);
		persistStudentToDB(student2);
		
		Student found = repository.findById(student2.getId());
		
		assertThat(found).isSameAs(student2);
	}
	
	@Test
	public void test_findByCode_when_not_present() {
		Student notFound = repository.findByCode("code1");
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_save_successful() {
		Student student = new Student("AR1", "Carlo");
				
		assertThat(catchThrowable(() -> {
			entityManager.getTransaction().begin();
			Student saved = repository.save(student);
			entityManager.getTransaction().commit();
			
			assertThat(saved).isSameAs(entityManager
					.createQuery("from Student",Student.class)
					.getSingleResult());
		})).isNull();
		
	}
	
	
	@Test
	public void test_save_when_Student_code_already_present_should_throw() {
		Student student1 = new Student("AR1", "Carlo");
		persistStudentToDB(student1);
		Student student2 = new Student("AR1", "Luigi");

		entityManager.getTransaction().begin();
		
		assertThatThrownBy(() -> repository.save(student2))
			.isInstanceOf(CodeConstraintViolationException.class)
			.hasMessage("Error while saving Student, duplicate code: AR1")
			.getCause().isInstanceOf(PersistenceException.class);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class)
				.getSingleResult()).isEqualToComparingFieldByField(student1);

	}
	
	@Test
	public void test_save_when_id_present_should_update() {
		Student student1 = new Student("C2","test 1");
		persistStudentToDB(student1);
		Student student2 = new Student("C2","modified name");
		student2.setId(student1.getId());
		
		entityManager.getTransaction().begin();		
		assertThat(catchThrowable(() -> repository.save(student2))).isNull();
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class)
			.getResultList()).containsExactly(student2);
		assertThat(student1).isEqualToComparingFieldByField(student2);
	}
	
	@Test
	public void test_delete_when_Student_not_present_should_return_null() {
		Student student = new Student("C1", "test");
		persistStudentToDB(student);
		
		entityManager.getTransaction().begin();
		assertThat(repository.delete(student.getId()+1)).isNull();
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class).getResultList())
			.containsExactly(student);
	}
	
	@Test
	public void test_delete_Student_successful() {
		Student student = new Student("C1", "test");
		persistStudentToDB(student);
		
		entityManager.getTransaction().begin();
		
		assertThat(repository.delete(student.getId())).isEqualTo(student);
		
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Student",Student.class).getResultList())
			.isEmpty();
	}
		

	private void persistStudentToDB(Student student) {
		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.getTransaction().commit();
	}
	
}
