package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.TaskOption;
import br.com.alura.AluraFake.task.domain.exception.InvalidCorrectAnswersException;
import br.com.alura.AluraFake.task.domain.exception.InvalidOptionsCountException;

import java.util.List;

public class MultipleChoiceValidator extends TaskOptionValidatorTemplate {

    @Override
    protected void validateOptionsCount(List<TaskOption> options) {
        if (options.size() < 3 || options.size() > 5) {
            throw InvalidOptionsCountException.multipleChoice();
        }
    }

    @Override
    protected void validateCorrectAnswersRules(List<TaskOption> options) {
        long correctOptions = options.stream().filter(TaskOption::isCorrect).count();
        long incorrectOptions = options.size() - correctOptions;
        
        if (correctOptions < 2) {
            throw InvalidCorrectAnswersException.multipleChoiceMinimumTwo();
        }
        
        if (incorrectOptions < 1) {
            throw InvalidCorrectAnswersException.multipleChoiceMinimumOneIncorrect();
        }
    }
}
