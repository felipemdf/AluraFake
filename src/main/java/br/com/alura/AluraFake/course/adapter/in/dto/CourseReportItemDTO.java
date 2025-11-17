package br.com.alura.AluraFake.course.adapter.in.dto;

import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.Status;

import java.time.LocalDateTime;
import java.util.List;

public class CourseReportItemDTO {
    private Long id;
    private String title;
    private Status status;
    private LocalDateTime publishedAt;
    private int taskCount;

    public CourseReportItemDTO(Long id, String title, Status status, LocalDateTime publishedAt, int taskCount) {

        this.id = id;
        this.title = title;
        this.status = status;
        this.publishedAt = publishedAt;
        this.taskCount = taskCount;
    }

    public static CourseReportItemDTO of(Course course) {

        return new CourseReportItemDTO(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                course.getPublishedAt(),
                course.getTasks().size()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public int getTaskCount() {
        return taskCount;
    }
}
