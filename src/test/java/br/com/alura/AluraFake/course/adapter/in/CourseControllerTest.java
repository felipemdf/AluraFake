package br.com.alura.AluraFake.course.adapter.in;

import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.application.port.in.FindInstructorCoursesReportUseCase;
import br.com.alura.AluraFake.course.application.port.out.FindAllCoursesPort;
import br.com.alura.AluraFake.course.application.port.out.SaveCoursePort;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.course.domain.Status;
import br.com.alura.AluraFake.shared.domain.exception.ResourceNotFoundException;
import br.com.alura.AluraFake.user.application.port.out.FindUserByEmailPort;
import br.com.alura.AluraFake.user.domain.Role;
import br.com.alura.AluraFake.user.domain.User;
import br.com.alura.AluraFake.user.domain.exception.UserNotInstructorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaveCoursePort saveCoursePort;

    @MockBean
    private FindAllCoursesPort findAllCoursesPort;

    @MockBean
    private FindUserByEmailPort findUserByEmailPort;

    @MockBean
    private FindInstructorCoursesReportUseCase findInstructorCoursesReportUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_invalid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        doReturn(Optional.empty()).when(findUserByEmailPort)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }


    @Test
    void newCourseDTO__should_return_bad_request_when_email_is_no_instructor() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(false).when(user).isInstructor();

        doReturn(Optional.of(user)).when(findUserByEmailPort)
                .findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("emailInstructor"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void newCourseDTO__should_return_created_when_new_course_request_is_valid() throws Exception {

        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setTitle("Java");
        newCourseDTO.setDescription("Curso de Java");
        newCourseDTO.setEmailInstructor("paulo@alura.com.br");

        User user = mock(User.class);
        doReturn(true).when(user).isInstructor();

        doReturn(Optional.of(user)).when(findUserByEmailPort).findByEmail(newCourseDTO.getEmailInstructor());

        mockMvc.perform(post("/course/new").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());

        verify(saveCoursePort, times(1)).save(any(Course.class));
    }

    @Test
    void listAllCourses__should_list_all_courses() throws Exception {

        User paulo = new User("Paulo", "paulo@alua.com.br", Role.INSTRUCTOR);

        Course java = new Course("Java", "Curso de java", paulo);
        Course hibernate = new Course("Hibernate", "Curso de hibernate", paulo);
        Course spring = new Course("Spring", "Curso de spring", paulo);

        when(findAllCoursesPort.findAll()).thenReturn(Arrays.asList(java, hibernate, spring));

        mockMvc.perform(get("/course/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].title").value("Java"))
                .andExpect(jsonPath("$[0].description").value("Curso de java"))

                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].description").value("Curso de hibernate"))

                .andExpect(jsonPath("$[2].title").value("Spring"))
                .andExpect(jsonPath("$[2].description").value("Curso de spring"));
    }

    @Test
    void listCoursesByInstructor__should_return_not_found_when_user_does_not_exist() throws Exception {

        Long instructorId = 999L;

        when(findInstructorCoursesReportUseCase.findInstructorCoursesReport(instructorId))
                .thenThrow(new ResourceNotFoundException("User", instructorId));

        mockMvc.perform(get("/course/instructor/" + instructorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.field").value("resource"));
    }

    @Test
    void listCoursesByInstructor__should_return_bad_request_when_user_is_not_instructor() throws Exception {
        Long instructorId = 1L;

        when(findInstructorCoursesReportUseCase.findInstructorCoursesReport(instructorId))
                .thenThrow(new UserNotInstructorException(instructorId));

        mockMvc.perform(get("/course/instructor/" + instructorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("domain"));
    }

    @Test
    void listCoursesByInstructor__should_return_courses_report_with_published_count() throws Exception {

        Long instructorId = 1L;
        User instructor = new User("Paulo", "paulo@alura.com.br", Role.INSTRUCTOR);
        
        Course javaCourse = new Course("Java", "Curso de Java", instructor);
        javaCourse.setStatus(Status.PUBLISHED);
        
        Course springCourse = new Course("Spring", "Curso de Spring", instructor);
        springCourse.setStatus(Status.BUILDING);

        List<Course> courses = Arrays.asList(javaCourse, springCourse);

        when(findInstructorCoursesReportUseCase.findInstructorCoursesReport(instructorId))
                .thenReturn(courses);

        mockMvc.perform(get("/course/instructor/" + instructorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses.length()").value(2))

                .andExpect(jsonPath("$.courses[0].title").value("Java"))
                .andExpect(jsonPath("$.courses[0].status").value("PUBLISHED"))

                .andExpect(jsonPath("$.courses[1].title").value("Spring"))
                .andExpect(jsonPath("$.courses[1].status").value("BUILDING"))

                .andExpect(jsonPath("$.totalPublishedCourses").value(1));

    }

    @Test
    void listCoursesByInstructor__should_return_empty_list_when_instructor_has_no_courses() throws Exception {

        Long instructorId = 1L;

        when(findInstructorCoursesReportUseCase.findInstructorCoursesReport(instructorId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/course/instructor/" + instructorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses.length()").value(0))
                .andExpect(jsonPath("$.totalPublishedCourses").value(0));
    }

}