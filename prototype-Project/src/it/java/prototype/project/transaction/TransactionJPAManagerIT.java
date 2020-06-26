package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*; 

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import prototype.project.model.GenericEntity;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;


class TransactionJPAManagerIT{
	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	private EntityManager spyEM;
	
	private TransactionJPAManager transactionManager;
	
	@Mock
	private GenericJPAEntityRepository<?> repository;

	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();
		spyEM = spy(entityManager);
		transactionManager = new TransactionJPAManager(spyEM);
	}
	
	@AfterEach
	public void tearDown() {
		if(entityManager != null)
			entityManager.close();
		if(eManagerFactory != null)
			eManagerFactory.close();
	}
	
	@Test
	public void test_persist_transation_succesful() {
		Student student = new Student("CD1", "test name");
		EntityTransaction spyTransaction = spy(entityManager.getTransaction());
		when(spyEM.getTransaction()).thenReturn(spyTransaction);
		
		Student persistedS = 
		transactionManager.doInTransaction(() -> {
			entityManager.persist(student);
			return student;
		});
		
		assertThat(persistedS).isSameAs(student);
		InOrder inOrder = inOrder(spyTransaction);
		inOrder.verify(spyTransaction).begin();
		inOrder.verify(spyTransaction).commit();
	}
	
	@Test
	public void test_rollBack_transaction() {
		transactionManager.doInTransaction(() -> {
			
		});
	}

}
