package br.com.alura.AluraFake.task.domain.exception;

import br.com.alura.AluraFake.shared.domain.exception.DomainException;


public class InvalidCorrectAnswersException extends DomainException {
    
    public InvalidCorrectAnswersException(String message) {
        super(message);
    }
    
    public static InvalidCorrectAnswersException singleChoiceExactlyOne() {

        return new InvalidCorrectAnswersException("Single choice task must have exactly one correct answer");
    }
    
    public static InvalidCorrectAnswersException multipleChoiceMinimumTwo() {

        return new InvalidCorrectAnswersException("Multiple choice task must have at least two correct answers");
    }
    
    public static InvalidCorrectAnswersException multipleChoiceMinimumOneIncorrect() {

        return new InvalidCorrectAnswersException("Multiple choice task must have at least one incorrect answer");
    }
}
