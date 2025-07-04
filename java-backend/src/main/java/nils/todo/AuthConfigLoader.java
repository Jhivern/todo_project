package nils.todo;

public class AuthConfigLoader {
    private final String path = "java-backend/auth.json";

    public AuthConfigLoader() {
        // Set location of auth.json file
    }

    // read information from auth.json
    public AuthDTO getConfig() {
        return null;
    }

    /**
     * Write to the auth.json config file
     * @param authDTO Config information which we want to save
     * @return True if the save was successful
     */
    public boolean setConfig(AuthDTO authDTO) {
        return false;
    }
}
