package prototype.project.test.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;


public class GenericJPAEntitySetup {
	protected static EntityManagerFactory eManagerFactory;
	protected static EntityManager entityManager;

	
	public static void setUpEntity(Class<?> entityClass) {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();
		
		entityManager.getTransaction().begin();
		entityManager.createQuery("from "+entityClass.getName(),entityClass).getResultStream()
			.forEach(e -> entityManager.remove(e));
		entityManager.getTransaction().commit();
	}
	
	@AfterEach
	public void tearDown() {
		if(entityManager != null)
			entityManager.close();
		if(eManagerFactory != null)
			eManagerFactory.close();
	}
}
