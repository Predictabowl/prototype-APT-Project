package prototype.project.transaction;

import static org.assertj.core.api.Assertions.*;
import org.mockito.stubbing.Answer;

import nl.altindag.log.LogCaptor;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ExceptionUtils;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.GenericEntity;
import prototype.project.model.Student;
import prototype.project.repositories.GenericJPAEntityRepository;
import prototype.project.test.utils.TestAppender;


class TransactionJPAManagerIT{
	private EntityManagerFactory eManagerFactory;
	private EntityManager entityManager;
	private EntityManager spyEM;
	
	@Mock
	Supplier<?> code;
	
	private AbstractTransactionJPAManager transactionManager;
	
	@Mock
	private GenericJPAEntityRepository<?> repository;
	private EntityTransaction spyTransaction;

	@BeforeEach
	public void setUp() {
		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
		entityManager = eManagerFactory.createEntityManager();
		spyEM = spy(entityManager);
		initMocks(this);
		transactionManager = new AbstractTransactionJPAManager(spyEM);
		
		spyTransaction = spy(entityManager.getTransaction());
		when(spyEM.getTransaction()).thenReturn(spyTransaction);
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
		Object anyObj = new Object();
		doReturn(anyObj).when(code).get();
		
		
		assertThatCode(() ->{ 
			assertThat(transactionManager.doInTransaction(code)).isSameAs(anyObj);
		}).doesNotThrowAnyException();
		
		InOrder inOrder = inOrder(code,spyTransaction);
		inOrder.verify(spyTransaction).begin();
		inOrder.verify(code).get();
		inOrder.verify(spyTransaction).commit();
		
	}
	
	@Test
	public void test_when_PresistanceException_should_throw_new_exception_and_rollBack_transaction() {
		doThrow(PersistenceException.class).when(code).get();
		
		assertThatThrownBy(() -> transactionManager.doInTransaction(code))
			.isExactlyInstanceOf(SchoolDomainException.class)
			.hasMessage("Error during transaction")
			.getCause().isExactlyInstanceOf(PersistenceException.class);
		
		InOrder inOrder = inOrder(code,spyTransaction);
		inOrder.verify(spyTransaction).begin();
		inOrder.verify(code).get();
		inOrder.verify(spyTransaction).rollback();
		verify(spyTransaction,never()).commit();
	}
	
	@Test
	public void test_when_code_is_marked_for_rollback__should_rollback_transaction() {
		Exception exception = new IllegalArgumentException();
		when(code.get()).thenThrow(exception);
		
		assertThatThrownBy(() ->  transactionManager.doInTransaction(code))
			.isSameAs(exception);
		
		InOrder inOrder = inOrder(code,spyTransaction);
		inOrder.verify(spyTransaction).begin();
		inOrder.verify(code).get();
		inOrder.verify(spyTransaction).rollback();
		verify(spyTransaction,never()).commit();
	}
	
	@Test
	public void test_rollback_failure() {
		LogCaptor<AbstractTransactionJPAManager> logCaptor = LogCaptor.forClass(AbstractTransactionJPAManager.class);
		when(code.get()).thenThrow(PersistenceException.class);
		doThrow(IllegalStateException.class).when(spyTransaction).rollback();
		
		try {
			transactionManager.doInTransaction(code);
		} catch (Exception e) {
		}
		
		assertThat(logCaptor.getErrorLogs()).containsExactly("Rollback failed");
//		final Logger logger = LogManager.getLogger(TransactionJPAManager.class);
//		assertThat(logger)

		//		InOrder inOrder = inOrder(code,spyTransaction);
//		inOrder.verify(spyTransaction).begin();
//		inOrder.verify(code).get();
//		inOrder.verify(spyTransaction).rollback();
//		verify(spyTransaction,never()).commit();
	}
	
	@Test
	public void learning_logging_test() {
//		final TestAppender appender = 
		
		when(code.get()).thenThrow(IllegalArgumentException.class);
		doThrow(IllegalStateException.class).when(spyTransaction).rollback();
		
		try {
			transactionManager.doInTransaction(code);
		} catch (Exception e) {
		}
	}
	
}
