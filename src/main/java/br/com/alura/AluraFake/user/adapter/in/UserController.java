package br.com.alura.AluraFake.user.adapter.in;

import br.com.alura.AluraFake.user.adapter.in.dto.NewUserDTO;
import br.com.alura.AluraFake.user.adapter.in.dto.UserListItemDTO;
import br.com.alura.AluraFake.user.application.port.in.CreateUserUseCase;
import br.com.alura.AluraFake.user.application.port.out.FindAllUsersPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final FindAllUsersPort findAllUsersPort;
    private final CreateUserUseCase createUserUseCase;


    public UserController(FindAllUsersPort findAllUsersPort, CreateUserUseCase createUserUseCase) {

        this.findAllUsersPort = findAllUsersPort;
        this.createUserUseCase = createUserUseCase;
    }

    @Transactional
    @PostMapping("/user/new")
    public ResponseEntity newStudent(@RequestBody @Valid NewUserDTO newUser) {
        createUserUseCase.create(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/user/all")
    public List<UserListItemDTO> listAllUsers() {
        return findAllUsersPort.findAll().stream().map(UserListItemDTO::new).toList();
    }

}
