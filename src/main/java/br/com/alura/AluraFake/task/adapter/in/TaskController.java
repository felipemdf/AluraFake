package br.com.alura.AluraFake.task.adapter.in;

import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskWithOptionsDTO;
import br.com.alura.AluraFake.task.application.port.in.CreateTaskUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;


    public TaskController(CreateTaskUseCase createTaskUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
    }

    @PostMapping("/task/new/opentext")
    public ResponseEntity newOpenTextExercise(@Valid @RequestBody NewTaskDTO dto) {

        createTaskUseCase.createOpenTextTask(dto.getCourseId(), dto.getStatement(), dto.getOrder());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/task/new/singlechoice")
    public ResponseEntity newSingleChoice(@Valid @RequestBody NewTaskWithOptionsDTO dto) {

        createTaskUseCase.createSingleChoiceTask(dto.getCourseId(), dto.getStatement(), dto.getOrder(), dto.getOptions());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/task/new/multiplechoice")
    public ResponseEntity newMultipleChoice(@Valid @RequestBody NewTaskWithOptionsDTO dto) {

        createTaskUseCase.createMultipleChoiceTask(dto.getCourseId(), dto.getStatement(), dto.getOrder(), dto.getOptions());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}