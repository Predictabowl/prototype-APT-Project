package prototype.project.repositories;

import java.util.List;

import prototype.project.exceptions.CodeConstraintViolationException;
import prototype.project.model.Student;

public interface StudentRepository{
	public List<Student> findAll();
	public Student findByCode(String code);
	public Student findById(long id);
	public Student save(Student student) throws CodeConstraintViolationException;
	public Student delete(long id);
}

