package br.com.alura.AluraFake.course.domain;

import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.Type;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseTest {

    private Course course;
    private User instructor;

    @BeforeEach
    void setUp() {
        instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        course = new Course("Java Programming", "Curso completo de Java", instructor);
    }

    @Test
    @DisplayName("Should add task successfully")
    void shouldAddTaskSuccessfully() {
        Task task = new Task("What is Java?", 1, Type.OPEN_TEXT);

        course.addTask(task);

        assertThat(course.getTasks()).hasSize(1);
        assertThat(course.getTasks().get(0)).isEqualTo(task);
        assertThat(course.getTasks().get(0).getStatement()).isEqualTo("What is Java?");
        assertThat(course.getTasks().get(0).getOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should increment order of existing task when adding task with same order")
    void shouldIncrementOrderOfExistingTaskWhenAddingTaskWithSameOrder() {
        Task existingTask = new Task("What is Java?", 1, Type.OPEN_TEXT);
        Task newTask = new Task("What is OOP?", 1, Type.SINGLE_CHOICE);

        course.addTask(existingTask);
        course.addTask(newTask);

        assertThat(course.getTasks()).hasSize(2);
        assertThat(course.getTasks().get(0).getOrder()).isEqualTo(2);
        assertThat(course.getTasks().get(1).getOrder()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw exception when course is not in BUILDING status")
    void shouldThrowExceptionWhenCourseIsNotInBuildingStatus() {
        course.setStatus(Status.PUBLISHED);
        Task task = new Task("What is Java?", 1, Type.OPEN_TEXT);

        assertThatThrownBy(() -> course.addTask(task))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course is not in BUILDING status");

        assertThat(course.getTasks()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when task statement is duplicate")
    void shouldThrowExceptionWhenTaskStatementIsDuplicate() {
        Task firstTask = new Task("What is Java?", 1, Type.OPEN_TEXT);
        Task duplicateTask = new Task("What is Java?", 2, Type.SINGLE_CHOICE);

        course.addTask(firstTask);

        assertThatThrownBy(() -> course.addTask(duplicateTask))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Duplicate task statement in the course");

        assertThat(course.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when task order is not sequential")
    void shouldThrowExceptionWhenTaskOrderIsNotSequential() {
        Task firstTask = new Task("What is Java?", 1, Type.OPEN_TEXT);
        Task nonSequentialTask = new Task("What is Spring?", 3, Type.SINGLE_CHOICE);

        course.addTask(firstTask);

        assertThatThrownBy(() -> course.addTask(nonSequentialTask))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Task order must be sequential");

        assertThat(course.getTasks()).hasSize(1);
    }

    @Test
    @DisplayName("Should add multiple tasks in sequence successfully")
    void shouldAddMultipleTasksInSequenceSuccessfully() {
        Task task1 = new Task("What is Java?", 1, Type.OPEN_TEXT);
        Task task2 = new Task("What is OOP?", 2, Type.SINGLE_CHOICE);
        Task task3 = new Task("What are design patterns?", 3, Type.MULTIPLE_CHOICE);

        course.addTask(task1);
        course.addTask(task2);
        course.addTask(task3);

        assertThat(course.getTasks()).hasSize(3);
        assertThat(course.getTasks().get(0).getOrder()).isEqualTo(1);
        assertThat(course.getTasks().get(1).getOrder()).isEqualTo(2);
        assertThat(course.getTasks().get(2).getOrder()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should publish course successfully")
    void shouldPublishCourseSuccessfully() {
        Task openTextTask = new Task("Explain Java concepts", 1, Type.OPEN_TEXT);
        Task singleChoiceTask = new Task("What is Java?", 2, Type.SINGLE_CHOICE);
        Task multipleChoiceTask = new Task("Select Java features", 3, Type.MULTIPLE_CHOICE);

        course.addTask(openTextTask);
        course.addTask(singleChoiceTask);
        course.addTask(multipleChoiceTask);

        LocalDateTime beforePublish = LocalDateTime.now();

        course.publish();

        assertThat(course.getStatus()).isEqualTo(Status.PUBLISHED);
        assertThat(course.getPublishedAt()).isNotNull();
        assertThat(course.getPublishedAt()).isAfterOrEqualTo(beforePublish);
    }

    @Test
    @DisplayName("Should throw exception when course is not in BUILDING status")
    void shouldThrowExceptionWhenPublishCourseIsNotInBuildingStatus() {
        Task openTextTask = new Task("Explain Java concepts", 1, Type.OPEN_TEXT);
        Task singleChoiceTask = new Task("What is Java?", 2, Type.SINGLE_CHOICE);
        Task multipleChoiceTask = new Task("Select Java features", 3, Type.MULTIPLE_CHOICE);

        course.addTask(openTextTask);
        course.addTask(singleChoiceTask);
        course.addTask(multipleChoiceTask);

        course.setStatus(Status.PUBLISHED);

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course is not in BUILDING status");
    }

    @Test
    @DisplayName("Should throw exception when course does not have all activity types")
    void shouldThrowExceptionWhenCourseDoesNotHaveAllActivityTypes() {
        Task openTextTask = new Task("Explain Java concepts", 1, Type.OPEN_TEXT);
        course.addTask(openTextTask);

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");

        assertThat(course.getStatus()).isEqualTo(Status.BUILDING);
        assertThat(course.getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("Should throw exception when missing SINGLE_CHOICE task")
    void shouldThrowExceptionWhenMissingSingleChoiceTask() {
        Task openTextTask = new Task("Explain Java concepts", 1, Type.OPEN_TEXT);
        Task multipleChoiceTask = new Task("Select Java features", 2, Type.MULTIPLE_CHOICE);

        course.addTask(openTextTask);
        course.addTask(multipleChoiceTask);

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");
    }

    @Test
    @DisplayName("Should throw exception when missing MULTIPLE_CHOICE task")
    void shouldThrowExceptionWhenMissingMultipleChoiceTask() {
        Task openTextTask = new Task("Explain Java concepts", 1, Type.OPEN_TEXT);
        Task singleChoiceTask = new Task("What is Java?", 2, Type.SINGLE_CHOICE);

        course.addTask(openTextTask);
        course.addTask(singleChoiceTask);

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");
    }

    @Test
    @DisplayName("Should throw exception when missing OPEN_TEXT task")
    void shouldThrowExceptionWhenMissingOpenTextTask() {
        Task singleChoiceTask = new Task("What is Java?", 1, Type.SINGLE_CHOICE);
        Task multipleChoiceTask = new Task("Select Java features", 2, Type.MULTIPLE_CHOICE);

        course.addTask(singleChoiceTask);
        course.addTask(multipleChoiceTask);

        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");
    }

    @Test
    @DisplayName("Should throw exception when course has no tasks")
    void shouldThrowExceptionWhenCourseHasNoTasks() {
        assertThatThrownBy(() -> course.publish())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");

        assertThat(course.getStatus()).isEqualTo(Status.BUILDING);
        assertThat(course.getPublishedAt()).isNull();
    }

    @Test
    @DisplayName("Should create course successfully with instructor")
    void shouldCreateCourseSuccessfullyWithInstructor() {
        Course newCourse = new Course("Spring Boot", "Curso de Spring Boot", instructor);

        assertThat(newCourse.getTitle()).isEqualTo("Spring Boot");
        assertThat(newCourse.getDescription()).isEqualTo("Curso de Spring Boot");
        assertThat(newCourse.getInstructor()).isEqualTo(instructor);
        assertThat(newCourse.getStatus()).isEqualTo(Status.BUILDING);
        assertThat(newCourse.getTasks()).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when user is not instructor")
    void shouldThrowExceptionWhenUserIsNotInstructor() {
        User student = new User("João", "joao@alura.com.br", Role.STUDENT);

        assertThatThrownBy(() -> new Course("Java Programming", "Curso de Java", student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuario deve ser um instrutor");
    }
}
