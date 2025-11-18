package br.com.alura.AluraFake.user.application.port.out;

import br.com.alura.AluraFake.user.domain.User;

import java.util.List;

public interface FindAllUsersPort {

    List<User> findAll();
}
