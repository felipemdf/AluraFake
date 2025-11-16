package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class InvalidOptionsCountException extends DomainException {
    
    public InvalidOptionsCountException(String message) { super(message); }
    
    public static InvalidOptionsCountException singleChoice() {

        return new InvalidOptionsCountException("Single choice task must have between 2 and 5 options");
    }
    
    public static InvalidOptionsCountException multipleChoice() {

        return new InvalidOptionsCountException("Multiple choice task must have between 3 and 5 options");
    }
}
