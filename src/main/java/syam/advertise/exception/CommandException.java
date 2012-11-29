/**
 * Advertise - Package: syam.advertise.exception
 * Created: 2012/11/30 6:28:38
 */
package syam.advertise.exception;

/**
 * CommandException (CommandException.java)
 * 
 * @author syam(syamn)
 */
public class CommandException extends Exception {
    private static final long serialVersionUID = -7254933911300056250L;

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
