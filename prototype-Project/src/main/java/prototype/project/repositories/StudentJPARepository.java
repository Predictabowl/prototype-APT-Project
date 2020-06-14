package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import prototype.project.model.Student;

public class StudentJPARepository implements StudentRepository{

	private EntityManager entityManager;

	public StudentJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Student> findAll() {
		return entityManager.createQuery("from Student",Student.class).getResultList();
	}

	@Override
	public Student findOne(String code) {
		Student student;
		try {
			student = entityManager.createQuery("SELECT s FROM Student s WHERE s.code LIKE :sCode",Student.class)
					.setParameter("sCode", code).getSingleResult();
		} catch (NoResultException e) {
			student = null;
		}
		return student;
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
