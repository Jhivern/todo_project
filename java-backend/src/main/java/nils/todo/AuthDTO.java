package nils.todo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the auth.json file
 * @param clientId ID of client
 * @param clientSecret Secret of the client
 * @param tenantId ID of tenant
 * @param accessToken Token with which we can access the MSGraph API
 * @param refreshToken Token to refresh accessToken without a new login
 */
public record AuthDTO(@JsonProperty String  clientId, @JsonProperty String clientSecret, @JsonProperty String tenantId,
                      @JsonProperty String accessToken, @JsonProperty String refreshToken) {
}
