package nils.todo.services;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nils.todo.facades.AuthFacade;
import nils.todo.util.MSGraphParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Singleton
@Service
public class MSGraphService {
    private final AuthFacade authFacade;
    private final RestClient client;
    private final MSGraphParser msGraphParser;

    /**
     * Constructor for MSGraphService
     * @param authFacade The AuthFacade class, used for getting credentials
     * @param client The client with which we make HTTP requests
     */
    @Inject
    public MSGraphService(AuthFacade authFacade, RestClient client, MSGraphParser msGraphParser) {
        this.authFacade = authFacade;
        this.client = client;
        this.msGraphParser = msGraphParser;
    }

    /**
     * Get the ID of the taskList we want to GET
     * @param displayName The name the user saved their list under
     * @return String id of the taskList
     */
    public String getTaskListID(String displayName) {
        // Perform the GET request
        String token = authFacade.getAccessToken();
        System.out.println("Authorization: Bearer " + token);
        String responseBody = client.get()
                .uri("/me/todo/lists")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);

        // Parse list ID from the response
        return msGraphParser.extractIdFromJSON(displayName, responseBody);

    }

    /**
     * Get top 2 tasks
     * @param id The id of the taskList
     * @return List of the top 2 tasks
     */
    // What if id is wrong? We get a notFound back, but never check it...
    public List<String> getTop2Tasks(String id) {
        // Make a call to MSGraph
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String token = authFacade.getAccessToken();
        System.out.println("Authorization: Bearer " + token);
        String responseBody = client.get()
                .uri("/me/todo/lists/" + id + "/tasks?$filter=status ne 'completed'")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
        // Get the titles from response
        List<String> taskList = new ArrayList<>(msGraphParser.extractTasksFromJSON(responseBody));
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).length() > 32) {
                // Strip it down to 32 characters
                String task = taskList.get(i);
                task = task.substring(0, 32);
                taskList.set(i, task);
            }
        }
        return taskList;
    }


}
