package com.file.fileprocess.Exceptions;

public class FileDatabaseException
        extends RuntimeException {

    public FileDatabaseException(
            String message,
            Throwable ex) {

        super(message, ex);
    }
}