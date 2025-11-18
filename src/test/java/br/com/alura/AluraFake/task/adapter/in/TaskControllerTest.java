package br.com.alura.AluraFake.task.adapter.in;

import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskDTO;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskOptionDTO;
import br.com.alura.AluraFake.task.adapter.in.dto.NewTaskWithOptionsDTO;
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
            NewTaskDTO dto = new NewTaskDTO(1L, "Qual é sua linguagem de programação favorita?", 1);
            Task task = new Task("Qual é sua linguagem de programação favorita?", 1, Type.OPEN_TEXT);

            when(createTaskUseCase.createOpenTextTask(1L, "Qual é sua linguagem de programação favorita?", 1)).thenReturn(task);

            // When & Then
            mockMvc.perform(post("/task/new/opentext")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createOpenTextTask(1L, "Qual é sua linguagem de programação favorita?", 1);
        }

        @Test
        @DisplayName("Should return bad request when statement is too short")
        void shouldReturnBadRequestWhenStatementIsTooShort() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(1L, "Oi", 1 );

            mockMvc.perform(post("/task/new/opentext")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("statement"))
                    .andExpect(jsonPath("$[0].message").value("Statement must contain between 4 and 255 characters"));

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "Oi", 1);
        }

        @Test
        @DisplayName("Should return bad request when statement is too long")
        void shouldReturnBadRequestWhenStatementIsTooLong() throws Exception {
            String longStatement = "a".repeat(256);
            NewTaskDTO dto = new NewTaskDTO(1L, longStatement, 1);

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
            NewTaskDTO dto = new NewTaskDTO(1L, "", 1);

            mockMvc.perform(post("/task/new/opentext")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "", 1);
        }

        @Test
        @DisplayName("Should return bad request when order is not positive")
        void shouldReturnBadRequestWhenOrderIsNotPositive() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(1L, "Enunciado válido", 0);

            mockMvc.perform(post("/task/new/opentext")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("order"));

            verify(createTaskUseCase, never()).createOpenTextTask(1L, "Enunciado válido", 0);
        }

        @Test
        @DisplayName("Should return bad request when course id is null")
        void shouldReturnBadRequestWhenCourseIdIsNull() throws Exception {
            NewTaskDTO dto = new NewTaskDTO(null, "Enunciado válido", 1);

            mockMvc.perform(post("/task/new/opentext")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("courseId"));

            verify(createTaskUseCase, never()).createOpenTextTask(any(), eq("Enunciado válido"), eq(1));
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
            NewTaskWithOptionsDTO dto = new NewTaskWithOptionsDTO(1L, "Qual é a capital do Brasil?", 2, options);

            Task task = new Task("Qual é a capital do Brasil?", 2, Type.SINGLE_CHOICE);
            when(createTaskUseCase.createSingleChoiceTask(eq(1L), eq("Qual é a capital do Brasil?"), eq(2), eq(options))).thenReturn(task);

            mockMvc.perform(post("/task/new/singlechoice")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createSingleChoiceTask(eq(1L), eq("Qual é a capital do Brasil?"), eq(2), anyList());
        }


        @Test
        @DisplayName("Should return bad request when option content is invalid")
        void shouldReturnBadRequestWhenOptionContentIsInvalid() throws Exception {
            // Given
            List<NewTaskOptionDTO> invalidOptions = List.of(
                    new NewTaskOptionDTO("BSB", true),
                    new NewTaskOptionDTO("São Paulo", false)
            );
            NewTaskWithOptionsDTO dto = new NewTaskWithOptionsDTO(1L, "Qual é a capital do Brasil?", 2, invalidOptions);

            mockMvc.perform(post("/task/new/singlechoice")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("options[0].option"))
                    .andExpect(jsonPath("$[0].message").value("Option must contain between 4 and 255 characters"));

            verify(createTaskUseCase, never()).createSingleChoiceTask(eq(1L), eq("Qual é a capital do Brasil?"), eq(2), anyList());
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
                    new NewTaskOptionDTO("Marcação HTML", false),
                    new NewTaskOptionDTO("Estilo CSS", false)
            );
            NewTaskWithOptionsDTO dto = new NewTaskWithOptionsDTO(1L, "Quais são linguagens de programação?", 3, options);
            Task task = new Task("Quais são linguagens de programação?", 3, Type.MULTIPLE_CHOICE);

            when(createTaskUseCase.createMultipleChoiceTask(eq(1L), eq("Quais são linguagens de programação?"), eq(3), eq(options)))
                    .thenReturn(task);

            mockMvc.perform(post("/task/new/multiplechoice")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(createTaskUseCase).createMultipleChoiceTask(eq(1L), eq("Quais são linguagens de programação?"), eq(3), anyList());
        }
    }
}