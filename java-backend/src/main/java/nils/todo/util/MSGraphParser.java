package nils.todo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
@Service
public class MSGraphParser {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Extracts the id of the list, given the displayName
     * @param displayName The name of the list requested
     * @param responseBody The response of MSGraph in JSON
     * @return The internal ID of the list
     */
    public String extractIdFromJSON(String displayName, String responseBody) {
        if (displayName == null || responseBody == null) {
            return null;
        }
        try {
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
     * Extracts the tasks from the responseBody
     * @param responseBody The JSON which contains tasks
     * @return The tasks in a list
     */
    public List<String> extractTasksFromJSON(String responseBody) {
        if (responseBody == null) {
            return new ArrayList<>();
        }
        try {
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

            // Determine the correct tasks to be returned
            return determineTaskOrder(taskList);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Failure while processing JSON", e);
        }
    }

    /**
     * Calculates the reduced tas list based on certain criteria
     * Current criteria: The top 2 tasks, so sorted lexicographically
     * Means: User has to use 1. 2. notation to get correct ordering
     * @param taskList The full list of tasks
     * @return A list that has been filtered according to criteria
     */
    private List<String> determineTaskOrder(List<String> taskList) {
        if (taskList == null) {
            return new ArrayList<String>();
        }

        // Sort the taskList based on the order given in String
        Collections.sort(taskList);

        // The list was not found
        return taskList.stream()
                .limit(2)
                .toList();
    }
}
