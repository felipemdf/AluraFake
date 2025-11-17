package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.application.port.in.PublishCourseUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.domain.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublishCourseService implements PublishCourseUseCase {

    private final FindCourseByIdPort findCourseByIdUseCase;

    public PublishCourseService(FindCourseByIdPort findCourseByIdUseCase) {

        this.findCourseByIdUseCase = findCourseByIdUseCase;
    }

    @Override
    public void publish(Long id) {

        Course course = findCourseByIdUseCase.findById(id);

        course.publish();
    }
}
