package br.com.alura.AluraFake.user.application.port.out;

public interface UserExistByEmail {

    Boolean existsByEmail(String email);
}
