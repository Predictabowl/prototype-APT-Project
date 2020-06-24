package prototype.project.repositories;

import org.junit.jupiter.api.BeforeEach;

import prototype.project.model.Student;

public class GenericJPAStudentRepositoryIT extends AbstractGenericJPARepositoryIT<Student>{

	@BeforeEach
	public void setUp() {
		setUpRepository(Student.class);
	}
	
	@Override
	protected Student createSampleEntity1() {
		return new Student("Stc1", "test student 1");
	}

	@Override
	protected Student createSampleEntity2() {
		return new Student("C0d32", "Student test 2");
	}
	

}
