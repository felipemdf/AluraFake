package br.com.alura.AluraFake.course.adapter.out;

import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesPort;
import br.com.alura.AluraFake.course.application.port.out.FindCourseByIdPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseRepositoryAdapter implements FindCourseByIdPort, FindAllCoursesPort, SaveCoursePort {

    private final CourseRepository courseRepository;

    public CourseRepositoryAdapter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course findById(Long courseId) {

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
    }

    @Override
    public List<Course> findAll() {

        return courseRepository.findAll();
    }

    @Override
    public Course save(Course course) {

        return courseRepository.save(course);
    }
}
