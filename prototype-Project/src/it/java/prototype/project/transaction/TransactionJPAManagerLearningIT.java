package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;
import prototype.project.repositories.RegistrationJPARepository;
import prototype.project.test.utils.JPAEntityManagerSetup;

class TransactionJPAManagerLearningIT extends JPAEntityManagerSetup{

//	private GenericJPAEntityRepository<Course> repository;
//	private EntityManager spyEM;
	private TempTransactionJPAManager manager;
	
	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);
//		spyEM = spy(entityManager);
//		repository = new GenericJPAEntityRepository<Course>(Course.class, spyEM);
		manager = new TempTransactionJPAManager(entityManager);
	}
	

	@Test
	public void test_TempTransactionManager() {
		Course course = new Course("CS1", "Corso buffo");
		GenericJPAEntityRepository<Course> repository = new GenericJPAEntityRepository<Course>(Course.class, entityManager);
		Course result = manager.doInTransaction(() -> repository.save(course));
		
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
		manager.doInTransaction(() -> studentRepository.save(student));
		manager.doInTransaction(() -> courseRepository.save(course));
	
		
		Registration registration = new Registration(student, course, false);
		RegistrationJPARepository regRepo = new RegistrationJPARepository(entityManager);
		
		
		manager.doInTransaction(() -> regRepo.save(registration));
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
		
		Student savedS = entityManager.createQuery("from Student",Student.class).getSingleResult();
		Course savedC = entityManager.createQuery("from Course",Course.class).getSingleResult();
		assertThat(savedC.getRegistrations().iterator().next().isPaid()).isFalse();
		assertThat(savedS.getRegistrations().iterator().next().isPaid()).isFalse();
	}
}
