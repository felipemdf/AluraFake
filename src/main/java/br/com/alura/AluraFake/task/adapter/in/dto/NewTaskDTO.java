package br.com.alura.AluraFake.task.adapter.in.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;


public class NewTaskDTO {

    @NotNull(message = "Course id cannot be null")
    private final Long courseId;

    @NotBlank
    @Size(min = 4, max = 255, message = "Statement must contain between 4 and 255 characters")
    private final String statement;

    @Positive
    private final Integer order;

    public NewTaskDTO(Long courseId, String statement, Integer order) {
        this.courseId = courseId;
        this.statement = statement;
        this.order = order;
    }

    public Long getCourseId() {

        return courseId;
    }

    public String getStatement() {

        return statement;
    }

    public Integer getOrder() {

        return order;
    }

}
