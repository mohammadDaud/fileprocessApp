package com.file.fileprocess.Exceptions;

import com.file.fileprocess.dtos.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            FileValidationException.class)
    public ResponseEntity<ApiResponseDTO>
    validation(
            FileValidationException ex) {

        return ResponseEntity.badRequest()
                .body(
                        ApiResponseDTO.builder()
                                .success(false)
                                .message(
                                        ex.getMessage())
                                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO>
    generic(
            Exception ex) {

        return ResponseEntity
                .internalServerError()
                .body(
                        ApiResponseDTO.builder()
                                .success(false)
                                .message(
                                        ex.getMessage())
                                .build());
    }
}