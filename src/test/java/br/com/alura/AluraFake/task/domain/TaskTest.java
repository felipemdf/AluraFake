package br.com.alura.AluraFake.task.domain;

import br.com.alura.AluraFake.task.domain.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Task Domain Tests")
class TaskTest {

    @Test
    @DisplayName("Should create task successfully with valid parameters")
    void shouldCreateTaskSuccessfully() {
        Task task = new Task("Enunciado válido para testes", 1, Type.OPEN_TEXT);

        assertThat(task.getStatement()).isEqualTo("Enunciado válido para testes");
        assertThat(task.getOrder()).isEqualTo(1);
        assertThat(task.getType()).isEqualTo(Type.OPEN_TEXT);
    }

    @Test
    @DisplayName("Should throw InvalidTaskStatementException when statement is too short")
    void shouldThrowExceptionWhenStatementTooShort() {
        assertThrows(InvalidTaskStatementException.class, () -> new Task("Oi", 1, Type.OPEN_TEXT));
    }

    @Test
    @DisplayName("Should throw InvalidTaskOrderException when order is negative")
    void shouldThrowExceptionWhenOrderIsNegative() {
        assertThrows(InvalidTaskOrderException.class, () -> new Task("Enunciado válido para testes", -1, Type.OPEN_TEXT));
    }

    @Test
    @DisplayName("Should throw InvalidTaskOptionsException when option is too short")
    void shouldThrowInvalidTaskOptionsForShortOption() {
        Task task = new Task("Qual é a capital?", 1, Type.SINGLE_CHOICE);
        List<TaskOption> options = List.of(
                new TaskOption("SP", true),
                new TaskOption("Rio de Janeiro", false)
        );

        assertThrows(InvalidTaskOptionsException.class, () -> task.addOptions(options));
    }

    @Test
    @DisplayName("Should throw InvalidTaskOptionsException when options are not unique")
    void shouldThrowInvalidTaskOptionsForNonUniqueOptions() {
        Task task = new Task("Qual é a capital?", 1, Type.SINGLE_CHOICE);
        List<TaskOption> options = List.of(
                new TaskOption("Brasília", true),
                new TaskOption("Brasilia", false)
        );

        assertThrows(InvalidTaskOptionsException.class, () -> task.addOptions(options));
    }

    @Test
    @DisplayName("Should throw InvalidTaskOptionsException when option equals statement")
    void shouldThrowInvalidTaskOptionsForOptionEqualsStatement() {
        Task task = new Task("Qual é a capital?", 1, Type.SINGLE_CHOICE);
        List<TaskOption> options = List.of(
                new TaskOption("qual é a capital?", true),
                new TaskOption("São Paulo", false)
        );

        assertThrows(InvalidTaskOptionsException.class, () -> task.addOptions(options));
    }

    @Nested
    @DisplayName("Single Choice Task Tests")
    class SingleChoiceTaskTests {

        @Test
        @DisplayName("Should add options successfully for single choice task")
        void shouldAddOptionsSingleChoiceSuccessfully() {
            Task task = new Task("Qual é a capital do Brasil?", 1, Type.SINGLE_CHOICE);
            List<TaskOption> options = List.of(
                    new TaskOption("Brasília", true),
                    new TaskOption("São Paulo", false)
            );

            task.addOptions(options);

            assertThat(task.getOptions()).hasSize(2);
            assertThat(task.getOptions().get(0).isCorrect()).isTrue();
        }

        @Test
        @DisplayName("Should throw InvalidOptionsCountException for single choice with insufficient options")
        void shouldThrowInvalidOptionsCountForSingleChoice() {
            Task task = new Task("Qual é a capital?", 1, Type.SINGLE_CHOICE);
            List<TaskOption> options = List.of(new TaskOption("Brasília", true));

            assertThrows(InvalidOptionsCountException.class, () -> task.addOptions(options));
        }

        @Test
        @DisplayName("Should throw InvalidCorrectAnswersException for single choice with multiple correct answers")
        void shouldThrowInvalidCorrectAnswersForSingleChoice() {
            Task task = new Task("Qual é a capital?", 1, Type.SINGLE_CHOICE);
            List<TaskOption> options = List.of(
                    new TaskOption("Brasília", true),
                    new TaskOption("São Paulo", true)
            );

            assertThrows(InvalidCorrectAnswersException.class, () -> task.addOptions(options));
        }
    }

    @Nested
    @DisplayName("Multiple Choice Task Tests")
    class MultipleChoiceTaskTests {

        @Test
        @DisplayName("Should add options successfully for multiple choice task")
        void shouldAddOptionsMultipleChoiceSuccessfully() {
            Task task = new Task("Quais são linguagens de programação?", 1, Type.MULTIPLE_CHOICE);
            List<TaskOption> options = List.of(
                    new TaskOption("Java", true),
                    new TaskOption("Python", true),
                    new TaskOption("HTML", false)
            );

            task.addOptions(options);

            assertThat(task.getOptions()).hasSize(3);
            assertThat(task.getOptions().get(0).isCorrect()).isTrue();
            assertThat(task.getOptions().get(1).isCorrect()).isTrue();
            assertThat(task.getOptions().get(2).isCorrect()).isFalse();
        }

        @Test
        @DisplayName("Should throw InvalidOptionsCountException for multiple choice with insufficient options")
        void shouldThrowInvalidOptionsCountForMultipleChoice() {
            Task task = new Task("Quais são linguagens?", 1, Type.MULTIPLE_CHOICE);
            List<TaskOption> options = List.of(
                    new TaskOption("Java", true),
                    new TaskOption("Python", true)
            );

            assertThrows(InvalidOptionsCountException.class, () -> task.addOptions(options));
        }

        @Test
        @DisplayName("Should throw InvalidCorrectAnswersException for multiple choice with insufficient correct answers")
        void shouldThrowInvalidCorrectAnswersForMultipleChoice() {
            Task task = new Task("Quais são linguagens?", 1, Type.MULTIPLE_CHOICE);
            List<TaskOption> options = List.of(
                    new TaskOption("Java", true),
                    new TaskOption("HTML", false),
                    new TaskOption("CSS3", false)
            );

            assertThrows(InvalidCorrectAnswersException.class, () -> task.addOptions(options));
        }
    }
}