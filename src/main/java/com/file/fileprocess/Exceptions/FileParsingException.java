package com.file.fileprocess.Exceptions;

public class FileParsingException
        extends RuntimeException {

    public FileParsingException(
            String message,
            Throwable ex) {

        super(message, ex);
    }
}