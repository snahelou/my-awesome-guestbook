package net.devlab722.guestbook.server.errors;

public class MessageFormatException extends RuntimeException {
    public MessageFormatException(String message) {
        super(message);
    }

    public MessageFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
