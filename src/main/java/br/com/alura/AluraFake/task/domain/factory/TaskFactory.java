package br.com.alura.AluraFake.task.domain.factory;

import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.TaskOption;
import br.com.alura.AluraFake.task.domain.Type;

import java.util.List;

public class TaskFactory {

    public static Task createOpenTextTask(String statement, Integer order) {
        return new Task(statement, order, Type.OPEN_TEXT);
    }

    public static Task createSingleChoiceTask(String statement, Integer order, List<TaskOption> options) {

        Task task = new Task(statement, order, Type.SINGLE_CHOICE);
        task.addOptions(options);

        return task;
    }

    public static Task createMultipleChoiceTask(String statement, Integer order, List<TaskOption> options) {

        Task task = new Task(statement, order, Type.MULTIPLE_CHOICE);
        task.addOptions(options);

        return task;
    }
}
