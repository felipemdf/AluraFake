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
        User instructor = new User("John Doe", "john@alura.com", Role.INSTRUCTOR);
        course = new Course("Java Programming", "java-programming", instructor);
    }

    @Nested
    @DisplayName("Create Open Text Task")
    class CreateOpenTextTask {

        @Test
        @DisplayName("Should create open text task successfully")
        void shouldCreateOpenTextTaskSuccessfully() {
            String statement = "What is your favorite programming language?";
            Integer order = 1;
            
            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
            assertThat(course.getTasks()).hasSize(1);

            Task task = createTaskService.createOpenTextTask(courseId, statement, order);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("Hello World!");
            assertThat(task.getOrder()).isEqualTo(1);
            assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
        }

//        @Test
//        @DisplayName("Should fail when course not found")
//        void shouldFailWhenCourseNotFound() {
//            // Given
//            String statement = "What is your favorite programming language?";
//            Integer order = 1;
//
//            when(findCourseByIdPort.findById(courseId))
//                    .thenThrow(new ResourceNotFoundException("Course", courseId));
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createOpenTextTask(courseId, statement, order))
//                    .isInstanceOf(ResourceNotFoundException.class)
//                    .hasMessage("Course with id 1 was not found");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should fail when statement is invalid")
//        void shouldFailWhenStatementIsInvalid() {
//            // Given
//            String invalidStatement = "Hi"; // Too short
//            Integer order = 1;
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createOpenTextTask(courseId, invalidStatement, order))
//                    .isInstanceOf(InvalidTaskStatementException.class)
//                    .hasMessage("Statement must be between 4 and 255 characters");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should fail when order is invalid")
//        void shouldFailWhenOrderIsInvalid() {
//            // Given
//            String statement = "What is your favorite programming language?";
//            Integer invalidOrder = -1;
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createOpenTextTask(courseId, statement, invalidOrder))
//                    .isInstanceOf(InvalidTaskOrderException.class)
//                    .hasMessage("Order must be a positive integer");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
    }

    @Nested
    @DisplayName("Create Single Choice Task")
    class CreateSingleChoiceTask {

        @Test
        @DisplayName("Should create single choice task successfully")
        void shouldCreateSingleChoiceTaskSuccessfully() {
            // Given
            String statement = "What is the capital of Brazil?";
            Integer order = 2;
            List<NewTaskOptionDTO> options = List.of(
                new NewTaskOptionDTO("Brasília", true),
                new NewTaskOptionDTO("São Paulo", false),
                new NewTaskOptionDTO("Rio de Janeiro", false)
            );

            when(findCourseByIdPort.findById(courseId)).thenReturn(course);

            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
            when(saveCoursePort.save(course)).thenReturn(course);

            Task task = createTaskService.createSingleChoiceTask(courseId, statement, order, options);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("What is the capital of Brazil?");
            assertThat(task.getOrder()).isEqualTo(2);
            assertThat(task.getType()).isEqualTo(Type.SINGLE_CHOICE);
            assertThat(task.getOptions().size()).isEqualTo(3);

            assertThat(task.getOptions().get(0).getOption()).isEqualTo("Brasília");
            assertThat(task.getOptions().get(1).getOption()).isEqualTo("São Paulo");
            assertThat(task.getOptions().get(2).getOption()).isEqualTo("Rio de Janeiro");

            assertThat(task.getOptions().get(0).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(1).isCorrect()).isEqualTo(false);
            assertThat(task.getOptions().get(2).isCorrect()).isEqualTo(false);

        }

//        @Test
//        @DisplayName("Should fail with invalid single choice options")
//        void shouldFailWithInvalidSingleChoiceOptions() {
//            // Given
//            String statement = "What is the capital of Brazil?";
//            Integer order = 2;
//            List<NewTaskOptionDTO> invalidOptions = List.of(
//                new NewTaskOptionDTO("Brasília", true),
//                new NewTaskOptionDTO("São Paulo", true) // Two correct answers
//            );
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createSingleChoiceTask(courseId, statement, order, invalidOptions))
//                    .isInstanceOf(InvalidCorrectAnswersException.class)
//                    .hasMessage("Single choice task must have exactly one correct answer");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should fail with insufficient options")
//        void shouldFailWithInsufficientOptions() {
//            // Given
//            String statement = "What is the capital of Brazil?";
//            Integer order = 2;
//            List<NewTaskOptionDTO> insufficientOptions = List.of(
//                new NewTaskOptionDTO("Brasília", true) // Only one option
//            );
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createSingleChoiceTask(courseId, statement, order, insufficientOptions))
//                    .isInstanceOf(InvalidOptionsCountException.class)
//                    .hasMessage("Single choice task must have between 2 and 5 options");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
    }

    @Nested
    @DisplayName("Create Multiple Choice Task")
    class CreateMultipleChoiceTask {

        @Test
        @DisplayName("Should create multiple choice task successfully")
        void shouldCreateMultipleChoiceTaskSuccessfully() {
            // Given
            String statement = "Which are programming languages?";
            Integer order = 3;
            List<NewTaskOptionDTO> options = List.of(
                new NewTaskOptionDTO("Java", true),
                new NewTaskOptionDTO("Python", true),
                new NewTaskOptionDTO("HTML", false),
                new NewTaskOptionDTO("CSS", false)
            );
            
            when(findCourseByIdPort.findById(courseId)).thenReturn(course);

            Task task = createTaskService.createMultipleChoiceTask(courseId, statement, order, options);

            verify(findCourseByIdPort).findById(courseId);

            assertThat(course.getTasks()).hasSize(1);
            assertThat(task.getStatement()).isEqualTo("Which are programming languages?");
            assertThat(task.getOrder()).isEqualTo(3);
            assertThat(task.getType()).isEqualTo(Type.MULTIPLE_CHOICE);
            assertThat(task.getOptions().size()).isEqualTo(4);

            assertThat(task.getOptions().get(0).getOption()).isEqualTo("Java");
            assertThat(task.getOptions().get(1).getOption()).isEqualTo("Python");
            assertThat(task.getOptions().get(2).getOption()).isEqualTo("HTML");
            assertThat(task.getOptions().get(3).getOption()).isEqualTo("CSS");

            assertThat(task.getOptions().get(0).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(1).isCorrect()).isEqualTo(true);
            assertThat(task.getOptions().get(2).isCorrect()).isEqualTo(false);
            assertThat(task.getOptions().get(3).isCorrect()).isEqualTo(false);
        }

//        @Test
//        @DisplayName("Should fail with invalid multiple choice options")
//        void shouldFailWithInvalidMultipleChoiceOptions() {
//            // Given
//            String statement = "Which are programming languages?";
//            Integer order = 3;
//            List<NewTaskOptionDTO> invalidOptions = List.of(
//                new NewTaskOptionDTO("Java", true),
//                new NewTaskOptionDTO("HTML", false),
//                new NewTaskOptionDTO("CSS", false) // Only one correct answer
//            );
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createMultipleChoiceTask(courseId, statement, order, invalidOptions))
//                    .isInstanceOf(InvalidCorrectAnswersException.class)
//                    .hasMessage("Multiple choice task must have at least two correct answers");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
//
//        @Test
//        @DisplayName("Should fail with insufficient options for multiple choice")
//        void shouldFailWithInsufficientOptionsForMultipleChoice() {
//            // Given
//            String statement = "Which are programming languages?";
//            Integer order = 3;
//            List<NewTaskOptionDTO> insufficientOptions = List.of(
//                new NewTaskOptionDTO("Java", true),
//                new NewTaskOptionDTO("Python", true) // Only two options
//            );
//
//            when(findCourseByIdPort.findById(courseId)).thenReturn(course);
//
//            // When & Then
//            assertThatThrownBy(() ->
//                createTaskService.createMultipleChoiceTask(courseId, statement, order, insufficientOptions))
//                    .isInstanceOf(InvalidOptionsCountException.class)
//                    .hasMessage("Multiple choice task must have between 3 and 5 options");
//
//            verify(findCourseByIdPort).findById(courseId);
//            verify(saveCoursePort, never()).save(any());
//        }
    }
}
