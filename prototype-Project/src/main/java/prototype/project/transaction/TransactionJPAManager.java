package prototype.project.transaction;

import javax.persistence.EntityManager;

import prototype.project.model.GenericEntity;
import prototype.project.repositories.GenericJPAEntityRepository;

public class TransactionJPAManager implements TransactionManager{

	private final EntityManager entityManager;
	
	public TransactionJPAManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	@Override
	public <E extends GenericEntity, T> T doInTransaction(TransactionFunction<E, T> code, Class<E> classType) {
		entityManager.getTransaction().begin();
		GenericJPAEntityRepository<E> repository = new GenericJPAEntityRepository<>(classType, entityManager);
		T transactionResult = code.apply(repository);
		entityManager.getTransaction().commit();
		return transactionResult;
//		return null;
	}

}
