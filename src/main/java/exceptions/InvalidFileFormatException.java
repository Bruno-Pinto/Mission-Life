package exceptions;

public class InvalidFileFormatException extends RuntimeException {
    public InvalidFileFormatException(String message) {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause) { super(message, cause); }

    public InvalidFileFormatException(String message, String filePath, Throwable cause) { super(message + " at: " + filePath, cause); }
}
