// package com.se.user_service.exception;

// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.context.request.WebRequest;

// @RestControllerAdvice
// public class TokenControllerAdvice {
//     @ExceptionHandler(value = TokenRefreshException.class)
//   @ResponseStatus(HttpStatus.FORBIDDEN)
//   public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
//     return new ErrorMessage(
//         HttpStatus.FORBIDDEN.value(),
//         new Date(),
//         ex.getMessage(),
//         request.getDescription(false));
//   }
// }
