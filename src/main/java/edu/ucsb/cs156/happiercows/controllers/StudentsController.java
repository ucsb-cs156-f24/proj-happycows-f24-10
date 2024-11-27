package edu.ucsb.cs156.happiercows.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.happiercows.entities.Student;
import edu.ucsb.cs156.happiercows.errors.EntityNotFoundException;
import edu.ucsb.cs156.happiercows.repositories.CourseRepository;
import edu.ucsb.cs156.happiercows.repositories.StudentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Tag(name = "Students")
@RequestMapping("/api/admin/students")
@RestController
public class StudentsController extends ApiController {

  @Autowired
  StudentRepository studentRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  ObjectMapper mapper;

  @Operation(summary = "Get a list of all students")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("")
  public ResponseEntity<String> allStudents()
      throws JsonProcessingException {
    Iterable<Student> students = studentRepository.findAll();
    String body = mapper.writeValueAsString(students);
    return ResponseEntity.ok().body(body);
  }

  @Operation(summary = "Get a student by id")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/student")
  public ResponseEntity<Student> studentById(@Parameter(name = "id") @RequestParam long id)
      throws JsonProcessingException {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Student.class, id));
    return ResponseEntity.ok().body(student);
  }

  @Operation(summary = "Get a student by email")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/email")
  public ResponseEntity<Iterable<Student>> studentByEmail(@Parameter(name = "email") @RequestParam String email)
      throws JsonProcessingException {
    Iterable<Student> students = studentRepository.findByEmail(email);
    return ResponseEntity.ok().body(students);
  }

  @Operation(summary = "Get a list of students by courseId")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/course")
  public ResponseEntity<Iterable<Student>> studentsByCourse(@Parameter(name = "courseId") @RequestParam long courseId)
      throws JsonProcessingException {
    Iterable<Student> students = studentRepository.findByCourseId(courseId);
    return ResponseEntity.ok().body(students);
  }

  @Operation(summary = "Get a student by perm")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/perm")
  public ResponseEntity<Iterable<Student>> studentByPerm(@Parameter(name = "perm") @RequestParam String perm)
      throws JsonProcessingException {
    Iterable<Student> students = studentRepository.findByPerm(perm);
    return ResponseEntity.ok().body(students);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/course/perm")
  public ResponseEntity<Iterable<Student>> studentByCourseAndPerm(
      @Parameter(name = "courseId") @RequestParam long courseId,
      @Parameter(name = "perm") @RequestParam String perm) throws JsonProcessingException {
    Iterable<Student> students = studentRepository.findByCourseIdAndPerm(courseId, perm);
    return ResponseEntity.ok().body(students);
  }

  @Operation(summary = "Create a new student")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/post")
  public Student createStudent(@RequestParam String perm, @RequestParam String email,
      @RequestParam String firstMiddleName, @RequestParam String lastName, @RequestParam long courseId) {
    Student student = new Student();
    student.setPerm(perm);
    student.setEmail(email);
    student.setFirstMiddleName(firstMiddleName);
    student.setLastName(lastName);
    student.setCourseId(courseId);

    Student savedStudent = studentRepository.save(student);

    return savedStudent;
  }

  @Operation(summary = "Update a student")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("")
  public Student updateStudent(
      @Parameter(name = "id") @RequestParam long id,
      @Parameter(name = "incoming") @RequestBody Student incoming) {

    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Student.class, id));

    student.setPerm(incoming.getPerm());
    student.setEmail(incoming.getEmail());
    student.setFirstMiddleName(incoming.getFirstMiddleName());
    student.setLastName(incoming.getLastName());
    student.setCourseId(incoming.getCourseId());

    studentRepository.save(student);

    return student;
  }
}