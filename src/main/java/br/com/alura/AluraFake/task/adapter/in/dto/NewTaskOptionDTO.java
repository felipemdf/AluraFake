package br.com.alura.AluraFake.task.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class NewTaskOptionDTO {

    @NotBlank
    @Size(min = 4, max = 80, message = "Option must contain between 4 and 255 characters")
    private final String option;

    @NotNull
    @JsonProperty("isCorrect")
    private final Boolean isCorrect;

    public NewTaskOptionDTO(String option, Boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    @JsonProperty("isCorrect")
    public Boolean isCorrect() {
        return isCorrect;
    }
}
