package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class UserMustBeAnInstructorException extends DomainException {

    public UserMustBeAnInstructorException() {

        super("User must be an instructor");

    }
}
