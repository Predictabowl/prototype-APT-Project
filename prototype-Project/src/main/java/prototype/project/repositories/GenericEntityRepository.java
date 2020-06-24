package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.GenericEntity;

public interface GenericEntityRepository<E extends GenericEntity> {
	public List<E> findAll();
	public E findById(long id);
	public E findByCode (String code);
	public E save(E entity);
	public E delete(E entity);
}
