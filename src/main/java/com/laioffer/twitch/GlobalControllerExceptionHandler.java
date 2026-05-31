package com.laioffer.twitch;

import com.laioffer.twitch.model.TwitchErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<TwitchErrorResponse> handleDefaultException(Exception e) {
    // Log full detail server-side; never expose internals (exception class, message,
    // stack) to clients -- that is an information-disclosure risk.
    logger.error("Unhandled exception", e);
    return new ResponseEntity<>(
        new TwitchErrorResponse(
            "Something went wrong, please try again later.",
            "INTERNAL_SERVER_ERROR",
            null
        ),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(ResponseStatusException.class)
  public final ResponseEntity<TwitchErrorResponse> handleResponseStatusException(
      ResponseStatusException e) {
    // 4xx responses are intentional and client-facing: surface the human-readable
    // reason, but not internal cause class names. Guard against a null cause
    // (a bare ResponseStatusException has none) to avoid an NPE in the handler.
    Throwable cause = e.getCause();
    if (cause != null) {
      logger.warn("Request failed ({}): {}", e.getStatusCode(), e.getReason(), cause);
    }
    return new ResponseEntity<>(
        new TwitchErrorResponse(
            e.getReason() != null ? e.getReason() : "Request failed",
            e.getStatusCode().toString(),
            null
        ),
        e.getStatusCode()
    );
  }
}
