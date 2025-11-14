package br.com.alura.AluraFake.course.adapter.out;

import br.com.alura.AluraFake.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
