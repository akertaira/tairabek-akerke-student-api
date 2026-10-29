package kz.iitu.tairabek.studentapi.controller;

import jakarta.validation.Valid;
import kz.iitu.tairabek.studentapi.exception.StudentNotFoundException;
import kz.iitu.tairabek.studentapi.model.Student;
import kz.iitu.tairabek.studentapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    private final StudentRepository repository;

    @Value("${app.version}")
    private String appVersion;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public Map<String, String> info() {
        return Map.of(
                "application", "tairabek-akerke-student-api",
                "author", "Akerke Tairabek",
                "group", "IT2-2307",
                "studentId", "38137",
                "version", appVersion);
    }

    @GetMapping("/api/students")
    public List<Student> getAll() {
        return repository.findAll();
    }

    @GetMapping("/api/students/{id}")
    public Student getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @PostMapping("/api/students")
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        student.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(student));
    }

    @PutMapping("/api/students/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student data) {
        Student s = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        s.setFirstName(data.getFirstName());
        s.setLastName(data.getLastName());
        s.setEmail(data.getEmail());
        s.setGroupName(data.getGroupName());
        s.setAge(data.getAge());
        return repository.save(s);
    }

    @DeleteMapping("/api/students/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
