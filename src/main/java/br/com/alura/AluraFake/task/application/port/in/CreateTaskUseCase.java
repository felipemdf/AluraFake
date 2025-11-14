package br.com.alura.AluraFake.task.application.port.in;

import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;

import java.util.List;

public interface CreateTaskUseCase {

    void createOpenTextTask(Long courseId, String statement, Integer order);
    void createSingleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO);
    void createMultipleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO);
}
