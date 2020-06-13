package prototype.project.model.id;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class RegistrationId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long student;
	private Long course;

	public RegistrationId() {
		// TODO Auto-generated constructor stub
	}

	public RegistrationId(Long student, Long course) {
		super();
		this.student = student;
		this.course = course;
	}

	public Long getStudent() {
		return student;
	}

	public void setStudent(Long student) {
		this.student = student;
	}

	public Long getCourse() {
		return course;
	}

	public void setCourse(Long course) {
		this.course = course;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((student == null) ? 0 : student.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrationId other = (RegistrationId) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (student == null) {
			if (other.student != null)
				return false;
		} else if (!student.equals(other.student))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegistrationId [student=" + student + ", course=" + course + "]";
	}
	

}
