package br.com.alura.AluraFake.course.domain.validator;

import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.Status;
import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.Type;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

public class CourseValidator {

    public static void validateTask(Course course, String statement, Integer order) {
        validateCourseIsBuilding(course);
        validateStatementIsUnique(course, statement);
        validateOrderIsSequential(course, order);
    }

    public static void validatePublish(Course course) {
        validateHasAllActivityTypes(course);
        validateCourseIsBuilding(course);
    }


    private static void validateCourseIsBuilding(Course course) {

        if (!course.getStatus().equals(Status.BUILDING)) {

            throw new RuntimeException("Course is not in BUILDING status");
        }
    }

    private static void validateStatementIsUnique(Course course, String statement) {


        if (course.getTasks()
                .stream()
                .map(Task::getStatement)
                .anyMatch(s -> normalize(s).equals(normalize(statement)))) {

            throw new RuntimeException("Duplicate task statement in the course");
        }
    }

    private static void validateOrderIsSequential(Course course, Integer order) {
        Integer maxOrder = course.getTasks().stream().map(Task::getOrder).max(Integer::compareTo).orElse(0);

        if (order > maxOrder + 1) {

            throw new RuntimeException("Task order must be sequential");
        }
    }

    public static void validateHasAllActivityTypes(Course course) {
        boolean hasAllTypes = List.of(Type.values()).stream()
                .allMatch(type -> hasTaskOfType(course.getTasks(), type));

        if (!hasAllTypes) {
            throw new RuntimeException("Course must have at least one task of each type: OPEN_TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE");
        }
    }

    private static boolean hasTaskOfType(List<Task> tasks, Type type) {
        return tasks.stream()
                .anyMatch(t -> t.getType().equals(type));
    }

    private static String normalize(String text) {

        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}
