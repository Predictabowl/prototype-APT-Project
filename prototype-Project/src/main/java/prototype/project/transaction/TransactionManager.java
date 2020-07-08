package prototype.project.transaction;

import java.util.function.Function;
import java.util.function.Supplier;

import prototype.project.exceptions.SchoolDomainException;

public interface TransactionManager<T> {
	<R> R doInTransaction(Function<T,R> code) throws SchoolDomainException;
}
