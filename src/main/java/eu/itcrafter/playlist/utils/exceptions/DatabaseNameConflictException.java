package eu.itcrafter.playlist.utils.exceptions;

public class DatabaseNameConflictException extends RuntimeException {
    public DatabaseNameConflictException(String errorMessage) {
        super(errorMessage);
    }
}
