package nils.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taskApi")
public class MSGraphController {

    @GetMapping("/tasks")
    public List<String> getAllTasks() {
        // return all task titles
        return null;
    }

}
