package br.com.alura.AluraFake.course.application.port.out;

import br.com.alura.AluraFake.course.domain.Course;

public interface FindCourseByIdPort {

    Course findById(Long courseId);
}
