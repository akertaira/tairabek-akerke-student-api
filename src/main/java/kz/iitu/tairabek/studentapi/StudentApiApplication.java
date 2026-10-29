package kz.iitu.tairabek.studentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentApiApplication {
    public static void main(String[] args) {
        System.out.println("=== Student API by Akerke Tairabek (IT2-2307, ID 38137) ===");
        SpringApplication.run(StudentApiApplication.class, args);
    }
}
