package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.Course;
import prototype.project.model.Registration;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;
import prototype.project.test.utils.GenericJPAEntitySetup;

class TransactionJPAManagerLearningIT extends GenericJPAEntitySetup{

//	private GenericJPAEntityRepository<Course> repository;
//	private EntityManager spyEM;
	private TransactionManager manager;
	
	@BeforeEach
	public void setUp() {
		setUpEntity(Course.class);
//		spyEM = spy(entityManager);
//		repository = new GenericJPAEntityRepository<Course>(Course.class, spyEM);
		manager = new TransactionJPAManager(entityManager);
	}
	
	
	@Test
	void test() {
		Course course = new Course("CS1", "Corso buffo");
		Course saved = manager.doInTransaction(c -> c.save(course), Course.class);
		
		List<Course> courses = entityManager.createQuery("from Course",Course.class).getResultList();
		
		assertThat(courses).containsExactly(course);
		assertThat(courses.iterator().next()).isEqualToComparingFieldByField(saved);
	}
	
	@Test
	void test_save_registration() {
		Student student = new Student("S1", "Mario");
		Course course = new Course("C2", "Corso di salto");
		Student studentP = manager.doInTransaction(c -> c.save(student), Student.class);
		Course courseP = manager.doInTransaction(c -> c.save(course), Course.class);
	
		
		Registration registration = new Registration(student, course, false);
		entityManager.getTransaction().begin();
		registration.getCourse().getRegistrations().add(registration);
		entityManager.getTransaction().commit();
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
		
		Student savedS = entityManager.createQuery("from Student",Student.class).getSingleResult();
		Course savedC = entityManager.createQuery("from Course",Course.class).getSingleResult();
		assertThat(savedC.getRegistrations().iterator().next().isPaid()).isFalse();
		
		registration.setPaid(true);
		
		manager.doInTransaction(c -> {
				registration.getStudent().getRegistrations().add(registration);
//				registration.getCourse().getRegistrations().add(registration);
				return null;
			}, Student.class);
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
		
		savedS = entityManager.createQuery("from Student",Student.class).getSingleResult();
		savedC = entityManager.createQuery("from Course",Course.class).getSingleResult();
		assertThat(savedC.getRegistrations().iterator().next().isPaid()).isTrue();
		assertThat(savedS.getRegistrations().iterator().next().isPaid()).isTrue();
	}

	@Test
	public void test_TempTransactionManager() {
		TempTransactionManager tem = new TempTransactionJPAManager(entityManager);
		Course course = new Course("CS1", "Corso buffo");
		GenericJPAEntityRepository<Course> repository = new GenericJPAEntityRepository<Course>(Course.class, entityManager);
		Course result = tem.doInTransaction(() -> repository.save(course));
		
		List<Course> courses = entityManager.createQuery("from Course",Course.class).getResultList(); 
		assertThat(courses).containsExactly(result);
		assertThat(courses.iterator().next()).isEqualToComparingFieldByField(result);
	}
	
	@Test
	void test_save_registration_with_TempManager() {
		TempTransactionManager tem = new TempTransactionJPAManager(entityManager);
		Student student = new Student("S1", "Mario");
		Course course = new Course("C2", "Corso di salto");
		GenericJPAEntityRepository<Student> studentRepository = new GenericJPAEntityRepository<>(Student.class, entityManager);
		GenericJPAEntityRepository<Course> courseRepository = new GenericJPAEntityRepository<>(Course.class, entityManager);		
		Student studentP = tem.doInTransaction(() -> studentRepository.save(student));
		Course courseP = tem.doInTransaction(() -> courseRepository.save(course));
	
		
		Registration registration = new Registration(student, course, false);
		
		
		tem.doInTransaction(() -> {
				registration.getStudent().getRegistrations().add(registration);
				registration.getCourse().getRegistrations().add(registration);
				return null;
			});
		
		assertThat(entityManager.createQuery("from Registration",Registration.class).getResultList())
			.containsExactly(registration);
		
		Student savedS = entityManager.createQuery("from Student",Student.class).getSingleResult();
		Course savedC = entityManager.createQuery("from Course",Course.class).getSingleResult();
		assertThat(savedC.getRegistrations().iterator().next().isPaid()).isFalse();
		assertThat(savedS.getRegistrations().iterator().next().isPaid()).isFalse();
	}
}
