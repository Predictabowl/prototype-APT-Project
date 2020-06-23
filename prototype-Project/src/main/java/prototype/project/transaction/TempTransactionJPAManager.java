package prototype.project.transaction;

import java.util.function.Supplier;

import javax.persistence.EntityManager;

public class TempTransactionJPAManager implements TempTransactionManager{

	private EntityManager em;

	public TempTransactionJPAManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public <T> T doInTransaction(Supplier<T> code) {
		em.getTransaction().begin();
		T result = code.get();
		em.getTransaction().commit();
		return result;
	}

}
