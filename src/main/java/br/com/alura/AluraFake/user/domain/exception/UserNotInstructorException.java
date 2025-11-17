package br.com.alura.AluraFake.user.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;

public class UserNotInstructorException extends DomainException {
    public UserNotInstructorException(Long userId) {
        super("User with id " + userId + " is not an instructor");
    }
}
