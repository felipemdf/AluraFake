package br.com.alura.AluraFake.user.application.port.out;

import br.com.alura.AluraFake.user.domain.User;

import java.util.Optional;

public interface FindUserByEmailPort {

    Optional<User> findByEmail(String email);
}
