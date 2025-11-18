package br.com.alura.AluraFake.task.application.service;

import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.Type;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskServiceTest {

    @Mock
    private FindCourseByIdPort findCourseByIdPort;

    @Mock
    private SaveCoursePort saveCoursePort;

    @InjectMocks
    private CreateTaskService createTaskService;

    private Course course;
    private final Long courseId = 1L;

    @BeforeEach
    void setUp() {
        User instructor = new User("João Silva", "joao@alura.com", Role.INSTRUCTOR);
        course = new Course("Programação Java", "programacao-java", instructor);
    }

    @Nested
    @DisplayName("Create Open Text Task")
    class CreateOpenTextTask {

        @Test
        @DisplayName("Should create open text task successfully")
        void shouldCreateOpenTextTaskSuccessfully() {
            String statement = "Qual é sua linguagem de programação favorita?";
            Integer order = 1;

            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
            when(saveCoursePort.save(course)).thenReturn(course);

            Task task = createTaskService.createOpenTextTask(courseId, statement, order);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("Qual é sua linguagem de programação favorita?");
            assertThat(task.getOrder()).isEqualTo(1);
            assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
        }

    }

    @Nested
    @DisplayName("Create Single Choice Task")
    class CreateSingleChoiceTask {

        @Test
        @DisplayName("Should create single choice task successfully")
        void shouldCreateSingleChoiceTaskSuccessfully() {
            String statement = "Qual é a capital do Brasil?";
            Integer order = 1;
            List<NewTaskOptionDTO> options = List.of(
                    new NewTaskOptionDTO("Brasília", true),
                    new NewTaskOptionDTO("São Paulo", false),
                    new NewTaskOptionDTO("Rio de Janeiro", false)
            );

            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
            when(saveCoursePort.save(course)).thenReturn(course);

            Task task = createTaskService.createSingleChoiceTask(courseId, statement, order, options);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("Qual é a capital do Brasil?");
            assertThat(task.getOrder()).isEqualTo(1);
            assertThat(task.getType()).isEqualTo(Type.SINGLE_CHOICE);
            assertThat(task.getOptions().size()).isEqualTo(3);

            assertThat(task.getOptions().get(0).getOption()).isEqualTo("Brasília");
            assertThat(task.getOptions().get(1).getOption()).isEqualTo("São Paulo");
            assertThat(task.getOptions().get(2).getOption()).isEqualTo("Rio de Janeiro");

            assertThat(task.getOptions().get(0).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(1).isCorrect()).isEqualTo(false);
            assertThat(task.getOptions().get(2).isCorrect()).isEqualTo(false);

        }
    }

    @Nested
    @DisplayName("Create Multiple Choice Task")
    class CreateMultipleChoiceTask {

        @Test
        @DisplayName("Should create multiple choice task successfully")
        void shouldCreateMultipleChoiceTaskSuccessfully() {
            String statement = "Quais são linguagens de programação?";
            Integer order = 1;
            List<NewTaskOptionDTO> options = List.of(
                    new NewTaskOptionDTO("Java", true),
                    new NewTaskOptionDTO("Python", true),
                    new NewTaskOptionDTO("Marcação HTML", false),
                    new NewTaskOptionDTO("Estilo CSS", false)
            );

            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
            when(saveCoursePort.save(course)).thenReturn(course);

            Task task = createTaskService.createMultipleChoiceTask(courseId, statement, order, options);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("Quais são linguagens de programação?");
            assertThat(task.getOrder()).isEqualTo(1);
            assertThat(task.getType()).isEqualTo(Type.MULTIPLE_CHOICE);
            assertThat(task.getOptions().size()).isEqualTo(4);

            assertThat(task.getOptions().get(0).getOption()).isEqualTo("Java");
            assertThat(task.getOptions().get(1).getOption()).isEqualTo("Python");
            assertThat(task.getOptions().get(2).getOption()).isEqualTo("Marcação HTML");
            assertThat(task.getOptions().get(3).getOption()).isEqualTo("Estilo CSS");

            assertThat(task.getOptions().get(0).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(1).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(2).isCorrect()).isEqualTo(false);
            assertThat(task.getOptions().get(3).isCorrect()).isEqualTo(false);
        }
    }
}
