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

    /**
     * Read information from auth.json
     * @return A populated AuthDTO object
     */
    public AuthDTO readConfig() {
        try {
            return mapper.readValue(Files.newBufferedReader(path), AuthDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failure while parsing auth.json", e);
        }
    }

}
