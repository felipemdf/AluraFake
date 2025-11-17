package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class InvalidTaskOptionsException extends DomainException {
    
    public InvalidTaskOptionsException(String message) {
        super(message);
    }
    
    public static InvalidTaskOptionsException invalidLength() {

        return new InvalidTaskOptionsException("Each option must be between 4 and 80 characters");
    }
    
    public static InvalidTaskOptionsException nonUniqueOptions() {

        return new InvalidTaskOptionsException("All options must be unique");
    }
    
    public static InvalidTaskOptionsException optionEqualsStatement() {

        return new InvalidTaskOptionsException("Options cannot be equal to the statement");
    }
}
