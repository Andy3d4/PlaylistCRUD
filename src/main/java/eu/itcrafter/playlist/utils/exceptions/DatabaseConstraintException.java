package eu.itcrafter.playlist.utils.exceptions;

public class DatabaseConstraintException extends RuntimeException {
    public DatabaseConstraintException(String errorMessage) {
        super(errorMessage);
    }
}

