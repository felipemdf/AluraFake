package br.com.alura.AluraFake.course.application.port.in;

import br.com.alura.AluraFake.course.domain.Course;

import java.util.List;

public interface FindInstructorCoursesReportUseCase {
    
    List<Course> findInstructorCoursesReport(Long instructorId);
}
