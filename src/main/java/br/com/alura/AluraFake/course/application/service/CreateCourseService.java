package br.com.alura.AluraFake.course.application.service;

import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.application.port.in.CreateCourseUseCase;
import br.com.alura.AluraFake.course.application.port.in.PublishCourseUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.exception.UserMustBeAnInstructorException;
import br.com.alura.AluraFake.shared.util.ErrorItemDTO;
import br.com.alura.AluraFake.user.application.port.out.FindUserByEmailPort;
import br.com.alura.AluraFake.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CreateCourseService implements CreateCourseUseCase {

    private final FindCourseByIdPort findCourseByIdUseCase;
    private final SaveCoursePort saveCoursePort;
    private final FindUserByEmailPort findUserByEmailPort;

    public CreateCourseService(FindCourseByIdPort findCourseByIdUseCase,
                               SaveCoursePort saveCoursePort,
                               FindUserByEmailPort findUserByEmailPort) {

        this.findCourseByIdUseCase = findCourseByIdUseCase;
        this.findUserByEmailPort = findUserByEmailPort;
        this.saveCoursePort = saveCoursePort;
    }

    @Override
    public Course create(NewCourseDTO newCourse) {

        Optional<User> possibleAuthor = getPossibleAuthor(newCourse);

        if(possibleAuthor.isEmpty()) {
            throw new UserMustBeAnInstructorException();
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        return saveCoursePort.save(course);
    }

    private Optional<User> getPossibleAuthor(NewCourseDTO newCourse) {

        return findUserByEmailPort
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);
    }
}
