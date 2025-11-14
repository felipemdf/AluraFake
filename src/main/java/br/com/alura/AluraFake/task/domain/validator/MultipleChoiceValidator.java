package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.TaskOption;

import java.util.List;

public class MultipleChoiceValidator extends TaskOptionValidatorTemplate {

    @Override
    protected void validateOptionsCount(List<TaskOption> options) {
        if (options.size() < 3 || options.size() > 5) {
            throw new IllegalArgumentException("Multiple choice task must have between 3 and 5 options");
        }
    }

    @Override
    protected void validateCorrectAnswersRules(List<TaskOption> options) {
        long correctOptions = options.stream().filter(TaskOption::isCorrect).count();
        long incorrectOptions = options.size() - correctOptions;
        
        if (correctOptions < 2) {
            throw new IllegalArgumentException("Multiple choice task must have at least two correct answers");
        }
        
        if (incorrectOptions < 1) {
            throw new IllegalArgumentException("Multiple choice task must have at least one incorrect answer");
        }
    }
}
