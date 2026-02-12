package patmal.course.enigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "patmal.course.enigma")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}