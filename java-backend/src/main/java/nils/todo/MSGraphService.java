package nils.todo;
import java.util.List;

public class MSGraphService {
    // Add authenticate and get method here

    /**
     * Get the ID of the taskList we want to GET
     * @param displayName The name the user saved their list under
     * @return String id of the taskList
     */
    public String getTaskListID(String displayName) {
        return null;
    }

    /**
     * Get top 2 tasks
     * @param id The id of the taskList
     * @return List of the top 2 tasks
     */
    public List<String> getTop2Tasks(String id) {
        // Make call to MSGraph
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
