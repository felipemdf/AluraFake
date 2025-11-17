package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class TaskNotFoundException extends DomainException {
    public TaskNotFoundException(String statement) {
        super("Task with statement '" + statement + "' not found");
    }
}
