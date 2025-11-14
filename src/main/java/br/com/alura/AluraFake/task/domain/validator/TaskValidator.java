package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.TaskOption;
import br.com.alura.AluraFake.task.domain.Type;

import java.util.List;

public class TaskValidator {

    private static final SingleChoiceValidator SINGLE_CHOICE_VALIDATOR = new SingleChoiceValidator();
    private static final MultipleChoiceValidator MULTIPLE_CHOICE_VALIDATOR = new MultipleChoiceValidator();


    public static void validateTask(Task task) {

        validateStatement(task.getStatement());
        validateOrder(task.getOrder());
    }


    public static void validateOptions(String statement, Type type, List<TaskOption> options) {

        if (type == Type.OPEN_TEXT) {
            return;
        }


        TaskOptionValidatorTemplate validator = getValidatorForType(type);
        validator.validate(statement, options);
    }
    

    private static TaskOptionValidatorTemplate getValidatorForType(Type type) {

        return switch (type) {
            case SINGLE_CHOICE -> SINGLE_CHOICE_VALIDATOR;
            case MULTIPLE_CHOICE -> MULTIPLE_CHOICE_VALIDATOR;
            case OPEN_TEXT -> throw new IllegalArgumentException("Open text tasks do not require option; validation");
        };
    }


    private static void validateStatement(String statement) {

        if (statement.length() < 4 || statement.length() > 255) {
            throw new IllegalArgumentException("Statement must be between 4 and 255 characters");
        }
    }

    private static void validateOrder(Integer order) {

        
        if (order < 0) {
            throw new IllegalArgumentException("Order must be a positive integer");
        }
    }
}
