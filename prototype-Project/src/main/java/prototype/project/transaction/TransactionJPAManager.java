package prototype.project.transaction;

import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import prototype.project.exceptions.SchoolDatabaseException;

public class TransactionJPAManager implements TransactionManager{

	private static final Logger LOGGER = LogManager.getLogger();
	
	private EntityManager entityManager;

	public TransactionJPAManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public <R> R doInTransaction(Supplier<R> code) {
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		R result = code.get();
		if (!transaction.getRollbackOnly()) {
			transaction.commit();
		} else {
			try {
				transaction.rollback();
			} catch (Exception e) {
				LOGGER.error("Rollback failed",e);
			}
		}
		return result;
	}


}
