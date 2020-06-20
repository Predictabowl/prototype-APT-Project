package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Course;

public interface CourseRepository{
	public List<Course> findAll();
	public Course findByCode(String code);
	public Course findById(long id);
	public Course save(Course course) throws SchoolDatabaseException;
	public Course delete(long id);
}

