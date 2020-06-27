package prototype.project.transaction;

import java.util.function.Function;

import prototype.project.model.GenericEntity;
import prototype.project.repositories.GenericEntityRepository;

public interface StudentTransactionCode<R> extends Function<GenericEntityRepository<GenericEntity>, R> {

}
