package br.com.alura.AluraFake.task.adapter.in.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


public class NewTaskWithOptionsDTO extends NewTaskDTO {

    @NotEmpty
    @Valid
    private List<NewTaskOptionDTO> options;

    public NewTaskWithOptionsDTO(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> options) {
       super(courseId, statement, order);
       this.options = options;
    }


    public List<NewTaskOptionDTO> getOptions() {
        return options;
    }
}
