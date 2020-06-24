package prototype.project.repositories;

import org.junit.jupiter.api.BeforeEach;

import prototype.project.model.Course;

public class GenericJPACourseRepositoryIT extends AbstractGenericJPARepositoryIT<Course>{

	@BeforeEach
	public void setUp() {
		setUpRepository(Course.class);
	}
	
	@Override
	protected Course createSampleEntity(int i) {
		return new Course("Code"+i, "Test course "+i);
	}

}
