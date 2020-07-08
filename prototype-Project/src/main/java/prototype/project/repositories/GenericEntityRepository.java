package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.GenericEntity;

public interface GenericEntityRepository<E,I> {
	public List<E> findAll();
	public E findById(I id);
//	public E findByCode (String code);
	public E save(E entity);
	public E delete(E entity);
}
