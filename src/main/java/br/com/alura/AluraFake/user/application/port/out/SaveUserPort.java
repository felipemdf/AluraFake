package br.com.alura.AluraFake.user.application.port.out;

import br.com.alura.AluraFake.user.domain.User;

public interface SaveUserPort {

    User save(User user);
}
