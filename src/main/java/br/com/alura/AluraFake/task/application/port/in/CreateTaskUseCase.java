package br.com.alura.AluraFake.task.application.port.in;

import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.domain.Task;

import java.util.List;

public interface CreateTaskUseCase {

    Task createOpenTextTask(Long courseId, String statement, Integer order);
    Task createSingleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO);
    Task createMultipleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO);
}
