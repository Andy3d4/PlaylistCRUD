package eu.itcrafter.playlist.infrastructure.rest.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {
    SOME_ERROR_ENUM("Some error message");

    private final String message;
}
