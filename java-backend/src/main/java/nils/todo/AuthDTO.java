package nils.todo;

/**
 * DTO for the auth.json file
 * @param clientId ID of client
 * @param clientSecret Secret of the client
 * @param tenantId ID of tenant
 * @param accessToken Token with which we can access the MSGraph API
 * @param refreshToken Token to refresh accessToken without new login
 */
public record AuthDTO(String clientId, String clientSecret, String tenantId,
                      String accessToken, String refreshToken) {
}
