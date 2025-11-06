package nils.todo.controllers;

import com.google.inject.Inject;
import nils.todo.facades.AuthFacade;
import nils.todo.services.MSGraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taskApi/tasks")
public class MSGraphController {

    private final MSGraphService graphService;
    private final AuthFacade authFacade;

    /**
     * Constructor for MSGraphController
     * @param graphService The GMSGraphService class, used for interacting with the MSGraph API
     * @param authFacade The AuthFacade class, used for getting credentials
     */
    @Inject
    public MSGraphController(MSGraphService graphService, AuthFacade authFacade) {
        this.graphService = graphService;
        this.authFacade = authFacade;
    }

    /**
     * Return the top 2 tasks from a list
     * @param name The name of the list we want to look into
     * @return Strings of the top 2 tasks from that list
     */
    @GetMapping
    public ResponseEntity<List<String>> getTop2Tasks(@RequestParam String name) {
        // Check if the server is ready to handle requests
        if (!authFacade.hasValidToken()) {
            return ResponseEntity.status(401).build();
        }
        // Call MSGraphService, since we know it's authenticated
        String id = graphService.getTaskListID(name);
        if (id == null) {
            return ResponseEntity.badRequest().body(List.of("List not found..."));
        }
        return ResponseEntity.ok().body(graphService.getTop2Tasks(id));
    }

}
