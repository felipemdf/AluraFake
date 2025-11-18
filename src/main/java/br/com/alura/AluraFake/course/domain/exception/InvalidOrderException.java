package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class InvalidOrderException extends DomainException {

    public InvalidOrderException() {

        super("Task order must be sequential");

    }
}
