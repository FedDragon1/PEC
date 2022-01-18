package pec.core;

/**
 * Exception is thrown when user specified an invalid input.
 * 
 * @author 乐观的辣条
 * @since alpha 1.0.0
 */
public class InvalidInputException extends Exception {
	private static final long serialVersionUID = -5295589308130645034L;

	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param msg Specified message
	 */
	public InvalidInputException (String msg) {
		super(msg);
	}
}
