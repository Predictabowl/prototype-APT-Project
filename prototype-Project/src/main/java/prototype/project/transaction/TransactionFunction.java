package prototype.project.transaction;

import java.util.function.Function;

import prototype.project.model.GenericEntity;
//import prototype.project.model.GenericEntity;
import prototype.project.repositories.GenericJPAEntityRepository;

@FunctionalInterface
public interface TransactionFunction<E extends GenericEntity, T> extends Function<GenericJPAEntityRepository<E>, T>{
	
}
