package prototype.project.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import prototype.project.exceptions.SchoolDatabaseException;
import prototype.project.model.Course;

public class CourseJPARepository implements CourseRepository{

	private EntityManager entityManager;

	public CourseJPARepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Course> findAll() {
		return entityManager.createQuery("from Course",Course.class).getResultList();
	}

	@Override
	public Course findByCode(String code) {
		try {
			return entityManager.createQuery("from Course c where c.code = :pCode",Course.class)
					.setParameter("pCode", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Course findById(long id) {
		return entityManager.find(Course.class, id);
	}

	@Override
	public Course save(Course course) throws SchoolDatabaseException {
		try {
			return entityManager.merge(course);
		} catch (PersistenceException e) {
			throw new SchoolDatabaseException("Error while saving Course: "+course, e); 
		}
	}

	@Override
	public Course delete(long id) {
		Course course = entityManager.find(Course.class, id);
		if (course != null)
			entityManager.remove(course);
		return course;
	}
	

}
