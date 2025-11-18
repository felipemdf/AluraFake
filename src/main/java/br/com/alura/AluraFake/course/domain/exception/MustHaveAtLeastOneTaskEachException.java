package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class MustHaveAtLeastOneTaskEachException extends DomainException {

    public MustHaveAtLeastOneTaskEachException() {

        super("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");

    }
}
