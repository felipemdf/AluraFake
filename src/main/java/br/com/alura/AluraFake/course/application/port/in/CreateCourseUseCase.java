package br.com.alura.AluraFake.course.application.port.in;

import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.domain.Course;

public interface CreateCourseUseCase {

    Course create(NewCourseDTO newCourse);
}
