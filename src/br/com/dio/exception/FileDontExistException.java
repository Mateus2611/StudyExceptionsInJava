package br.com.dio.exception;

import java.io.IOException;

public class FileDontExistException extends IOException {

    public FileDontExistException(final String message) {
        super(message);
    }
}
