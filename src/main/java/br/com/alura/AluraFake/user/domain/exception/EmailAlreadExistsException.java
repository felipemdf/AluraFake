package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class EmailAlreadExistsException extends DomainException {

    public EmailAlreadExistsException() {

        super("Email already exists");

    }
}
