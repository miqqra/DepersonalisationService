package ru.nsu.backend.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseException extends Exception {
    public final HttpStatus httpStatus;
    public final String reason;

    public ResponseException(HttpStatus httpStatus) {
        this(httpStatus, "Some error");
    }

    public ResponseException(String reason) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }

    public ResponseException(HttpStatus httpStatus, String reason) {
        this.httpStatus = httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus;
        this.reason = reason == null ? "Some error" : reason;
    }

    public ResponseEntity<?> response() {
        return ResponseEntity.status(httpStatus.value()).body(new Response(httpStatus, reason));
    }

    public ResponseEntity<?> responseWithJSON() {
        return ResponseEntity.status(httpStatus.value()).body(this);
    }
    public static void throwResponse(HttpStatus httpStatus, String reason) throws ResponseException {
        throw new ResponseException(httpStatus, reason);
    }
    @Data
    @AllArgsConstructor
    static public class Response {
        private HttpStatus status;
        private String message;
    }
}




