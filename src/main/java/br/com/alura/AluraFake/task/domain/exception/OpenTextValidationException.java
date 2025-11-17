package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class OpenTextValidationException extends DomainException {

    public OpenTextValidationException() {

        super("Open text tasks do not require option validation");
    }
}
