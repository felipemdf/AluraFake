package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.TaskOption;
import br.com.alura.AluraFake.task.domain.exception.InvalidCorrectAnswersException;
import br.com.alura.AluraFake.task.domain.exception.InvalidOptionsCountException;

import java.util.List;

public class SingleChoiceValidator extends TaskOptionValidatorTemplate {

    @Override
    protected void validateOptionsCount(List<TaskOption> options) {
        if (options.size() < 2 || options.size() > 5) {
            throw InvalidOptionsCountException.singleChoice();
        }
    }

    @Override
    protected void validateCorrectAnswersRules(List<TaskOption> options) {
        long correctOptions = options.stream().filter(TaskOption::isCorrect).count();
        
        if (correctOptions != 1) {
            throw InvalidCorrectAnswersException.singleChoiceExactlyOne();
        }
    }
}
