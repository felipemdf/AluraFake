package br.com.alura.AluraFake.course.adapter.in;

import br.com.alura.AluraFake.course.adapter.in.dto.CourseListItemDTO;
import br.com.alura.AluraFake.course.adapter.in.dto.InstructorCourseReportDTO;
import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.application.port.in.CreateCourseUseCase;
import br.com.alura.AluraFake.course.application.port.in.FindInstructorCoursesReportUseCase;
import br.com.alura.AluraFake.course.application.port.in.PublishCourseUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesPort;
import br.com.alura.AluraFake.course.domain.Course;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {

    private final PublishCourseUseCase publishCourseUseCase;
    private final FindAllCoursesPort findAllCoursesPort;
    private final FindInstructorCoursesReportUseCase findInstructorCoursesReportUseCase;
    private final CreateCourseUseCase createCourseUseCase;

    @Autowired
    public CourseController(PublishCourseUseCase publishCourseUseCase,
                            FindAllCoursesPort findAllCoursesPort,
                            FindInstructorCoursesReportUseCase findInstructorCoursesReportUseCase,
                            CreateCourseUseCase createCourseUseCase){

        this.publishCourseUseCase = publishCourseUseCase;
        this.findAllCoursesPort = findAllCoursesPort;
        this.findInstructorCoursesReportUseCase = findInstructorCoursesReportUseCase;
        this.createCourseUseCase = createCourseUseCase;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

       createCourseUseCase.create(newCourse);

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
