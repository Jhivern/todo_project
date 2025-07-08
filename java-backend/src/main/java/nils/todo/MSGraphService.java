package nils.todo;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.microsoft.aad.msal4j.HttpRequest;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Singleton
public class MSGraphService {
    private final AuthService authService;

    @Inject
    public MSGraphService(AuthService authService) {
        this.authService =  authService;
    }

    /**
     * Get the ID of the taskList we want to GET
     * @param displayName The name the user saved their list under
     * @return String id of the taskList
     */
    public String getTaskListID(String displayName) {
        // Make a call to MSGraph using displayName
        // Parse list ID from
        return null;
    }

    /**
     * Get top 2 tasks
     * @param id The id of the taskList
     * @return List of the top 2 tasks
     */
    public List<String> getTop2Tasks(String id) {
        // Make a call to MSGraph
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
