package nils.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taskAPI")
public class MSGraphController {

    private final MSGraphService graphService;

    public MSGraphController(MSGraphService graphService) {
        this.graphService = graphService;
    }

    // Return the top 2 tasks from a list
    @GetMapping("/tasks")
    public List<String> getTop2Tasks(@RequestParam String name) {
        // Call MSGraphService
        String id = graphService.getTaskListID(name);
        return graphService.getTop2Tasks(id);
    }

}
