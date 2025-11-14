package br.com.alura.AluraFake.task.domain.validator;

import br.com.alura.AluraFake.task.domain.TaskOption;

import java.text.Normalizer;
import java.util.List;

public abstract class TaskOptionValidatorTemplate {

    public final void validate(String statement, List<TaskOption> options) {
        validateOptionsCount(options);
        validateOptionsLength(options);
        validateUniqueOptions(options);
        validateOptionsNotEqualToStatement(statement, options);
        validateCorrectAnswersRules(options);
    }

    private void validateOptionsLength(List<TaskOption> options) {
        Integer minLength = 4;
        Integer maxLength = 80;

        for (TaskOption option : options) {
            Integer optionLength = option.getOption().length();

            if (optionLength < minLength || optionLength > maxLength) {
                throw new IllegalArgumentException("Each option must be between 4 and 80 characters");
            }
        }
    }

    private void validateUniqueOptions(List<TaskOption> options) {
        long uniqueCount = options.stream().map(o -> normalize(o.getOption())).distinct().count();
        
        if (uniqueCount != options.size()) {
            throw new IllegalArgumentException("All options must be unique");
        }
    }

    private void validateOptionsNotEqualToStatement(String statement, List<TaskOption> options) {
        String normalizedStatement = normalize(statement);
        
        for (TaskOption option : options) {
            if (normalize(option.getOption()).equals(normalizedStatement)) {
                throw new IllegalArgumentException("Options cannot be equal to the statement");
            }
        }
    }

    protected abstract void validateOptionsCount(List<TaskOption> options);
    
    protected abstract void validateCorrectAnswersRules(List<TaskOption> options);

    protected final String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase().trim();
    }
}
