package br.com.alura.AluraFake.user.application.port.out;

public interface ExistsUserByEmailPort {

    Boolean existsByEmail(String email);
}
