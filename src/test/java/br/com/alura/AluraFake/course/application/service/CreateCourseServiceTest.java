package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.exception.UserMustBeAnInstructorException;
import br.com.alura.AluraFake.user.application.port.out.FindUserByEmailPort;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCourseServiceTest {

    @Mock
    private FindCourseByIdPort findCourseByIdPort;

    @Mock
    private SaveCoursePort saveCoursePort;

    @Mock
    private FindUserByEmailPort findUserByEmailPort;

    @InjectMocks
    private CreateCourseService createCourseService;


    @Test
    void should_create_course_when_instructor_email_is_valid() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);

        when(findUserByEmailPort.findByEmail("paulo@alura.com.br")).thenReturn(Optional.of(instructor));
        when(saveCoursePort.save(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            return course;
        });

        createCourseService.create(newCourseDTO);

        verify(findUserByEmailPort).findByEmail("paulo@alura.com.br");
        verify(saveCoursePort).save(argThat(course ->
                course.getTitle().equals("Java") &&
                        course.getDescription().equals("Curso de Java") &&
                        course.getInstructor().equals(instructor)
        ));
    }

    @Test
    void should_throw_exception_when_user_email_not_found() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("inexistente@alura.com.br");

        when(findUserByEmailPort.findByEmail("inexistente@alura.com.br")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createCourseService.create(newCourseDTO))
                .isInstanceOf(UserMustBeAnInstructorException.class);

        verify(findUserByEmailPort).findByEmail("inexistente@alura.com.br");
        verify(saveCoursePort, never()).save(any(Course.class));
    }

    @Test
    void should_throw_exception_when_user_is_not_instructor() {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("student@alura.com.br");

        User student = new User("Diogo", "student@alura.com.br", Role.STUDENT);

        when(findUserByEmailPort.findByEmail("student@alura.com.br")).thenReturn(Optional.of(student));

        assertThatThrownBy(() -> createCourseService.create(newCourseDTO))
                .isInstanceOf(UserMustBeAnInstructorException.class);

        verify(findUserByEmailPort).findByEmail("student@alura.com.br");
        verify(saveCoursePort, never()).save(any(Course.class));
    }
}
