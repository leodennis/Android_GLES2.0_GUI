package ldkproductions.com.example.exceptions;

/**
 * Created by Leo on 02.12.2015.
 */
public class FontNotSetException extends Exception {
    private final static String message = "Font not set and no default font available!";
    public FontNotSetException() { super(message); }
    public FontNotSetException(String m) { super(message + " " + m); }
    public FontNotSetException(String m, Throwable cause) { super(message + " " + m, cause); }
    public FontNotSetException(Throwable cause) { super(message, cause); }
}
