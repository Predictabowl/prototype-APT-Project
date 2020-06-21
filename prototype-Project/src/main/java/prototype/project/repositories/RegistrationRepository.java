package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Registration;

public interface RegistrationRepository {
	public List<Registration> findAll();
	public Registration findById(long courseId, long studentId);
	public Registration save(Registration registration) throws SchoolDatabaseException;
	public Registration delete(long courseId, long studentId);
}
