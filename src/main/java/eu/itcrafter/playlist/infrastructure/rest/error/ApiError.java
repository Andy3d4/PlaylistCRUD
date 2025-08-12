package eu.itcrafter.playlist.infrastructure.rest.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private String path;
}

