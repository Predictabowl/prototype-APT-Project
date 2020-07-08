package prototype.project.transaction;

import javax.persistence.EntityManager;

import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;

public class StudentTransactionJPAManager extends AbstractTransactionJPAManager<GenericJPAEntityRepository<Student>>{

	public StudentTransactionJPAManager(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected GenericJPAEntityRepository<Student> createRepository(EntityManager entityManager) {
		return new GenericJPAEntityRepository<>(Student.class, entityManager);
	}

}
