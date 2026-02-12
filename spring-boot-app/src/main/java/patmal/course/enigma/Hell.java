package patmal.course.enigma;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hell {
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, Enigma!";
    }
}
