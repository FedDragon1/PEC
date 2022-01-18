package pec.util;

public class IllegalFileCountException extends Exception{
	private static final long serialVersionUID = -6147957199653228238L;
	
	/**
	 * Constructs an {@code IllegalFileCountException} with the specified detail message.
	 * 
	 * @param message
	 * 		The detail message (which is saved for later retrieval
	 * 		by the {@link #getMessage()} method)
	 * 
	 */
	public IllegalFileCountException(String message) {
		super(message);
	}
}
