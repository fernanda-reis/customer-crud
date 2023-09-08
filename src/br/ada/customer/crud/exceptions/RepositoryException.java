package br.ada.customer.crud.exceptions;

public class RepositoryException extends RuntimeException {

    public RepositoryException(String message, Exception cause) {
        super(message, cause);
    }

}
