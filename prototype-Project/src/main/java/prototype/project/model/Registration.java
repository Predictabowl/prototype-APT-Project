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
	private Student studentId;

	@Id
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private Course courseId;

	private boolean paid;
	
	public Registration() {
		// TODO Auto-generated constructor stub
	}

	public Registration(Student student, Course course, boolean paid) {
		super();
		this.studentId = student;
		this.courseId = course;
		this.paid = paid;
	}

	public Student getStudent() {
		return studentId;
	}

	public void setStudent(Student student) {
		this.studentId = student;
	}

	public Course getCourse() {
		return courseId;
	}

	public void setCourse(Course course) {
		this.courseId = course;
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
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + (paid ? 1231 : 1237);
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
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
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (paid != other.paid)
			return false;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Registration [student=" + studentId + ", course=" + courseId + ", paid=" + paid + "]";
	}


}
