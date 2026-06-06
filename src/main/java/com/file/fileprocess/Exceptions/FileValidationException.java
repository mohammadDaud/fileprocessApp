package com.file.fileprocess.Exceptions;

public class FileValidationException
        extends RuntimeException {

    public FileValidationException(
            String message) {

        super(message);
    }
}