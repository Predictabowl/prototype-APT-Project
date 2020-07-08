package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDomainException;
import prototype.project.model.Registration;
import prototype.project.model.id.RegistrationId;

public interface RegistrationRepository{
	public List<Registration> findAll();
	public Registration findById(RegistrationId id);
	public void add (Registration registration);
	public void remove (Registration registration);
}
