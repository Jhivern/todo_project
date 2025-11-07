package nils.todo.facades;

import nils.todo.services.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade {

    private final AuthService authService;

    /**
     * Constructor
     * @param authService The underlying Auth business logic
     */
    public AuthFacade(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Starts the login flow. Generates the authURL and opens it in a browser
     */
    public void startLoginFlow() {
        String authUrl = authService.getAuthorizationUrl();
        authService.launchBrowser(authUrl);
    }

    /**
     * Completes the login process by getting the token from the auth code.
     * @param code Code received from Microsoft for Authorization
     */
    public void completeLogin(String code) {
        authService.acquireTokenFromCode(code);
    }

    /**
     * Getter for the access token. Used when building requests
     * @return The access token
     */
    public String getAccessToken() {
        return authService.getAccessToken();
    }

    /**
     * Checks if the authService has an access token. If no, then we are unauthorized
     * @return If we have a valid token
     */
    public boolean hasValidToken() {
        return authService.hasValidToken();
    }

}

