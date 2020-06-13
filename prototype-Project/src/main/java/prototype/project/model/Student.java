package prototype.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id = null;
	
	@Column(unique = true, updatable = false)
	private String code;
	
	private String name;
	
	@OneToMany (mappedBy = "student",cascade = CascadeType.ALL, orphanRemoval = true)
//	@OneToMany (mappedBy = "student")
//	@OneToMany
//	@JoinTable
	private Set<Registration> registrations = new HashSet<>();
	
	public Student() {
	}

	public Student(String code, String name, Set<Registration> registrations) {
		this.code = code;
		this.name = name;
		this.registrations = registrations;
	}
	
	public Student(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Registration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(Set<Registration> registrations) {
		this.registrations = registrations;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Student other = (Student) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", registrations=" + registrations + "]";
	}

	
}
