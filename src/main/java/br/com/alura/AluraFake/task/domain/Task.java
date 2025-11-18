package br.com.alura.AluraFake.task.domain;

import br.com.alura.AluraFake.task.domain.validator.TaskValidator;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "statement", length = 255, nullable = false)
    private String statement;

    @Column(name = "`order`", nullable = false)
    private Integer order;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "task_id", nullable = false)
    private List<TaskOption> options = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    public Task() {}

    public Task(String statement, Integer order, Type type) {
        this.statement = statement;
        this.order = order;
        this.type = type;
        
        TaskValidator.validateTask(this);
    }


    public String getStatement() {

        return statement;
    }

    public Integer getOrder() {

        return order;
    }

    public List<TaskOption> getOptions() {

        return options;
    }

    public Type getType() {

        return type;
    }

    public void addOptions(List<TaskOption> options) {
        TaskValidator.validateOptions(this.statement, this.type, options);
        this.options.addAll(options);
    }

    public void increaseOrder() {
        this.order += 1;
    }

}
