package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesByInstructorIdPort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.user.application.port.out.FindUserByIdPort;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import br.com.alura.AluraFake.user.domain.exception.UserNotInstructorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindInstructorCoursesReportServiceTest {

    @Mock
    private FindUserByIdPort findUserByIdPort;

    @Mock
    private FindAllCoursesByInstructorIdPort findAllCoursesByInstructorIdPort;

    @InjectMocks
    private FindInstructorCoursesReportService service;

    private User instructor;
    private User student;
    private final Long instructorId = 1L;
    private final Long studentId = 2L;

    @BeforeEach
    void setUp() {
        instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        student = new User("João", "joao@alura.com.br", Role.STUDENT);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user does not exist")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        when(findUserByIdPort.findById(999L)).thenThrow(new ResourceNotFoundException("User", 999L));

        assertThrows(ResourceNotFoundException.class, () -> service.findInstructorCoursesReport(999L));

        verify(findUserByIdPort).findById(999L);
    }

    @Test
    @DisplayName("Should throw UserNotInstructorException when user is not an instructor")
    void shouldThrowUserNotInstructorExceptionWhenUserIsNotInstructor() {
        when(findUserByIdPort.findById(studentId)).thenReturn(student);

        assertThrows(UserNotInstructorException.class, () -> service.findInstructorCoursesReport(studentId));

        verify(findUserByIdPort).findById(studentId);
    }

    @Test
    @DisplayName("Should return empty list when instructor has no courses")
    void shouldReturnEmptyListWhenInstructorHasNoCourses() {
        when(findUserByIdPort.findById(instructorId)).thenReturn(instructor);
        when(findAllCoursesByInstructorIdPort.findCourseByInstructorId(instructorId)).thenReturn(Collections.emptyList());

        List<Course> courses = service.findInstructorCoursesReport(instructorId);

        assertThat(courses).isEmpty();
        
        verify(findUserByIdPort).findById(instructorId);
        verify(findAllCoursesByInstructorIdPort).findCourseByInstructorId(instructorId);
    }

    @Test
    @DisplayName("Should successfully return list of courses for valid instructor")
    void shouldSuccessfullyReturnListOfCoursesForValidInstructor() {
        Course course1 = new Course("Java", "Curso de Java", instructor);
        Course course2 = new Course("Spring", "Curso de Spring", instructor);
        Course course3 = new Course("Hibernate", "Curso de Hibernate", instructor);

        List<Course> expectedCourses = Arrays.asList(course1, course2, course3);

        when(findUserByIdPort.findById(instructorId)).thenReturn(instructor);
        when(findAllCoursesByInstructorIdPort.findCourseByInstructorId(instructorId)).thenReturn(expectedCourses);

        List<Course> courses = service.findInstructorCoursesReport(instructorId);

        assertThat(courses).hasSize(3);
        assertThat(courses).containsExactly(course1, course2, course3);
        
        verify(findUserByIdPort).findById(instructorId);
        verify(findAllCoursesByInstructorIdPort).findCourseByInstructorId(instructorId);
    }
}
