package br.com.alura.AluraFake.shared.domain.exception;

public class ResourceNotFoundException extends DomainException{
    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s with id %s was not found", resource, id));
    }
}
