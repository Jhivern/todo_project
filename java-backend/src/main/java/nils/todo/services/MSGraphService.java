package nils.todo.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Singleton
@Service
public class MSGraphService {
    private final AuthService authService;
    private final RestClient client;

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
    public List<String> getTop2Tasks(String id) {
        // Make a call to MSGraph
        RestClient client = RestClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0/me")
                .build();


        // Parse titles
        return null;
    }

    /**
     * Parse JSON received by MSGraph API
     * @param json The incoming JSON, from which we grab the title
     * @return String with the title field from the JSON
     */
    public String parseTitle(String json) {
        return null;
    }
}
