package nils.todo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class AuthConfigLoader {
    Path path = Paths.get("auth.json");
    ObjectMapper mapper = new ObjectMapper();

    // read information from auth.json
    public AuthDTO readConfig() {
        try {
            return mapper.readValue(Files.newBufferedReader(path), AuthDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failure while parsing auth.json", e);
        }
    }

    /**
     * Write to the auth.json config file
     * @param authDTO Config information which we want to save
     * @return True if the save was successful
     */
    public boolean writeConfig(AuthDTO authDTO) {
        try {
            mapper.writeValue(Files.newBufferedWriter(path), authDTO);
        }
        catch(Exception e) {
            throw new RuntimeException("Failure while writing to auth.json", e);
        }
        return false;
    }
}
