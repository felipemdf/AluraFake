package br.com.alura.AluraFake.task.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TaskOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80, nullable = false)
    private String option;

    @Column(nullable = false)
    private boolean isCorrect;

    private LocalDateTime createdAt = LocalDateTime.now();


    public TaskOption() {}

    public TaskOption(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

}
