package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDatabaseException;

public interface GenericEntityRepository<E> {
	public List<E> findAll();
	public E findByCode(String code);
	public E findById(long id);
	public E save(E entity) throws SchoolDatabaseException;
	public E delete(long id);
}
