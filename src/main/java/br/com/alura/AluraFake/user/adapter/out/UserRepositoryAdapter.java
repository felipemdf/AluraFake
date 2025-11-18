package br.com.alura.AluraFake.user.adapter.out;

import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.user.application.port.out.UserExistByEmail;
import br.com.alura.AluraFake.user.application.port.out.FindUserByEmailPort;
import br.com.alura.AluraFake.user.application.port.out.FindUserByIdPort;
import br.com.alura.AluraFake.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserExistRepositoryAdapter implements FindUserByEmailPort, FindUserByIdPort, UserExistByEmail {

    private final UserRepository repository;

    public UserExistRepositoryAdapter(UserRepository repository) {

        this.repository = repository;
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
