package prototype.project.repositories;

import org.junit.jupiter.api.BeforeEach;

import prototype.project.model.Student;

public class GenericJPAStudentRepositoryIT extends AbstractGenericJPARepositoryIT<Student>{

	@BeforeEach
	public void setUp() {
		setUpRepository(Student.class);
	}
	
	@Override
	protected Student createSampleEntity(int i) {
		return new Student("Stcd"+i, "test student "+i);
	}	

}
