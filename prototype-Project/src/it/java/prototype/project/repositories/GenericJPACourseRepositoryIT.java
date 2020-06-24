package prototype.project.repositories;

import org.junit.jupiter.api.BeforeEach;

import prototype.project.model.Course;

public class GenericJPACourseRepositoryIT extends AbstractGenericJPARepositoryIT<Course>{

	@BeforeEach
	public void setUp() {
		setUpRepository(Course.class);
	}
	
	@Override
	protected Course createSampleEntity1() {
		return new Course("Code1", "Test course");
	}

	@Override
	protected Course createSampleEntity2() {
		return new Course("Cod3", "Some other course");
	}

}
