package prototype.project.transaction;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.repositories.GenericEntityRepository;

public abstract class AbstractTransactionJPAManager<T> implements TransactionManager<T>{

	private static final Logger LOGGER = LogManager.getLogger(AbstractTransactionJPAManager.class);
	
	private EntityManager entityManager;
	private T repository;

	public AbstractTransactionJPAManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		repository = createRepository(entityManager);
	}
	
	@Override
	public <R> R doInTransaction(Function<T,R> code) throws SchoolDomainException {
		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			transaction.begin();
			R result = code.apply(repository);
			transaction.commit();
			return result;
		} catch (PersistenceException e) {
			doRollBack(transaction);
			throw new SchoolDomainException("Error during transaction",e);
		} catch (Exception e) {
			doRollBack(transaction);
			throw e;
		}

	}
	
	private void doRollBack(EntityTransaction transaction) {
		try {
			transaction.rollback();
		} catch (Exception e) {
			LOGGER.error("Rollback failed",e);
		}
	}
	
	protected abstract T createRepository(EntityManager entityManager); 
		
}
