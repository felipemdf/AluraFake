package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class InvalidTaskOrderException extends DomainException {

    public InvalidTaskOrderException() {

        super("Order must be a positive integer");
    }
    
    public InvalidTaskOrderException(String message) {

        super(message);
    }
}
