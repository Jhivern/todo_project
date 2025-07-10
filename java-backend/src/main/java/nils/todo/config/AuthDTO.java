package nils.todo.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the auth.json file
 * @param clientId ID of a client
 * @param clientSecret Secret of the client
 * @param tenantId ID of tenant
 */
public record AuthDTO(@JsonProperty String clientId, @JsonProperty String clientSecret,
                      @JsonProperty String tenantId) {
}
