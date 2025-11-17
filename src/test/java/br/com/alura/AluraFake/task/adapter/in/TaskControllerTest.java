package br.com.alura.AluraFake.task.adapter.in;

import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.application.port.in.CreateTaskUseCase;
import br.com.alura.AluraFake.task.domain.Task;
import br.com.alura.AluraFake.task.domain.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @Nested
    @DisplayName("POST /task/new/opentext")
    class CreateOpenTextTask {

        @Test
        @DisplayName("Should create open text task successfully")
        void shouldCreateOpenTextTaskSuccessfully() throws Exception {
            // Given
            NewTaskDTO dto = new NewTaskDTO(1L, "What is your favorite programming language?", 1, List.of());
            Task task = new Task("What is your favorite programming language?", 1, Type.OPEN_TEXT);

            when(createTaskUseCase.createOpenTextTask(1L, "What is your favorite programming language?", 1)).thenReturn(task);

            // When & Then
            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createOpenTextTask(1L, "What is your favorite programming language?", 1);
        }

        @Test
        @DisplayName("Should return bad request when statement is too short")
        void shouldReturnBadRequestWhenStatementIsTooShort() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(1L, "Hi", 1, List.of()); // Too short

            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("statement"))
                    .andExpect(jsonPath("$[0].message").value("Statement must contain between 4 and 255 characters"));

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "Hi", 1);
        }

        @Test
        @DisplayName("Should return bad request when statement is too long")
        void shouldReturnBadRequestWhenStatementIsTooLong() throws Exception {
            String longStatement = "a".repeat(256);
            NewTaskDTO dto = new NewTaskDTO(1L, longStatement, 1, List.of());

            // When & Then
            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("statement"))
                    .andExpect(jsonPath("$[0].message").value("Statement must contain between 4 and 255 characters"));

            verify(createTaskUseCase, never()).createOpenTextTask(1L, longStatement, 1);
        }

        @Test
        @DisplayName("Should return bad request when statement is blank")
        void shouldReturnBadRequestWhenStatementIsBlank() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(1L, "", 1, List.of());

            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "", 1);
        }

        @Test
        @DisplayName("Should return bad request when order is not positive")
        void shouldReturnBadRequestWhenOrderIsNotPositive() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(1L, "Valid statement", 0, List.of()); // Not positive

            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("order"));

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "Valid statement", 0);
        }

        @Test
        @DisplayName("Should return bad request when course id is null")
        void shouldReturnBadRequestWhenCourseIdIsNull() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(null, "Valid statement", 1, List.of());

            mockMvc.perform(post("/task/new/opentext")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("courseId"));

            verify(createTaskUseCase, never()).createOpenTextTask(any(), eq("Valid statement"), eq(1));
        }

    }

    @Nested
    @DisplayName("POST /task/new/singlechoice")
    class CreateSingleChoiceTask {

        @Test
        @DisplayName("Should create single choice task successfully")
        void shouldCreateSingleChoiceTaskSuccessfully() throws Exception {
            List<NewTaskOptionDTO> options = List.of(
                new NewTaskOptionDTO("Brasília", true),
                new NewTaskOptionDTO("São Paulo", false),
                new NewTaskOptionDTO("Rio de Janeiro", false)
            );
            NewTaskDTO dto = new NewTaskDTO(1L, "What is the capital of Brazil?", 2, options);

            Task task = new Task("What is the capital of Brazil?", 2, Type.SINGLE_CHOICE);
            when(createTaskUseCase.createSingleChoiceTask(eq(1L), eq("What is the capital of Brazil?"), eq(2), eq(options))).thenReturn(task);

            mockMvc.perform(post("/task/new/singlechoice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createSingleChoiceTask(eq(1L), eq("What is the capital of Brazil?"), eq(2), anyList());
        }


        @Test
        @DisplayName("Should return bad request when option content is invalid")
        void shouldReturnBadRequestWhenOptionContentIsInvalid() throws Exception {
            // Given
            List<NewTaskOptionDTO> invalidOptions = List.of(
                new NewTaskOptionDTO("BSB", true),
                new NewTaskOptionDTO("São Paulo", false)
            );
            NewTaskDTO dto = new NewTaskDTO(1L, "What is the capital of Brazil?", 2, invalidOptions);

            mockMvc.perform(post("/task/new/singlechoice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("options[0].option"))
                    .andExpect(jsonPath("$[0].message").value("Option must contain between 4 and 255 characters"));

            verify(createTaskUseCase, never()).createSingleChoiceTask(eq(1L), eq("What is the capital of Brazil?"), eq(2), anyList());
        }
    }

    @Nested
    @DisplayName("POST /task/new/multiplechoice")
    class CreateMultipleChoiceTask {

        @Test
        @DisplayName("Should create multiple choice task successfully")
        void shouldCreateMultipleChoiceTaskSuccessfully() throws Exception {
            List<NewTaskOptionDTO> options = List.of(
                new NewTaskOptionDTO("Java", true),
                new NewTaskOptionDTO("Python", true),
                new NewTaskOptionDTO("HTML markup", false),
                new NewTaskOptionDTO("CSS styling", false)
            );
            NewTaskDTO dto = new NewTaskDTO(1L, "Which are programming languages?", 3, options);
            Task task = new Task("Which are programming languages?", 3, Type.MULTIPLE_CHOICE);

            when(createTaskUseCase.createMultipleChoiceTask(eq(1L), eq("Which are programming languages?"), eq(3), eq(options)))
                    .thenReturn(task);

            mockMvc.perform(post("/task/new/multiplechoice")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createMultipleChoiceTask(eq(1L), eq("Which are programming languages?"), eq(3), anyList());
        }
    }
}
