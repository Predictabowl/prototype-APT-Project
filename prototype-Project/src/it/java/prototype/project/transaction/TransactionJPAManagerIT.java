package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

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
	
	@Mock
	Supplier<?> code;
	
	private TransactionJPAManager transactionManager;
	
	@Mock
	private GenericJPAEntityRepository<?> repository;

	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();
		spyEM = spy(entityManager);
		initMocks(this);
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
	public void test_lambda_called_in_transation_succesful() {
		EntityTransaction spyTransaction = spy(entityManager.getTransaction());
		when(spyEM.getTransaction()).thenReturn(spyTransaction);
		Object anyObj = new Object();
		doReturn(anyObj).when(code).get();
		
		assertThat(transactionManager.doInTransaction(code)).isSameAs(anyObj);
		
		InOrder inOrder = inOrder(code,spyTransaction);
		inOrder.verify(spyTransaction).begin();
		inOrder.verify(code).get();
		inOrder.verify(spyTransaction).commit();
	}
	
	@Test
	public void learningTest() {
		Student student = new Student("CD1", "test name");
		entityManager.getTransaction().begin();
		entityManager.persist(student);
		entityManager.getTransaction().commit();
	}
	
	@Test
	public void test_rollBack_transaction() {
//		transactionManager.doInTransaction(() -> {
			
//		});
	}
	
	private class mockedLambda<T,R> implements Function<T, R>{

		@Override
		public R apply(T t) {
			return any();
		}
		
	}
	
}
