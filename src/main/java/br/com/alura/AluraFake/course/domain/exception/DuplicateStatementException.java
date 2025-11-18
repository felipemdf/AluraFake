package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class DuplicateStatementException extends DomainException {

    public DuplicateStatementException() {

        super("Duplicate task statement in the course");

    }
}
