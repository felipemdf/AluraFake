package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.application.port.in.FindInstructorCoursesReportUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesByInstructorIdPort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.user.application.port.out.FindUserByIdPort;
import br.com.alura.AluraFake.user.domain.User;
import br.com.alura.AluraFake.user.domain.exception.UserNotInstructorException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindInstructorCoursesReportService implements FindInstructorCoursesReportUseCase {

    private final FindUserByIdPort findUserByIdPort;
    private final FindAllCoursesByInstructorIdPort findAllCoursesByInstructorIdPort;

    public FindInstructorCoursesReportService(FindUserByIdPort findUserByIdPort,
                                              FindAllCoursesByInstructorIdPort findAllCoursesByInstructorIdPort) {

        this.findUserByIdPort = findUserByIdPort;
        this.findAllCoursesByInstructorIdPort = findAllCoursesByInstructorIdPort;
    }

    @Override
    public List<Course> findInstructorCoursesReport(Long instructorId) {

        User instructor = findUserByIdPort.findById(instructorId);

        if (!instructor.isInstructor()) {
            throw new UserNotInstructorException(instructorId);
        }

        return findAllCoursesByInstructorIdPort.findCourseByInstructorId(instructorId);
    }
}
