package prototype.project.repositories;

import java.util.List;

import prototype.project.model.Student;

public interface StudentRepository {
	public List<Student> findAll();
	public Student findOne(long id);
	public void save(Student student);
	public Student delete(long id);
}
