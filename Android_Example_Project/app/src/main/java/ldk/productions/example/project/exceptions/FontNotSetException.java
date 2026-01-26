package ldk.productions.example.project.exceptions;

/**
 * Created by Leo on 02.12.2015.
 */
public class FontNotSetException extends RuntimeException {
    private final static String message = "Font not set and no default font available!";

    public FontNotSetException() {
        super(message);
    }

}
