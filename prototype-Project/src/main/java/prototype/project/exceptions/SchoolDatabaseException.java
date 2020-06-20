package prototype.project.exceptions;

public class SchoolDatabaseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchoolDatabaseException() {
		super();
	}
	
	public SchoolDatabaseException(Throwable e) {
		super(e);
	}
	
	public SchoolDatabaseException(String message, Throwable e) {
		super(message,e);
	}
	
}
