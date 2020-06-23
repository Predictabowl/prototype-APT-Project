package prototype.project.transaction;

import prototype.project.model.GenericEntity;

public interface TransactionManager {
	<E extends GenericEntity,T> T doInTransaction(TransactionFunction<E,T> function, Class<E> classType);
}
