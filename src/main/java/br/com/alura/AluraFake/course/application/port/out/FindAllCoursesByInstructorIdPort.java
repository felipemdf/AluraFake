package br.com.alura.AluraFake.course.application.port.out;

import br.com.alura.AluraFake.course.domain.Course;

import java.util.List;

public interface FindAllCoursesByInstructorIdPort {

    List<Course> findCourseByInstructorId(Long instructorId);
}
