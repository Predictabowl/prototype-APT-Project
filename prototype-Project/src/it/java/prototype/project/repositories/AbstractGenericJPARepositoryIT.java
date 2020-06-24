package prototype.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import prototype.project.model.GenericEntity;
import prototype.project.utils.JPAEntityManagerSetup;

public abstract class AbstractGenericJPARepositoryIT<E extends GenericEntity> extends JPAEntityManagerSetup{

	private GenericJPAEntityRepository<E> repository;
	private EntityManager spyEM;
	private Class<E> entityClass;

	
	protected void setUpRepository(Class<E> entityClass) {
		this.entityClass = entityClass;
		setUpEntity(entityClass);
		
		spyEM = spy(entityManager);
		repository = new GenericJPAEntityRepository<E>(entityClass, spyEM);
	}
	
//	protected void setUpEntity(Class<E> entityClass) {
//		this.entityClass = entityClass;
//		eManagerFactory = Persistence.createEntityManagerFactory("prototype.project");
//		entityManager = eManagerFactory.createEntityManager();
//		
//		entityManager.getTransaction().begin();
//		entityManager.createQuery("from "+entityClass.getName(),entityClass).getResultStream()
//			.forEach(e -> entityManager.remove(e));
//		entityManager.getTransaction().commit();
//		
//		spyEM = spy(entityManager);
//		repository = new GenericJPAEntityRepository<E>(entityClass, spyEM);
//	}
//	
//	@AfterEach
//	public void tearDown() {
//		if(entityManager != null)
//			entityManager.close();
//		if(eManagerFactory != null)
//			eManagerFactory.close();
//	}
	
	
	@Test
	public void test_findAll_when_empty() {
		List<E> entities = repository.findAll();
		assertThat(entities).isEmpty();
	}
	
	@Test
	public void test_findAll_when_not_empty() {
		E entity = createSampleEntity1();
		persistEntityToDB(entity);
		
		List<E> entities= repository.findAll();
		
		assertThat(entities).containsExactly(entity);
	}
	
	@Test
	public void test_findByCode_successful() {
		E entity1 = createSampleEntity1();
		E entity2 = createSampleEntity2();
		persistEntityToDB(entity1);
		persistEntityToDB(entity2);
		
		E found = repository.findByCode(entity2.getCode());
		
		assertThat(found).isSameAs(entity2);
	}
	
	@Test
	public void test_findByCode_when_not_present() {
		E notFound = repository.findByCode("code1");
		
		assertThat(notFound).isNull();
	}
	
	@Test
	public void test_findById_when_not_present() {
		E notFound = repository.findById(1L);
		
		assertThat(notFound).isNull();
		verify(spyEM).find(entityClass, 1L);
	}
	
	@Test
	public void test_findById_successful() {
		E entity1 = createSampleEntity1();
		E entity2 = createSampleEntity2();
		persistEntityToDB(entity1);
		persistEntityToDB(entity2);
		
		E found = repository.findById(entity2.getId());
		
		assertThat(found).isSameAs(entity2);
		verify(spyEM).find(entityClass, entity2.getId());
	}

	
	@Test
	public void test_save_successful() {
		E entity = createSampleEntity1();
				
		E saved = repository.save(entity);

		assertThat(entityManager.contains(saved)).isTrue();
		verify(spyEM).persist(entity);
	}

		
	@Test
	public void test_save_when_id_present_should_update() {
		E entity1 = createSampleEntity1();
		persistEntityToDB(entity1);
		E entity2 = createSampleEntity2();
		entity2.setId(entity1.getId());
		
		E saved = repository.save(entity2);
		
		assertThat(entityManager.contains(saved)).isTrue();
		assertThat(entity1).isEqualToComparingFieldByField(saved);
		verify(spyEM).merge(entity2);
	}
	
	@Test
	public void test_delete_when_entity_not_present_should_return_null() {
		E entity1 = createSampleEntity1();
		persistEntityToDB(entity1);
		E entity2 = createSampleEntity2();
		long fakeId = entity1.getId()+1;
		entity2.setId(fakeId);
		
		assertThat(repository.delete(entity2)).isNull();
		verify(spyEM).find(entityClass, fakeId);
		verify(spyEM,never()).remove(any(entityClass));
	}
	
	@Test
	public void test_delete_should_remove_and_return_deleted_entity() {
		E entity = createSampleEntity1();
		persistEntityToDB(entity);
		
		E deleted = repository.delete(entity);
		
		assertThat(deleted).isEqualToComparingFieldByField(entity);
		verify(spyEM).find(entityClass, entity.getId());
		verify(spyEM).remove(entity);
		assertThat(entityManager.contains(entity)).isFalse();
	}	

	private void persistEntityToDB(E entity) {
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}
	
	protected abstract E createSampleEntity1();
	protected abstract E createSampleEntity2();

}
