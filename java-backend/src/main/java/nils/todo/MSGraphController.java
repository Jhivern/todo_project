package nils.todo;

import com.google.inject.Inject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/taskAPI/tasks")
public class MSGraphController {

    private final MSGraphService graphService;
    private final AuthService authService;

    @Inject
    public MSGraphController(MSGraphService graphService, AuthService authService) {
        this.graphService = graphService;
        this.authService = authService;
    }

    /**
     * Return the top 2 tasks from a list
     * @param name The name of the list we want to look into
     * @return Strings of the top 2 tasks from that list
     */
    @GetMapping
    public ResponseEntity<List<String>> getTop2Tasks(@RequestParam String name) {
        // Check if the server is ready to handle requests
        if (!authService.hasValidToken()) {
            return ResponseEntity.status(401).build();
        }
        // Call MSGraphService, since we know it's authenticated
        String id = graphService.getTaskListID(name);
        return ResponseEntity.ok().body(graphService.getTop2Tasks(id));
    }

}
