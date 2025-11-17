package br.com.alura.AluraFake.course.adapter.in.dto;

import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.Status;

import java.util.List;

public class InstructorCourseReportDTO {

    private List<CourseReportItemDTO> courses;
    private long totalPublishedCourses;

    public InstructorCourseReportDTO(List<CourseReportItemDTO> courses, long totalPublishedCourses) {

        this.courses = courses;
        this.totalPublishedCourses = totalPublishedCourses;
    }

    public static InstructorCourseReportDTO of(List<Course> courses) {

        List<CourseReportItemDTO> courseReportItemDTOS = getCourseReportItemDTOS(courses);
        Long publishedCoursesCount = getPublishedCoursesCount(courses);

        return new InstructorCourseReportDTO(courseReportItemDTOS, publishedCoursesCount);
    }

    private static List<CourseReportItemDTO> getCourseReportItemDTOS(List<Course> courses) {

        return courses.stream().map(CourseReportItemDTO::of).toList();
    }

    private static long getPublishedCoursesCount(List<Course> courses) {

        return courses.stream().filter(course -> Status.PUBLISHED.equals(course.getStatus())).count();
    }

    public List<CourseReportItemDTO> getCourses() {
        return courses;
    }

    public long getTotalPublishedCourses() {
        return totalPublishedCourses;
    }
}
