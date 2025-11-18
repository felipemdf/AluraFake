package br.com.alura.AluraFake.user.adapter.out;

import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.user.application.port.out.*;
import br.com.alura.AluraFake.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements FindUserByEmailPort, FindUserByIdPort, ExistsUserByEmailPort, SaveUserPort, FindAllUsersPort {

    private final UserRepository repository;

    public UserRepositoryAdapter(UserRepository repository) {

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

    @Override
    public User save(User user) {

        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }
}
