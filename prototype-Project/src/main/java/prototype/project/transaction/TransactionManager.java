package prototype.project.transaction;

import java.util.function.Supplier;

public interface TransactionManager {
	<R> R doInTransaction(Supplier<R> code);
}
