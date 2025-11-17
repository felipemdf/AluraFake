package br.com.alura.AluraFake.course.adapter.in;

import br.com.alura.AluraFake.course.adapter.in.dto.CourseListItemDTO;
import br.com.alura.AluraFake.course.adapter.in.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.application.port.in.FindInstructorCoursesReportUseCase;
import br.com.alura.AluraFake.course.application.port.in.PublishCourseUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.shared.util.ErrorItemDTO;
import br.com.alura.AluraFake.user.application.port.out.FindUserByEmailPort;
import br.com.alura.AluraFake.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {

    private final PublishCourseUseCase publishCourseUseCase;
    private final SaveCoursePort saveCoursePort;
    private final FindAllCoursesPort findAllCoursesPort;
    private final FindUserByEmailPort findUserByEmailPort;
    private final FindInstructorCoursesReportUseCase findInstructorCoursesReportUseCase;

    @Autowired
    public CourseController(PublishCourseUseCase publishCourseUseCase,
                            SaveCoursePort saveCoursePort,
                            FindAllCoursesPort findAllCoursesPort,
                            FindUserByEmailPort findUserByEmailPort,
                            FindInstructorCoursesReportUseCase findInstructorCoursesReportUseCase){

        this.publishCourseUseCase = publishCourseUseCase;
        this.saveCoursePort = saveCoursePort;
        this.findAllCoursesPort = findAllCoursesPort;
        this.findUserByEmailPort = findUserByEmailPort;
        this.findInstructorCoursesReportUseCase = findInstructorCoursesReportUseCase;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        Optional<User> possibleAuthor = findUserByEmailPort
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);

        if(possibleAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", "Usuário não é um instrutor"));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        saveCoursePort.save(course);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> listCourses() {
        List<CourseListItemDTO> courses = findAllCoursesPort.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();

        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity publishCourse(@PathVariable("id") Long id) {

        publishCourseUseCase.publish(id);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/course/instructor/{id}")
    public ResponseEntity<InstructorCourseReportDTO> listCoursesByInstructor(@PathVariable("id") Long id) {
        List<Course> courses = findInstructorCoursesReportUseCase.findInstructorCoursesReport(id);

        InstructorCourseReportDTO report = InstructorCourseReportDTO.of(courses);

        return ResponseEntity.ok(report);
    }
}
