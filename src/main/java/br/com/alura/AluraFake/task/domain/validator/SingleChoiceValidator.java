package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.TaskOption;

import java.util.List;

public class SingleChoiceValidator extends TaskOptionValidatorTemplate {

    @Override
    protected void validateOptionsCount(List<TaskOption> options) {
        if (options.size() < 2 || options.size() > 5) {
            throw new IllegalArgumentException("Single choice task must have between 2 and 5 options");
        }
    }

    @Override
    protected void validateCorrectAnswersRules(List<TaskOption> options) {
        long correctCount = options.stream().filter(TaskOption::isCorrect).count();
        
        if (correctCount != 1) {
            throw new IllegalArgumentException("Single choice task must have exactly one correct answer");
        }
    }
}
