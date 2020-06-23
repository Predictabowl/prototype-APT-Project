package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Registration;

public interface RegistrationRepository {
	public List<Registration> findAll();
	public Registration findById(long studentId, long courseId);
//	public Registration save(Registration registration);
//	public Registration delete(long studentId, long courseId);
}
