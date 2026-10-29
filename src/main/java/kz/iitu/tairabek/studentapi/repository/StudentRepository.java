package kz.iitu.tairabek.studentapi.repository;

import kz.iitu.tairabek.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    java.util.List<Student> findByLastNameIgnoreCase(String lastName);
}
