package prototype.project.repositories;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import prototype.project.model.Student;

public class StudentJPARepository implements StudentRepository{

	private EntityManager entityManager;

	public StudentJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Student> findAll() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Student findOne(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Student student) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Student delete(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
