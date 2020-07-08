package prototype.project.transaction;

import javax.persistence.EntityManager;

import prototype.project.model.Course;
import prototype.project.repositories.GenericJPAEntityRepository;

public class CourseTransactionJPAManager extends AbstractTransactionJPAManager<GenericJPAEntityRepository<Course>>{

	public CourseTransactionJPAManager(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected GenericJPAEntityRepository<Course> createRepository(EntityManager entityManager) {
		return new GenericJPAEntityRepository<>(Course.class, entityManager);
	}

}
