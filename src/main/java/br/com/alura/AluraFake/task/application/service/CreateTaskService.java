package br.com.alura.AluraFake.task.application.service;

import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.application.port.in.CreateTaskUseCase;
import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.exception.TaskNotFoundException;
import br.com.alura.AluraFake.task.domain.factory.TaskFactory;
import br.com.alura.AluraFake.task.domain.TaskOption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreateTaskService implements CreateTaskUseCase {

    private final FindCourseByIdPort findCourseByIdPort;
    private final SaveCoursePort saveCoursePort;

    public CreateTaskService(FindCourseByIdPort findCourseByIdPort, SaveCoursePort saveCoursePort) {
        this.findCourseByIdPort = findCourseByIdPort;
        this.saveCoursePort = saveCoursePort;
    }


    @Override
    public Task createOpenTextTask(Long courseId, String statement, Integer order) {
        Course course = findCourseByIdPort.findById(courseId);

        Task task = TaskFactory.createOpenTextTask(statement, order);
        course.addTask(task);

        Course savedCourse = saveCoursePort.save(course);

        return getSavedTask(statement, savedCourse);
    }


    @Override
    public Task createSingleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO) {
        Course course = findCourseByIdPort.findById(courseId);

        Task task = TaskFactory.createSingleChoiceTask(statement, order, createTaskOptions(optionsDTO));
        course.addTask(task);

        Course savedCourse = saveCoursePort.save(course);

        return getSavedTask(statement, savedCourse);
    }


    @Override
    public Task createMultipleChoiceTask(Long courseId, String statement, Integer order, List<NewTaskOptionDTO> optionsDTO) {
        Course course = findCourseByIdPort.findById(courseId);

        Task task = TaskFactory.createMultipleChoiceTask(statement, order, createTaskOptions(optionsDTO));
        course.addTask(task);

        Course savedCourse = saveCoursePort.save(course);

        return getSavedTask(statement, savedCourse);
    }

    private List<TaskOption> createTaskOptions(List<NewTaskOptionDTO> optionsDTO) {
        return optionsDTO.stream()
                .map(dto -> new TaskOption(dto.getOption(), dto.isCorrect()))
                .toList();
    }

    private Task getSavedTask(String statement, Course savedCourse) {
        return savedCourse.getTasks().stream()
                .filter(t -> t.getStatement().equals(statement))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(statement));
    }
}
