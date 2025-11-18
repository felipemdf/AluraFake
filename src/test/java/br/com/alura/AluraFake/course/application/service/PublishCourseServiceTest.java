package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.Status;
import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.Type;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishCourseServiceTest {

    @Mock
    private FindCourseByIdPort findCourseByIdPort;

    @InjectMocks
    private PublishCourseService publishCourseService;

    @Test
    void should_publish_course_successfully_when_course_has_all_task_types() {
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);

        Long courseId = 1L;
        Course course = new Course("Java", "Curso de Java", instructor);

        Task openTextTask = new Task("Explique os principais conceitos do Java", 1, Type.OPEN_TEXT);
        Task singleChoiceTask = new Task("O que é Java?", 2, Type.SINGLE_CHOICE);
        Task multipleChoiceTask = new Task("Selecione as funcionalidades do Java", 3, Type.MULTIPLE_CHOICE);
        
        course.addTask(openTextTask);
        course.addTask(singleChoiceTask);
        course.addTask(multipleChoiceTask);

        when(findCourseByIdPort.findById(courseId)).thenReturn(course);

        publishCourseService.publish(courseId);

        assertThat(course.getStatus()).isEqualTo(Status.PUBLISHED);
        assertThat(course.getPublishedAt()).isNotNull();

        verify(findCourseByIdPort).findById(courseId);
    }
}
