package br.ada.customer.crud.exceptions;

public class OrderException extends RuntimeException {

    public OrderException(String message, Exception cause) {
        super(message, cause);
    }

}
