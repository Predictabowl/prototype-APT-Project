package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;
import prototype.project.repositories.RegistrationJPARepository;
import prototype.project.test.utils.JPAEntityManagerSetup;

class TransactionJPAManagerLearningIT extends JPAEntityManagerSetup{

//	private GenericJPAEntityRepository<Course> repository;
//	private EntityManager spyEM;
	private AbstractTransactionJPAManager manager;
	
	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);
//		spyEM = spy(entityManager);
//		repository = new GenericJPAEntityRepository<Course>(Course.class, spyEM);
		manager = new AbstractTransactionJPAManager(entityManager);
	}
	

	@Test
	public void test_TempTransactionManager() {
		Course course = new Course("CS1", "Corso buffo");
		GenericJPAEntityRepository<Course> repository = new GenericJPAEntityRepository<Course>(Course.class, entityManager);
		Course result = null;
		try {
			result = manager.doInTransaction(() -> repository.save(course));
		} catch (SchoolDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Course> courses = entityManager.createQuery("from Course",Course.class).getResultList(); 
		assertThat(courses).containsExactly(result);
		assertThat(courses.iterator().next()).isEqualToComparingFieldByField(result);
	}
	
	@Test
	void test_save_registration_with_TempManager() {
		Student student = new Student("S1", "Mario");
		Course course = new Course("C2", "Corso di salto");
		GenericJPAEntityRepository<Student> studentRepository = new GenericJPAEntityRepository<>(Student.class, entityManager);
		GenericJPAEntityRepository<Course> courseRepository = new GenericJPAEntityRepository<>(Course.class, entityManager);		
		try {
			manager.doInTransaction(() -> studentRepository.save(student));
			manager.doInTransaction(() -> courseRepository.save(course));
		} catch (SchoolDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		Registration registration = new Registration(student, course, false);
		RegistrationJPARepository regRepo = new RegistrationJPARepository(entityManager);
		
		
		try {
			manager.doInTransaction(() -> regRepo.save(registration));
		} catch (SchoolDomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
		
		Student savedS = entityManager.createQuery("from Student",Student.class).getSingleResult();
		Course savedC = entityManager.createQuery("from Course",Course.class).getSingleResult();
		assertThat(savedC.getRegistrations().iterator().next().isPaid()).isFalse();
		assertThat(savedS.getRegistrations().iterator().next().isPaid()).isFalse();
	}
	
	@Test
	public void learningTest() {
		Student student = new Student("CD1", "test name");
		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.getTransaction().commit();
	}
	
	@Test
	public void rollback_learning_tests() {
		Student student = new Student("CD1", "test name");
		Student student2 = new Student("CD1", "test 2");
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(student);
			entityManager.persist(student2);
			transaction.commit();
		} catch (Exception e) {
		} finally {
			assertThat(transaction.getRollbackOnly()).isTrue();
			transaction.rollback();
		}
	}
	
	@Test
	public void rollback_skipped_learning_tests() {
		Student student = new Student("CD1", "test name");
		Student student2 = new Student("CD1", "test 2");
		EntityTransaction transaction = entityManager.getTransaction();
		try {
			transaction.begin();
			entityManager.persist(student);
			entityManager.persist(student2);
			transaction.commit();
		} catch (Exception e) {
		} finally {
			assertThat(transaction.getRollbackOnly()).isTrue();
		}
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}
