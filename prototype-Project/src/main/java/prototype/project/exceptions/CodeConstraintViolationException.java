package prototype.project.exceptions;

public class CodeConstraintViolationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CodeConstraintViolationException() {
		super();
	}
	
	public CodeConstraintViolationException(Throwable e) {
		super(e);
	}
	
	public CodeConstraintViolationException(String message, Throwable e) {
		super(message,e);
	}
	
}
