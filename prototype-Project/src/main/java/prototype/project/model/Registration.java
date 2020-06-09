package prototype.project.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(RegistrationId.class)
public class Registration {
	
	@Id
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private Student student;

	@Id
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private Course course;

	private boolean paid;
	
	public Registration() {
		// TODO Auto-generated constructor stub
	}

	public Registration(Student student, Course course, boolean paid) {
		super();
		this.student = student;
		this.course = course;
		this.paid = paid;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + (paid ? 1231 : 1237);
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
		Registration other = (Registration) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (paid != other.paid)
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
		return "Registration [student=" + student + ", course=" + course + ", paid=" + paid + "]";
	}
	
	
}
