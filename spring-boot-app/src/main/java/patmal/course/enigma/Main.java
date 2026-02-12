package patmal.course.enigma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // This line starts the entire Spring framework and the embedded server (like Tomcat)
        SpringApplication.run(Main.class, args);
    }
}