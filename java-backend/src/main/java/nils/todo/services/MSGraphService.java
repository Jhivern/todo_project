package nils.todo.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Service
public class MSGraphService {
    private final AuthService authService;
    private final RestClient client;

    /**
     * Constructor for MSGraphService
     * @param authService The AuthService class, used for getting credentials
     * @param client The client with which we make HTTP requests
     */
    @Inject
    public MSGraphService(AuthService authService, RestClient client) {
        this.authService =  authService;
        this.client = client;
    }

    /**
     * Get the ID of the taskList we want to GET
     * @param displayName The name the user saved their list under
     * @return String id of the taskList
     */
    public String getTaskListID(String displayName) {
        // Perform the GET request
        String token = authService.getAccessToken();
        System.out.println("Authorization: Bearer " + token);
        String responseBody = client.get()
                .uri("/me/todo/lists")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);

        // Parse list ID from the response
        return extractIdFromJSON(displayName, responseBody);

    }

    /**
     * Extracts the id of the list, given the displayName
     * @param displayName The name of the list requested
     * @param responseBody The response of MSGraph in JSON
     * @return The internal ID of the list
     */
    private String extractIdFromJSON(String displayName, String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode lists = root.get("value");

            // Check if the displayName is found
            for (JsonNode list : lists) {
                if (list.get("displayName").asText().equals(displayName)) {
                    System.out.println("Found id for: " + displayName);
                    System.out.println(list.get("id").asText());
                    return list.get("id").asText();
                }
            }
            // The list was not found
            return null;
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failure while processing JSON", e);
        }
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
        String token = authService.getAccessToken();
        System.out.println("Authorization: Bearer " + token);
        String responseBody = client.get()
                .uri("/me/todo/lists/" + id + "/tasks?$filter=status ne 'completed'")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
        // Get the titles from response
        return extractTasksFromJSON(responseBody);
    }

    /**
     * Extracts the tasks from the responseBody
     * @param responseBody The JSON which contains tasks
     * @return The tasks in a list
     */
    private List<String> extractTasksFromJSON(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode lists = root.get("value");
            if (lists == null) {
                throw new RuntimeException("No JSON array names 'values' found");
            }

            // Add each title into a list
            List<String> taskList = new ArrayList<>();
            for (JsonNode list : lists) {
                taskList.add(list.get("title").asText());
            }

            // Sort the taskList based on the order given in String
            Collections.sort(taskList);

            // The list was not found
            return taskList.stream()
                    .limit(2)
                    .toList();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failure while processing JSON", e);
        }
    }
}
