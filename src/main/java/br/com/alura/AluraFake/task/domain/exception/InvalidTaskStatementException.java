package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class InvalidTaskStatementException extends DomainException {

    public InvalidTaskStatementException() {

        super("Statement must be between 4 and 255 characters");
    }
    
    public InvalidTaskStatementException(String message) {

        super(message);
    }
}
