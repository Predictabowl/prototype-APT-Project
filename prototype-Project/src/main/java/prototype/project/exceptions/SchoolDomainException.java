package prototype.project.exceptions;

public class SchoolDomainException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchoolDomainException() {
		super();
	}
	
	public SchoolDomainException(Throwable e) {
		super(e);
	}
	
	public SchoolDomainException(String message, Throwable e) {
		super(message,e);
	}
	
}
