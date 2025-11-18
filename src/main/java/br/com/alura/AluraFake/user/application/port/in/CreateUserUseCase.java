package br.com.alura.AluraFake.user.application.port.in;

import br.com.alura.AluraFake.course.adapter.in.dto.NewCourseDTO;
import br.com.alura.AluraFake.course.domain.Course;
import br.com.alura.AluraFake.user.adapter.in.dto.NewUserDTO;
import br.com.alura.AluraFake.user.domain.User;

public interface CreateUserUseCase {

    User create(NewUserDTO newUser);
}
