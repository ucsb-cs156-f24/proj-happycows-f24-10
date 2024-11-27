package edu.ucsb.cs156.happiercows.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucsb.cs156.happiercows.ControllerTestCase;
import edu.ucsb.cs156.happiercows.entities.Student;
import edu.ucsb.cs156.happiercows.repositories.CourseRepository;
import edu.ucsb.cs156.happiercows.repositories.StudentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentsController.class)
@Import(StudentsController.class)
@AutoConfigureDataJpa
public class StudentControllerTests extends ControllerTestCase {
  @Autowired
  ObjectMapper objectMapper = new ObjectMapper();

  @MockBean
  StudentRepository studentRepository;

  @MockBean
  CourseRepository courseRepository;

  Student expectedStudent = Student.builder().id(42L)
      .email("cgaucho@ucsb.edu").firstMiddleName("Chris P")
      .lastName("Gaucho").courseId(1L).perm("1234567")
      .build();

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getAllStudents() throws Exception {
    List<Student> students = List.of(expectedStudent);
    when(studentRepository.findAll()).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findAll();

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentById() throws Exception {
    when(studentRepository.findById(42L)).thenReturn(Optional.of(expectedStudent));

    MvcResult response = mockMvc.perform(get("/api/admin/students/student?id=42")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findById(eq(42L));

    String responseString = response.getResponse().getContentAsString();
    Student actualStudent = objectMapper.readValue(responseString, Student.class);
    assertEquals(expectedStudent, actualStudent);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentByIdNotFound() throws Exception {
    when(studentRepository.findById(42L)).thenReturn(Optional.empty());

    MvcResult response = mockMvc.perform(get("/api/admin/students/student?id=42")).andDo(print())
        .andExpect(status().isNotFound()).andReturn();

    verify(studentRepository, times(1)).findById(eq(42L));

    Map<String, Object> json = responseToJson(response);
    assertEquals("EntityNotFoundException", json.get("type"));
    assertEquals("Student with id 42 not found", json.get("message"));
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentByEmail() throws Exception {
    List<Student> students = List.of(expectedStudent);
    when(studentRepository.findByEmail("cgaucho@ucsb.edu")).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students/email?email=cgaucho@ucsb.edu")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findByEmail(eq("cgaucho@ucsb.edu"));

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentsByCourse() throws Exception {
    List<Student> students = List.of(expectedStudent);
    when(studentRepository.findByCourseId(1L)).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students/course?courseId=1")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findByCourseId(eq(1L));

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentsByPerm() throws Exception {
    List<Student> students = List.of(expectedStudent);
    when(studentRepository.findByPerm("1234567")).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students/perm?perm=1234567")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findByPerm(eq("1234567"));

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentsByCourseAndPerm() throws Exception {
    List<Student> students = List.of(expectedStudent);
    when(studentRepository.findByCourseIdAndPerm(1L,
        "1234567")).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students/course/perm?courseId=1&perm=1234567")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findByCourseIdAndPerm(eq(1L),
        eq("1234567"));

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void getStudentsByCourseAndPermNoStudents() throws Exception {
    List<Student> students = List.of();
    when(studentRepository.findByCourseIdAndPerm(1L,
        "1234567")).thenReturn(students);

    MvcResult response = mockMvc.perform(get("/api/admin/students/course/perm?courseId=1&perm=1234567")).andDo(print())
        .andExpect(status().isOk()).andReturn();

    verify(studentRepository, times(1)).findByCourseIdAndPerm(eq(1L),
        eq("1234567"));

    String responseString = response.getResponse().getContentAsString();
    List<Student> actualStudents = objectMapper.readValue(responseString, new TypeReference<List<Student>>() {
    });
    assertEquals(students, actualStudents);
  }

}
