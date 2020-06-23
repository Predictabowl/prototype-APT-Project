package prototype.project.transaction;

import java.util.function.Supplier;


public interface TempTransactionManager {
	<T> T doInTransaction(Supplier<T> code);
}
