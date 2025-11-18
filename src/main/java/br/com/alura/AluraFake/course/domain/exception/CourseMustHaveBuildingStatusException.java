package br.com.alura.AluraFake.course.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class CourseMustHaveBuildingStatusException extends DomainException {

    public CourseMustHaveBuildingStatusException() {

        super("Course is not in BUILDING status");
    }
}
