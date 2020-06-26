package prototype.project.transaction;

import java.util.function.Function;

import javax.persistence.EntityManager;

public interface JPACode<R> extends Function<EntityManager, R>{

}
