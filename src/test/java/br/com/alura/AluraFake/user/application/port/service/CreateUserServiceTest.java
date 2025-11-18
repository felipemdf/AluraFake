package br.com.alura.AluraFake.user.application.port.service;

import br.com.alura.AluraFake.user.adapter.in.dto.NewUserDTO;
import br.com.alura.AluraFake.user.application.port.out.ExistsUserByEmailPort;
import br.com.alura.AluraFake.user.application.port.out.SaveUserPort;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import br.com.alura.AluraFake.user.domain.exception.EmailAlreadExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private ExistsUserByEmailPort existsUserByEmailPort;

    @Mock
    private SaveUserPort saveUserPort;

    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        createUserService = new CreateUserService(
                existsUserByEmailPort,
                saveUserPort
        );
    }

    @Test
    void should_create_user_when_email_is_unique() {
        // Given
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("João Silva");
        newUserDTO.setEmail("joao.silva@alura.com.br");
        newUserDTO.setRole(Role.STUDENT);

        User expectedUser = new User("João Silva", "joao.silva@alura.com.br", Role.STUDENT);

        when(existsUserByEmailPort.existsByEmail("joao.silva@alura.com.br"))
                .thenReturn(false);
        when(saveUserPort.save(any(User.class)))
                .thenReturn(expectedUser);

        // When
        User result = createUserService.create(newUserDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("João Silva");
        assertThat(result.getEmail()).isEqualTo("joao.silva@alura.com.br");
        assertThat(result.getRole()).isEqualTo(Role.STUDENT);

        verify(existsUserByEmailPort).existsByEmail("joao.silva@alura.com.br");
        verify(saveUserPort).save(any(User.class));
    }

    @Test
    void should_create_instructor_user_successfully() {
        // Given
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("Paulo Instructor");
        newUserDTO.setEmail("paulo@alura.com.br");
        newUserDTO.setRole(Role.INSTRUCTOR);

        User expectedUser = new User("Paulo Instructor", "paulo@alura.com.br", Role.INSTRUCTOR);

        when(existsUserByEmailPort.existsByEmail("paulo@alura.com.br"))
                .thenReturn(false);
        when(saveUserPort.save(any(User.class)))
                .thenReturn(expectedUser);

        // When
        User result = createUserService.create(newUserDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Paulo Instructor");
        assertThat(result.getEmail()).isEqualTo("paulo@alura.com.br");
        assertThat(result.getRole()).isEqualTo(Role.INSTRUCTOR);

        verify(existsUserByEmailPort).existsByEmail("paulo@alura.com.br");
        verify(saveUserPort).save(any(User.class));
    }

    @Test
    void should_throw_exception_when_email_already_exists() {
        // Given
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("Maria Santos");
        newUserDTO.setEmail("maria.santos@alura.com.br");
        newUserDTO.setRole(Role.STUDENT);

        when(existsUserByEmailPort.existsByEmail("maria.santos@alura.com.br"))
                .thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> createUserService.create(newUserDTO))
                .isInstanceOf(EmailAlreadExistsException.class);

        verify(existsUserByEmailPort).existsByEmail("maria.santos@alura.com.br");
        verify(saveUserPort, never()).save(any(User.class));
    }

    @Test
    void should_call_save_user_port_with_correct_parameters() {
        // Given
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("Ana Costa");
        newUserDTO.setEmail("ana.costa@alura.com.br");
        newUserDTO.setRole(Role.STUDENT);

        User savedUser = new User("Ana Costa", "ana.costa@alura.com.br", Role.STUDENT);

        when(existsUserByEmailPort.existsByEmail("ana.costa@alura.com.br"))
                .thenReturn(false);
        when(saveUserPort.save(any(User.class)))
                .thenReturn(savedUser);

        // When
        User result = createUserService.create(newUserDTO);

        // Then
        verify(saveUserPort).save(argThat(user ->
                user.getName().equals("Ana Costa") &&
                user.getEmail().equals("ana.costa@alura.com.br") &&
                user.getRole().equals(Role.STUDENT)
        ));

        assertThat(result).isEqualTo(savedUser);
    }

    @Test
    void should_verify_email_exists_before_saving() {
        // Given
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("Carlos Lima");
        newUserDTO.setEmail("carlos.lima@alura.com.br");
        newUserDTO.setRole(Role.INSTRUCTOR);

        when(existsUserByEmailPort.existsByEmail("carlos.lima@alura.com.br"))
                .thenReturn(false);

        User savedUser = new User("Carlos Lima", "carlos.lima@alura.com.br", Role.INSTRUCTOR);
        when(saveUserPort.save(any(User.class)))
                .thenReturn(savedUser);

        // When
        createUserService.create(newUserDTO);

        // Then
        // Verify that email check is called before save
        var inOrder = inOrder(existsUserByEmailPort, saveUserPort);
        inOrder.verify(existsUserByEmailPort).existsByEmail("carlos.lima@alura.com.br");
        inOrder.verify(saveUserPort).save(any(User.class));
    }
}
