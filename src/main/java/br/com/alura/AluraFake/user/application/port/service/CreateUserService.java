package br.com.alura.AluraFake.user.application.port.service;

import br.com.alura.AluraFake.user.domain.exception.EmailAlreadExistsException;
import br.com.alura.AluraFake.user.adapter.in.dto.NewUserDTO;
import br.com.alura.AluraFake.user.application.port.in.CreateUserUseCase;
import br.com.alura.AluraFake.user.application.port.out.ExistsUserByEmailPort;
import br.com.alura.AluraFake.user.application.port.out.SaveUserPort;
import br.com.alura.AluraFake.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserService implements CreateUserUseCase {

    private final ExistsUserByEmailPort existsUserByEmailPort;
    private final SaveUserPort saveUserPort;

    public CreateUserService(ExistsUserByEmailPort existsUserByEmailPort, SaveUserPort saveUserPort) {

        this.existsUserByEmailPort = existsUserByEmailPort;
        this.saveUserPort = saveUserPort;
    }

    @Override
    public User create(NewUserDTO newUser) {

        if(existsUserByEmailPort.existsByEmail(newUser.getEmail())) {
            throw new EmailAlreadExistsException();
        }

        User user = newUser.toModel();
        return saveUserPort.save(user);
    }

}
